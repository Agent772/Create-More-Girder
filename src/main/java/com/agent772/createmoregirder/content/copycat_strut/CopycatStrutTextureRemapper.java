package com.agent772.createmoregirder.content.copycat_strut;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CopycatStrutTextureRemapper {

    private static final long QUAD_SAMPLING_SEED = 42L;

    private static final Direction[][] FACE_MAPPINGS = {
        { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST },
        { Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST },
        { Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST },
        { Direction.SOUTH, Direction.NORTH, Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST },
        { Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP },
        { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN },
    };

    @Nullable
    public static FaceData[] resolveFaceData(BlockState mimicked, int faceRotation) {
        try {
            BakedModel srcModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimicked);
            TextureAtlasSprite fallback = srcModel.getParticleIcon(ModelData.EMPTY);
            FaceData[] faces = new FaceData[6];
            int orientation = Math.floorMod(faceRotation, 6);
            Direction[] mapping = FACE_MAPPINGS[orientation];
            int emission = mimicked.getLightEmission();
            boolean srcUsesAO = srcModel.useAmbientOcclusion();

            for (Direction dir : Direction.values()) {
                Direction sourceFace = mapping[dir.get3DDataValue()];
                List<BakedQuad> quads = srcModel.getQuads(mimicked, sourceFace,
                        RandomSource.create(QUAD_SAMPLING_SEED), ModelData.EMPTY, null);
                if (!quads.isEmpty()) {
                    BakedQuad srcQuad = quads.get(0);
                    boolean shade = srcQuad.isShade() && srcUsesAO;
                    int lightmap = 0;
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

    @Nullable
    public static TextureAtlasSprite getParticleSprite(BlockState mimicked) {
        try {
            BakedModel srcModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimicked);
            return srcModel.getParticleIcon(ModelData.EMPTY);
        } catch (Exception e) {
            return null;
        }
    }

    public static BakedQuad remapQuadUVs(BakedQuad orig, TextureAtlasSprite sourceSprite) {
        TextureAtlasSprite girderSprite = orig.getSprite();
        int[] src = orig.getVertices();
        int[] dst = src.clone();
        int vertexSize = dst.length / 4;
        float gU0 = girderSprite.getU0();
        float gV0 = girderSprite.getV0();
        float gUSpan = girderSprite.getU1() - gU0;
        float gVSpan = girderSprite.getV1() - gV0;
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
        }
        return new BakedQuad(dst, orig.getTintIndex(), orig.getDirection(), sourceSprite, orig.isShade());
    }

    public record FaceData(TextureAtlasSprite sprite, int lightmap, boolean shade) {}
}
