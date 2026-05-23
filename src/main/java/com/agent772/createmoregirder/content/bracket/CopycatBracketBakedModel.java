package com.agent772.createmoregirder.content.bracket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.NotNull;
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
 * Baked model wrapper for copycat bracket blocks. Reads the mimicked block
 * state from {@link CopycatBracketModelProperties} (populated on the host's
 * {@link ModelData} by {@code BracketedKineticBlockModelMixin}) and remaps
 * the bracket's plate/arm UVs onto the mimicked block's textures.
 */
public class CopycatBracketBakedModel extends BakedModelWrapper<BakedModel> {

    private static final int ORIENTATION_COUNT = 6;
    private static final long QUAD_SAMPLING_SEED = 42L;

    private static final Direction[][] FACE_MAPPINGS = {
        { DOWN,  UP,   NORTH, SOUTH, WEST, EAST },
        { UP,    DOWN, NORTH, SOUTH, EAST, WEST },
        { NORTH, SOUTH, UP,   DOWN,  WEST, EAST },
        { SOUTH, NORTH, DOWN, UP,    WEST, EAST },
        { EAST,  WEST,  NORTH, SOUTH, DOWN, UP  },
        { WEST,  EAST,  NORTH, SOUTH, UP,  DOWN },
    };

    public CopycatBracketBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand,
                                                     @NotNull ModelData data) {
        ChunkRenderTypeSet baseTypes = super.getRenderTypes(state, rand, data);
        BlockState mimicked = data.get(CopycatBracketModelProperties.MIMICKED_STATE);
        if (mimicked == null || mimicked.isAir()) return baseTypes;
        ChunkRenderTypeSet mimicTypes = mimicRenderTypes(mimicked, rand);
        if (mimicTypes == null) return baseTypes;
        return ChunkRenderTypeSet.union(baseTypes, mimicTypes);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand,
                                    ModelData data, RenderType renderType) {
        BlockState mimicked = data.get(CopycatBracketModelProperties.MIMICKED_STATE);
        boolean hasMimic = mimicked != null && !mimicked.isAir();

        if (!hasMimic) {
            return super.getQuads(state, side, rand, data, renderType);
        }

        // Render the mimic-skinned quads on whichever render layer the host
        // iterates. We intentionally do NOT filter by mimicTypes here: the host
        // (BracketedKineticBlockModel / PipeAttachmentModel) decides which layers
        // get iterated based on its own model, not on ours, and on hosts like the
        // glass pipe (cutoutMipped-only) the bracket would otherwise be invisible.
        // Hosts that iterate a single layer (the common case) render the bracket
        // exactly once; the rare multi-layer hosts may overdraw it, but for the
        // opaque materials typically used as a mimic the second pass is
        // visually identical to the first.
        List<BakedQuad> base = super.getQuads(state, side, rand, data, null);
        if (base.isEmpty()) {
            return base;
        }

        Integer rot = data.get(CopycatBracketModelProperties.FACE_ROTATION);
        int orientation = rot == null ? 0 : Math.floorMod(rot, ORIENTATION_COUNT);

        FaceData[] faceData = resolveFaceData(mimicked, orientation);
        if (faceData == null) return base;

        List<BakedQuad> out = new ArrayList<>(base.size());
        for (BakedQuad quad : base) {
            Direction face = quad.getDirection();
            FaceData fd = face != null ? faceData[face.get3DDataValue()] : faceData[0];
            if (fd != null && fd.sprite != null) {
                out.add(remapQuadUVs(quad, fd.sprite, fd.lightmap, fd.shade));
            } else {
                out.add(quad);
            }
        }
        return out;
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        BlockState mimicked = data.get(CopycatBracketModelProperties.MIMICKED_STATE);
        if (mimicked != null && !mimicked.isAir()) {
            try {
                BakedModel src = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimicked);
                TextureAtlasSprite particle = src.getParticleIcon(ModelData.EMPTY);
                if (particle != null) return particle;
            } catch (Exception ignored) {}
        }
        return super.getParticleIcon(data);
    }

    @Override
    public TriState useAmbientOcclusion(BlockState state, ModelData data, RenderType renderType) {
        BlockState mimicked = data.get(CopycatBracketModelProperties.MIMICKED_STATE);
        if (mimicked != null && !mimicked.isAir()) {
            try {
                BakedModel srcModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimicked);
                if (!srcModel.useAmbientOcclusion()) return TriState.FALSE;
            } catch (Exception ignored) {}
        }
        return super.useAmbientOcclusion(state, data, renderType);
    }

    @Nullable
    private static ChunkRenderTypeSet mimicRenderTypes(BlockState mimicked, RandomSource rand) {
        try {
            BakedModel mimicModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimicked);
            return mimicModel.getRenderTypes(mimicked, rand, ModelData.EMPTY);
        } catch (Exception e) {
            return null;
        }
    }

    private record FaceData(TextureAtlasSprite sprite, int lightmap, boolean shade) {}

    @Nullable
    private FaceData[] resolveFaceData(BlockState mimicked, int orientation) {
        try {
            BakedModel srcModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimicked);
            TextureAtlasSprite fallback = srcModel.getParticleIcon(ModelData.EMPTY);
            FaceData[] faces = new FaceData[6];
            Direction[] mapping = FACE_MAPPINGS[orientation];
            int emission = mimicked.getLightEmission();
            boolean srcUsesAO = srcModel.useAmbientOcclusion();

            for (Direction dir : Direction.values()) {
                Direction sourceFace = mapping[dir.get3DDataValue()];
                List<BakedQuad> quads = srcModel.getQuads(mimicked, sourceFace,
                    RandomSource.create(QUAD_SAMPLING_SEED), ModelData.EMPTY, null);
                if (!quads.isEmpty()) {
                    BakedQuad srcQuad = quads.get(0);
                    int[] verts = srcQuad.getVertices();
                    int vertexSize = verts.length / 4;
                    int lightmap = vertexSize > 6 ? verts[6] : 0;
                    boolean shade = srcQuad.isShade() && srcUsesAO;
                    if (emission > 0) {
                        lightmap = LightTexture.FULL_BRIGHT;
                        shade = false;
                    }
                    faces[dir.get3DDataValue()] = new FaceData(srcQuad.getSprite(), lightmap, shade);
                } else {
                    boolean shade = emission <= 0 && srcUsesAO;
                    int lightmap = emission > 0 ? LightTexture.FULL_BRIGHT : 0;
                    faces[dir.get3DDataValue()] = new FaceData(fallback, lightmap, shade);
                }
            }
            return faces;
        } catch (Exception e) {
            return null;
        }
    }

    private BakedQuad remapQuadUVs(BakedQuad orig, TextureAtlasSprite sourceSprite, int sourceLightmap, boolean shade) {
        TextureAtlasSprite bracketSprite = orig.getSprite();
        int[] src = orig.getVertices();
        int[] dst = src.clone();
        int vertexSize = dst.length / 4;
        float gU0 = bracketSprite.getU0();
        float gV0 = bracketSprite.getV0();
        float gUSpan = bracketSprite.getU1() - gU0;
        float gVSpan = bracketSprite.getV1() - gV0;
        if (gUSpan == 0f || gVSpan == 0f) return orig;
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
