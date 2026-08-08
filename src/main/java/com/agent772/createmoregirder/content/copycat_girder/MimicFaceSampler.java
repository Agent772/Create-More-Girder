package com.agent772.createmoregirder.content.copycat_girder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.core.Direction.DOWN;
import static net.minecraft.core.Direction.EAST;
import static net.minecraft.core.Direction.NORTH;
import static net.minecraft.core.Direction.SOUTH;
import static net.minecraft.core.Direction.UP;
import static net.minecraft.core.Direction.WEST;

/**
 * Shared sampling logic for the mimic ("copycat") render pipeline. All four remap
 * paths (girder, metal girder, bracket, strut anchor, strut beams) resolve the
 * mimicked block's textures through this class so the behaviour stays identical.
 *
 * <p>Two fidelity fixes live here (see issue #167):
 * <ul>
 *   <li><b>Unculled-quad fallback.</b> Blockbench-exported models (e.g. Foxy's Mod
 *       glowing blocks) declare no {@code cullface}, so {@code getQuads(state, side)}
 *       returns an empty list for every side and the old code fell back to the
 *       particle texture (plain concrete) &mdash; the reported "neon green looks
 *       yellow". We now also scan the unculled ({@code side == null}) list and match
 *       quads by geometric direction before falling back to the particle sprite.</li>
 *   <li><b>Multi-layer sampling.</b> Glowing blocks stack a solid core plus a
 *       translucent glow shell on the same face. Sampling is done per {@link RenderType}
 *       so each render pass yields only its own layer, and the caller emits one
 *       remapped quad per sampled source quad instead of collapsing to one sprite
 *       per face.</li>
 *   <li><b>Authored layer geometry ("glass contour").</b> Create's copycats crop the
 *       material's <em>real</em> quads into the copycat box
 *       ({@code BakedModelHelper.cropAndMove}), so an inset element (e.g. Foxy's
 *       concrete core at 0.5..15.5 under a full-cube glass shell) keeps its inset:
 *       a glass-only translucent ring frames every face and the layers sit on
 *       different planes (parallax). Our repaint pipeline used to draw every layer
 *       coplanar and full-size on the girder quads, erasing that contour. Each
 *       {@link Layer} now records the source quad's inset from its face plane and
 *       its in-plane footprint, and {@link #remapLayerQuad} crops the remapped quad
 *       to it with the same clamp-plus-proportional-UV semantics as Create's
 *       {@code cropAndMove} (computed from the quad's own UV mapping rather than
 *       assuming vanilla texel density).</li>
 * </ul>
 *
 * <p>Quads that request a tint index are skipped: the copycat pipeline does not yet
 * resolve block-colour tints, so drawing a greyscale overlay would look worse (white
 * film) than omitting it. This keeps tinted blocks (grass, leaves) rendering exactly
 * as before.
 */
public final class MimicFaceSampler {

    public static final int ORIENTATION_COUNT = 6;

    private static final long QUAD_SAMPLING_SEED = 42L;

    /**
     * For each of the 6 orientations, maps a girder face to the source block face
     * that should supply the texture, indexed by
     * {@code [orientation][girderFace.get3DDataValue()]}.
     */
    private static final Direction[][] FACE_MAPPINGS = {
        { DOWN,  UP,    NORTH, SOUTH, WEST, EAST },
        { UP,    DOWN,  NORTH, SOUTH, EAST, WEST },
        { NORTH, SOUTH, UP,    DOWN,  WEST, EAST },
        { SOUTH, NORTH, DOWN,  UP,    WEST, EAST },
        { EAST,  WEST,  NORTH, SOUTH, DOWN, UP   },
        { WEST,  EAST,  NORTH, SOUTH, UP,   DOWN },
    };

    private MimicFaceSampler() {
    }

    /**
     * A single texture layer to draw on a girder face. {@code shade} is the source
     * quad's authored flag, carried over unchanged so the remapped quad renders
     * exactly like base Create's copycats, which draw the material's own quads.
     * {@code inset} is the source quad's authored geometry (see {@link Inset});
     * {@link Inset#NONE} means full-face.
     */
    public record Layer(TextureAtlasSprite sprite, int lightmap, boolean shade, Inset inset) {}

    /**
     * Authored geometry of a source quad relative to the unit cube of its block:
     * {@code depth} is its inset from the face plane along the face normal, and
     * {@code minA/maxA/minB/maxB} its footprint on the two tangent axes in
     * X&lt;Y&lt;Z order. Tangent bounds are applied to the girder face's sorted
     * tangent axes without accounting for in-plane rotation, mirroring the UV
     * remap; exact for the symmetric insets these models author.
     */
    public record Inset(float depth, float minA, float maxA, float minB, float maxB) {

        public static final Inset NONE = new Inset(0f, 0f, 1f, 0f, 1f);

        public boolean isNone() {
            return depth <= EPS && minA <= EPS && 1f - maxA <= EPS && minB <= EPS && 1f - maxB <= EPS;
        }
    }

    private static final float EPS = 1.0e-4f;

    /**
     * Samples the mimicked model, returning per-face lists of {@link Layer}s to draw.
     * The returned array is indexed by {@code girderFace.get3DDataValue()}; each entry
     * is a (possibly empty) list.
     *
     * @param mimicked        the block being mimicked
     * @param orientation     face-rotation orientation (0..5)
     * @param renderType      the render layer being drawn, or {@code null} to collect
     *                        every layer in a single pass (item / no-layer queries)
     * @param includeFallback when {@code true}, faces with no matching source geometry
     *                        receive a particle-texture fallback layer; pass {@code true}
     *                        only for the primary render pass to avoid duplicate draws
     */
    @Nullable
    public static List<Layer>[] sampleLayers(BlockState mimicked, int orientation,
                                             @Nullable RenderType renderType, boolean includeFallback) {
        try {
            BakedModel srcModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimicked);
            TextureAtlasSprite fallback = srcModel.getParticleIcon(ModelData.EMPTY);
            @SuppressWarnings("unchecked")
            List<Layer>[] faces = new List[6];
            Direction[] mapping = FACE_MAPPINGS[Math.floorMod(orientation, ORIENTATION_COUNT)];
            int emission = mimicked.getLightEmission();

            for (Direction dir : Direction.values()) {
                Direction sourceFace = mapping[dir.get3DDataValue()];
                List<BakedQuad> quads = sampleFaceQuads(srcModel, mimicked, sourceFace, renderType);
                List<Layer> layers = new ArrayList<>(quads.size());
                for (BakedQuad quad : quads) {
                    if (quad.getTintIndex() != -1) {
                        // No tint support yet: skip so we don't draw an untinted greyscale overlay.
                        continue;
                    }
                    layers.add(toLayer(quad, emission));
                }
                if (layers.isEmpty() && includeFallback) {
                    layers.add(fallbackLayer(fallback, emission));
                }
                faces[dir.get3DDataValue()] = layers;
            }
            return faces;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Emits one UV-remapped copy of {@code quad} per sampled layer of its face.
     * Faces with an empty layer list emit nothing (their layers belong to another
     * render pass or were skipped as tinted).
     */
    public static void emitLayers(List<BakedQuad> out, BakedQuad quad, List<Layer>[] faceLayers) {
        Direction face = quad.getDirection();
        List<Layer> layers = face != null ? faceLayers[face.get3DDataValue()] : faceLayers[0];
        if (layers == null) {
            return;
        }
        for (Layer layer : layers) {
            BakedQuad remapped = remapLayerQuad(quad, layer);
            if (remapped != null) {
                out.add(remapped);
            }
        }
    }

    /**
     * Remaps {@code quad} onto {@code layer}'s sprite and crops it to the layer's
     * authored {@link Inset}, reproducing the geometry Create's copycats keep by
     * cropping the material's real quads. Returns {@code null} when the quad's
     * footprint lies entirely outside the layer (that region belongs to another
     * layer, e.g. the glass-only contour ring around an inset core).
     */
    @Nullable
    public static BakedQuad remapLayerQuad(BakedQuad quad, Layer layer) {
        BakedQuad remapped = remapQuadUVs(quad, layer.sprite(), layer.lightmap(), layer.shade());
        Inset inset = layer.inset();
        Direction dir = remapped.getDirection();
        if (inset.isNone() || dir == null) {
            return remapped;
        }
        return cropQuad(remapped, dir, inset, layer.sprite(), layer.shade());
    }

    private static List<BakedQuad> sampleFaceQuads(BakedModel srcModel, BlockState mimicked,
                                                   Direction sourceFace, @Nullable RenderType renderType) {
        List<BakedQuad> sided = srcModel.getQuads(mimicked, sourceFace,
            RandomSource.create(QUAD_SAMPLING_SEED), ModelData.EMPTY, renderType);
        if (!sided.isEmpty()) {
            return sided;
        }
        // Unculled fallback: match quads from the side==null list by geometric direction.
        List<BakedQuad> unculled = srcModel.getQuads(mimicked, null,
            RandomSource.create(QUAD_SAMPLING_SEED), ModelData.EMPTY, renderType);
        if (unculled.isEmpty()) {
            return unculled;
        }
        List<BakedQuad> matched = new ArrayList<>();
        for (BakedQuad quad : unculled) {
            if (quad.getDirection() == sourceFace) {
                matched.add(quad);
            }
        }
        return matched;
    }

    private static Layer toLayer(BakedQuad quad, int emission) {
        int[] verts = quad.getVertices();
        int vertexSize = verts.length / 4;
        int lightmap = vertexSize > 6 ? verts[6] : 0;
        if (emission > 0) {
            // Create's copycats glow because the block forwards the material's light
            // emission into the world and renders the quads normally. Consumers max
            // this lightmap with world light, so in-world the result is identical,
            // and paths whose host cannot forward emission (brackets, contraptions)
            // still glow. Never force fullbright or strip the authored shade flag:
            // that flattened emissive mimics into a uniform unshaded colour unlike
            // base Create's copycats.
            lightmap = maxLightmap(lightmap, LightTexture.pack(emission, 0));
        }
        return new Layer(quad.getSprite(), lightmap, quad.isShade(), computeInset(quad));
    }

    private static Layer fallbackLayer(TextureAtlasSprite fallback, int emission) {
        int lightmap = emission > 0 ? LightTexture.pack(emission, 0) : 0;
        return new Layer(fallback, lightmap, true, Inset.NONE);
    }

    /**
     * Derives the source quad's authored geometry: its inset from the unit-cube
     * face plane and its in-plane footprint. Non-planar (slanted) quads and quads
     * inset past the cube's centre return {@link Inset#NONE} so they render
     * full-face rather than mis-cropped.
     */
    private static Inset computeInset(BakedQuad quad) {
        Direction dir = quad.getDirection();
        if (dir == null) {
            return Inset.NONE;
        }
        int[] verts = quad.getVertices();
        int stride = verts.length / 4;
        float[] min = { Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE };
        float[] max = { -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE };
        for (int v = 0; v < 4; v++) {
            for (int c = 0; c < 3; c++) {
                float p = Float.intBitsToFloat(verts[v * stride + c]);
                min[c] = Math.min(min[c], p);
                max[c] = Math.max(max[c], p);
            }
        }
        int n = dir.getAxis().ordinal();
        if (max[n] - min[n] > EPS) {
            return Inset.NONE;
        }
        float depth = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1f - max[n] : min[n];
        if (depth <= EPS) {
            depth = 0f;
        }
        if (depth < 0f || depth >= 0.5f) {
            return Inset.NONE;
        }
        int a = n == 0 ? 1 : 0;
        int b = n == 2 ? 1 : 2;
        Inset inset = new Inset(depth,
            clamp01(min[a]), clamp01(max[a]),
            clamp01(min[b]), clamp01(max[b]));
        return inset.isNone() ? Inset.NONE : inset;
    }

    private static float clamp01(float f) {
        return Math.min(1f, Math.max(0f, f));
    }

    /**
     * Crops {@code quad} to the layer's authored geometry: the girder-face
     * footprint is clamped to the layer's tangent bounds and the whole quad is
     * recessed behind the painted face by {@code depth} along its normal. Same
     * semantics as Create's {@code BakedModelHelper.cropAndMove} (clamp vertices,
     * shift UVs proportionally), but the UV shift uses the quad's own affine UV
     * mapping so it stays exact for girder UV layouts. Returns {@code null} when
     * the quad lies entirely outside the layer's footprint; slanted quads are
     * returned unmodified.
     */
    @Nullable
    private static BakedQuad cropQuad(BakedQuad quad, Direction dir, Inset inset,
                                      TextureAtlasSprite sprite, boolean shade) {
        int[] src = quad.getVertices();
        int stride = src.length / 4;
        float[][] pos = new float[4][3];
        float[][] uv = new float[4][2];
        for (int v = 0; v < 4; v++) {
            for (int c = 0; c < 3; c++) {
                pos[v][c] = Float.intBitsToFloat(src[v * stride + c]);
            }
            uv[v][0] = Float.intBitsToFloat(src[v * stride + 4]);
            uv[v][1] = Float.intBitsToFloat(src[v * stride + 5]);
        }

        int n = dir.getAxis().ordinal();
        int a = n == 0 ? 1 : 0;
        int b = n == 2 ? 1 : 2;
        float[] qMin = { Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE };
        float[] qMax = { -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE };
        for (int v = 0; v < 4; v++) {
            for (int c = 0; c < 3; c++) {
                qMin[c] = Math.min(qMin[c], pos[v][c]);
                qMax[c] = Math.max(qMax[c], pos[v][c]);
            }
        }
        if (qMax[n] - qMin[n] > EPS) {
            // Slanted quad (e.g. the girder's diagonal lattice braces): clamping or
            // offsetting individual vertices would bend it out of plane. Leave the
            // geometry untouched so every layer stays vertex-identical there, which
            // cannot z-fight.
            return quad;
        }

        float[] boxMin = { 0f, 0f, 0f };
        float[] boxMax = { 1f, 1f, 1f };
        boxMin[a] = Math.max(boxMin[a], inset.minA());
        boxMax[a] = Math.min(boxMax[a], inset.maxA());
        boxMin[b] = Math.max(boxMin[b], inset.minB());
        boxMax[b] = Math.min(boxMax[b], inset.maxB());
        for (int axis : new int[] { a, b }) {
            if (Math.min(qMax[axis], boxMax[axis]) - Math.max(qMin[axis], boxMin[axis]) <= EPS) {
                // Entirely outside the layer's footprint (e.g. inside the
                // glass-only contour ring): this layer draws nothing here.
                return null;
            }
        }

        // Recess the layer behind the painted face by its authored depth on every
        // face, not only cube-boundary faces. Clamping only at the cube boundary
        // left interior faces with coplanar-but-differently-shaved layer pairs,
        // whose interpolated depths differ by float rounding and z-fight (visible
        // as flicker); a uniform recess keeps the layers separated everywhere and
        // matches the parallax the authored geometry has.
        float shift = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE
            ? -inset.depth()
            : inset.depth();

        // Affine UV basis from the quad's own edges (0->1 and 0->3).
        float[] e1 = new float[3];
        float[] e3 = new float[3];
        for (int c = 0; c < 3; c++) {
            e1[c] = pos[1][c] - pos[0][c];
            e3[c] = pos[3][c] - pos[0][c];
        }
        float len1Sq = e1[0] * e1[0] + e1[1] * e1[1] + e1[2] * e1[2];
        float len3Sq = e3[0] * e3[0] + e3[1] * e3[1] + e3[2] * e3[2];
        float du1 = uv[1][0] - uv[0][0];
        float dv1 = uv[1][1] - uv[0][1];
        float du3 = uv[3][0] - uv[0][0];
        float dv3 = uv[3][1] - uv[0][1];

        int[] dst = src.clone();
        boolean changed = false;
        for (int v = 0; v < 4; v++) {
            float[] diff = new float[3];
            boolean moved = false;
            for (int c = 0; c < 3; c++) {
                float target = c == n
                    ? clamp01(pos[v][c] + shift)
                    : Math.min(boxMax[c], Math.max(boxMin[c], pos[v][c]));
                diff[c] = target - pos[v][c];
                if (diff[c] != 0f) {
                    moved = true;
                }
                dst[v * stride + c] = Float.floatToRawIntBits(target);
            }
            if (!moved) {
                continue;
            }
            changed = true;
            float s1 = len1Sq > EPS ? (diff[0] * e1[0] + diff[1] * e1[1] + diff[2] * e1[2]) / len1Sq : 0f;
            float s3 = len3Sq > EPS ? (diff[0] * e3[0] + diff[1] * e3[1] + diff[2] * e3[2]) / len3Sq : 0f;
            dst[v * stride + 4] = Float.floatToRawIntBits(uv[v][0] + s1 * du1 + s3 * du3);
            dst[v * stride + 5] = Float.floatToRawIntBits(uv[v][1] + s1 * dv1 + s3 * dv3);
        }
        if (!changed) {
            return quad;
        }
        return new BakedQuad(dst, quad.getTintIndex(), dir, sprite, shade);
    }

    /** Per-component (block/sky) max of two packed lightmaps. */
    private static int maxLightmap(int a, int b) {
        return Math.max(a & 0xFFFF, b & 0xFFFF) | Math.max(a & 0xFFFF0000, b & 0xFFFF0000);
    }

    /**
     * The render type that owns the particle-fallback draw. Faces with no source
     * geometry are only drawn on this (first) mimic render type so the fallback quad
     * cannot leak onto other passes.
     */
    @Nullable
    public static RenderType primaryRenderType(ChunkRenderTypeSet mimicTypes) {
        for (RenderType type : mimicTypes) {
            return type;
        }
        return null;
    }

    /**
     * Rewrites {@code orig}'s UVs from its own sprite onto {@code sourceSprite}, keeping
     * the girder geometry but painting it with the mimicked texture. Optionally forces a
     * lightmap value (used for emissive layers).
     */
    public static BakedQuad remapQuadUVs(BakedQuad orig, TextureAtlasSprite sourceSprite,
                                         int sourceLightmap, boolean shade) {
        TextureAtlasSprite baseSprite = orig.getSprite();
        int[] src = orig.getVertices();
        int[] dst = src.clone();
        int vertexSize = dst.length / 4;
        float gU0 = baseSprite.getU0();
        float gV0 = baseSprite.getV0();
        float gUSpan = baseSprite.getU1() - gU0;
        float gVSpan = baseSprite.getV1() - gV0;
        if (gUSpan == 0f || gVSpan == 0f) {
            return orig;
        }
        float sU0 = sourceSprite.getU0();
        float sV0 = sourceSprite.getV0();
        float sUSpan = sourceSprite.getU1() - sU0;
        float sVSpan = sourceSprite.getV1() - sV0;
        for (int v = 0; v < 4; v++) {
            int off = v * vertexSize;
            float u = Float.intBitsToFloat(dst[off + 4]);
            float vv = Float.intBitsToFloat(dst[off + 5]);
            float fu = (u - gU0) / gUSpan;
            float fv = (vv - gV0) / gVSpan;
            dst[off + 4] = Float.floatToRawIntBits(sU0 + fu * sUSpan);
            dst[off + 5] = Float.floatToRawIntBits(sV0 + fv * sVSpan);
            if (sourceLightmap != 0 && vertexSize > 6) {
                dst[off + 6] = sourceLightmap;
            }
        }
        return new BakedQuad(dst, orig.getTintIndex(), orig.getDirection(), sourceSprite, shade);
    }
}
