package com.agent772.createmoregirder.content.strut.geometry;

import com.agent772.createmoregirder.content.strut.IBlockEntityRelighter;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * Girder Strut Block implementation
 * 
 * Adapted from Bits-n-Bobs by Industrialists-Of-Create
 * Original: https://github.com/Industrialists-Of-Create/Bits-n-Bobs
 * Licensed under MIT License
 * 
 * Modifications:
 * - Adapted for Create: More Girder mod structure
 */


public final class GirderGeometry {

    public static final float EPSILON = 1.0e-4f;
    public static final int DEFAULT_COLOR = 0xFFFFFFFF;
    public static final int DEFAULT_LIGHT = LightTexture.pack(15, 15);

    /**
     * Vanilla's directional diffuse factor ({@code x²·0.6 + y²·(3+y)/4 + z²·0.8},
     * clamped to 1). For axis-aligned normals this evaluates to exactly the chunk
     * renderer's per-face shade constants (0.5 down, 0.6 east/west, 0.8 north/south,
     * 1.0 up). Mimicking beams bake this into their vertex colours for layers whose
     * source quad is shaded (and leave unshaded layers white) so the BE-rendered beam
     * shades each face exactly like the chunk-rendered copycat girder, instead of
     * using {@code SuperByteBuffer}'s slightly different all-or-nothing diffuse.
     */
    public static float diffuse(final float nx, final float ny, final float nz) {
        return Math.min(nx * nx * 0.6f + ny * ny * ((3f + ny) / 4f) + nz * nz * 0.8f, 1f);
    }

    public static int diffuseColor(final Vector3f normal) {
        float f = diffuse(normal.x, normal.y, normal.z);
        if (!Float.isFinite(f)) {
            return DEFAULT_COLOR;
        }
        final int c = (int) Mth.clamp(255f * f + 0.5f, 0f, 255f);
        return 0xFF000000 | (c << 16) | (c << 8) | c;
    }

    public static float signedDistance(final Vector3f point, final Vector3f planeNormal, final Vector3f planePoint) {
        return new Vector3f(point).sub(planePoint).dot(planeNormal);
    }

    public static GirderVertex interpolate(final GirderVertex start, final GirderVertex end, final float t) {
        final Vector3f position = new Vector3f(start.position()).lerp(end.position(), t);
        final Vector3f normal = new Vector3f(start.normal()).lerp(end.normal(), t);
        if (normal.lengthSquared() > EPSILON) {
            normal.normalize();
        }
        final float u = Mth.lerp(t, start.u(), end.u());
        final float v = Mth.lerp(t, start.v(), end.v());
        final int color = lerpColor(start.color(), end.color(), t);
        final int light = start.light();
        return new GirderVertex(position, normal, u, v, color, light);
    }

    public static int lerpColor(final int a, final int b, final float t) {
        if (a == b) {
            return a;
        }
        final int aA = (a >>> 24) & 0xFF;
        final int aR = (a >>> 16) & 0xFF;
        final int aG = (a >>> 8) & 0xFF;
        final int aB = a & 0xFF;
        final int bA = (b >>> 24) & 0xFF;
        final int bR = (b >>> 16) & 0xFF;
        final int bG = (b >>> 8) & 0xFF;
        final int bB = b & 0xFF;
        final int alpha = (int) Mth.clamp(Mth.lerp(t, aA, bA), 0f, 255f);
        final int red = (int) Mth.clamp(Mth.lerp(t, aR, bR), 0f, 255f);
        final int green = (int) Mth.clamp(Mth.lerp(t, aG, bG), 0f, 255f);
        final int blue = (int) Mth.clamp(Mth.lerp(t, aB, bB), 0f, 255f);
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static int lerpPackedLight(final int a, final int b, final float t) {
        final int blockA = a & 0xFFFF;
        final int skyA = (a >>> 16) & 0xFFFF;
        final int blockB = b & 0xFFFF;
        final int skyB = (b >>> 16) & 0xFFFF;
        final int block = (int) Mth.clamp(Mth.lerp(t, blockA, blockB), 0f, 0xFFFF);
        final int sky = (int) Mth.clamp(Mth.lerp(t, skyA, skyB), 0f, 0xFFFF);
        return (sky << 16) | block;
    }

    public static boolean positionsEqual(final Vector3f a, final Vector3f b) {
        final float dx = a.x - b.x;
        final float dy = a.y - b.y;
        final float dz = a.z - b.z;
        return dx * dx + dy * dy + dz * dz <= EPSILON * EPSILON;
    }

    public static List<GirderVertex> dedupeLoopVertices(final List<GirderVertex> vertices) {
        final List<GirderVertex> cleaned = new ArrayList<>(vertices.size());
        for (final GirderVertex vertex : vertices) {
            if (cleaned.isEmpty() || !positionsEqual(cleaned.get(cleaned.size() - 1).position(), vertex.position())) {
                cleaned.add(vertex);
            }
        }
        if (cleaned.size() >= 2 && positionsEqual(cleaned.get(0).position(), cleaned.get(cleaned.size() - 1).position())) {
            cleaned.remove(cleaned.size() - 1);
        }
        return cleaned;
    }

    public static Vector3f computePolygonNormal(final List<GirderVertex> vertices) {
        final Vector3f normal = new Vector3f();
        final int size = vertices.size();
        for (int i = 0; i < vertices.size(); i++) {
            final Vector3f current = vertices.get(i).position();
            final Vector3f next = vertices.get((i + 1) % size).position();
            normal.x += (current.y - next.y) * (current.z + next.z);
            normal.y += (current.z - next.z) * (current.x + next.x);
            normal.z += (current.x - next.x) * (current.y + next.y);
        }
        return normal.normalize();
    }

    public static void emitPolygon(
            final List<GirderVertex> vertices,
            final TextureAtlasSprite sprite,
            final Direction faceOverride,
            final int tintIndex,
            final boolean shade,
            final List<BakedQuad> consumer
    ) {
        if (vertices.size() == 4) {
            consumer.add(buildQuad(vertices, sprite, faceOverride, tintIndex, shade));
            return;
        }
        if (vertices.size() == 3) {
            consumer.add(buildQuad(Arrays.asList(vertices.get(0), vertices.get(1), vertices.get(2), vertices.get(2)), sprite, faceOverride, tintIndex, shade));
            return;
        }

        final GirderVertex anchor = vertices.get(0);
        for (int i = 1; i < vertices.size() - 1; i++) {
            final List<GirderVertex> tri = Arrays.asList(anchor, vertices.get(i), vertices.get(i + 1), vertices.get(i + 1));
            consumer.add(buildQuad(tri, sprite, faceOverride, tintIndex, shade));
        }
    }

    private static BakedQuad buildQuad(
            final List<GirderVertex> quadVertices,
            final TextureAtlasSprite sprite,
            final Direction faceOverride,
            final int tintIndex,
            final boolean shade
    ) {
        final int stride = BakedQuadHelper.VERTEX_STRIDE;
        final int[] vertexData = new int[stride * 4];
        for (int i = 0; i < quadVertices.size(); i++) {
            final GirderVertex vertex = quadVertices.get(i);
            final Vec3 pos = new Vec3(vertex.position().x, vertex.position().y, vertex.position().z);
            final Vec3 normal = new Vec3(vertex.normal().x, vertex.normal().y, vertex.normal().z);
            BakedQuadHelper.setXYZ(vertexData, i, pos);
            BakedQuadHelper.setNormalXYZ(vertexData, i, normal);
            BakedQuadHelper.setU(vertexData, i, vertex.u());
            BakedQuadHelper.setV(vertexData, i, vertex.v());
            final int baseIndex = i * stride;
            vertexData[baseIndex + BakedQuadHelper.COLOR_OFFSET] = vertex.color();
            vertexData[baseIndex + BakedQuadHelper.LIGHT_OFFSET] = vertex.light();
        }
        final Vector3f avgNormal = GirderGeometry.computePolygonNormal(quadVertices);

//        Vector3f avgNormal = new Vector3f();
//        for (GirderVertex vertex : quadVertices) {
//            avgNormal.add(vertex.normal());
//        }
        Direction face = faceOverride;
        if (avgNormal.lengthSquared() > EPSILON) {
            avgNormal.normalize();
            face = Math.abs(avgNormal.y) > EPSILON ? avgNormal.y < 0 ? Direction.DOWN : Direction.UP :
                    Direction.getNearest(avgNormal.x, avgNormal.y, avgNormal.z);
        }

        return new BakedQuad(vertexData, tintIndex, face, sprite, shade);
    }

    public static float remapU(final float originalU, final TextureAtlasSprite from, final TextureAtlasSprite to) {
        final float fromSpan = from.getU1() - from.getU0();
        final float toSpan = to.getU1() - to.getU0();
        if (Math.abs(fromSpan) <= EPSILON || Math.abs(toSpan) <= EPSILON) {
            return to.getU0();
        }
        return ((originalU - from.getU0()) / fromSpan) * toSpan + to.getU0();
    }

    public static float remapV(final float originalV, final TextureAtlasSprite from, final TextureAtlasSprite to) {
        final float fromSpan = from.getV1() - from.getV0();
        final float toSpan = to.getV1() - to.getV0();
        if (Math.abs(fromSpan) <= EPSILON || Math.abs(toSpan) <= EPSILON) {
            return to.getV0();
        }
        return ((originalV - from.getV0()) / fromSpan) * toSpan + to.getV0();
    }

    public static void emitPolygonToConsumer(
            final List<GirderVertex> verticesToTestRelight,
            final List<Consumer<BufferBuilder>> consumer,
            final Function<Vector3f, Integer> lightFunction) {
        emitPolygonToConsumer(verticesToTestRelight, consumer, lightFunction, 0, false);
    }

    /**
     * Emits the polygon with per-vertex world light from {@code lightFunction}, combined
     * with {@code faceLightmap} when non-zero. The face lightmap carries the mimicked
     * block's sampled emissive lightmap (baked into the source model quad, see
     * {@code MimicFaceSampler#toLayer}); combining it via
     * {@link IBlockEntityRelighter#maximizeLight} makes copycat strut beams glow at the
     * same brightness as copycat girders, rather than falling back to dull world light
     * when the mimicked block emits through a baked lightmap instead of block light.
     *
     * <p>When {@code bakeDiffuse} is set (mimicking beams render with
     * {@code SuperByteBuffer}'s diffuse disabled and reproduce the chunk renderer's
     * per-quad shade instead), the polygon's vertex colour carries {@link #diffuseColor}
     * so shaded layers darken exactly like the girder's, while unshaded (glow) layers
     * pass {@code false} and stay full-bright white.
     */
    public static void emitPolygonToConsumer(
            List<GirderVertex> verticesToTestRelight,
            final List<Consumer<BufferBuilder>> consumer,
            final Function<Vector3f, Integer> lightFunction,
            final int faceLightmap,
            final boolean bakeDiffuse) {
        verticesToTestRelight = dedupeLoopVertices(verticesToTestRelight);
        final Vector3f normal = GirderGeometry.computePolygonNormal(verticesToTestRelight);
        final int color = bakeDiffuse ? diffuseColor(normal) : DEFAULT_COLOR;
        final List<GirderVertex> vertices = new ArrayList<>();

        for (final GirderVertex v : verticesToTestRelight) {
            int light = lightFunction.apply(v.position());
            if (faceLightmap != 0) {
                light = IBlockEntityRelighter.maximizeLight(light, faceLightmap);
            }
            vertices.add(new GirderVertex(
                    v.position(),
                    normal,
                    v.u(),
                    v.v(),
                    color, light
            ));
        }
        if (vertices.size() == 4) {
            consumer.add(buildQuadConsumer(vertices));
            return;
        }
        if (vertices.size() == 3) {
            consumer.add(buildQuadConsumer(Arrays.asList(vertices.get(0), vertices.get(1), vertices.get(2), vertices.get(2))));
            return;
        }

        final GirderVertex anchor = vertices.get(0);
        for (int i = 1; i < vertices.size() - 1; i++) {
            final List<GirderVertex> tri = Arrays.asList(anchor, vertices.get(i), vertices.get(i + 1), vertices.get(i + 1));
            consumer.add(buildQuadConsumer(tri));
        }
    }

    private static Consumer<BufferBuilder> buildQuadConsumer(final List<GirderVertex> tri) {
        return bufferBuilder -> {
            for (final GirderVertex vertex : tri) {
                bufferBuilder.vertex(vertex.position().x, vertex.position().y, vertex.position().z)
                        .color((vertex.color() >> 16) & 0xFF, (vertex.color() >> 8) & 0xFF, vertex.color() & 0xFF, (vertex.color() >> 24) & 0xFF)
                        .uv(vertex.u(), vertex.v())
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(vertex.light())
                        .normal(vertex.normal().x, vertex.normal().y, vertex.normal().z)
                        .endVertex();
            }
        };
    }

}
