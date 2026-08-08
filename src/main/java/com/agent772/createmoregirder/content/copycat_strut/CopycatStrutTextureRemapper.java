package com.agent772.createmoregirder.content.copycat_strut;

import com.agent772.createmoregirder.content.copycat_girder.MimicFaceSampler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Remaps UV coordinates on strut beam quads to use the mimicked block's texture.
 * Delegates sampling to {@link MimicFaceSampler}. Beams composite the mimicked block
 * exactly like the copycat girder does: the first (base) layer of each face renders on
 * the beam's primary pass, and every further layer (e.g. a semi-transparent glow shell)
 * renders alpha-blended on top on the translucent pass, so the resulting pixel is the
 * same {@code shell×α + core×(1−α)} blend the girder shows.
 */
@OnlyIn(Dist.CLIENT)
public class CopycatStrutTextureRemapper {

    /**
     * Per-face layer stack for beam rendering. {@code base[face]} is the bottom layer
     * (always present thanks to the particle fallback); {@code overlays[depth][face]}
     * holds the remaining layers in draw order, {@code null} where a face has no layer
     * at that depth.
     */
    public record BeamLayers(FaceData[] base, FaceData[][] overlays) {}

    @Nullable
    public static BeamLayers resolveBeamLayers(BlockState mimicked, int faceRotation) {
        List<MimicFaceSampler.Layer>[] faceLayers =
                MimicFaceSampler.sampleLayers(mimicked, faceRotation, null, true);
        if (faceLayers == null) {
            return null;
        }
        int overlayDepth = 0;
        for (List<MimicFaceSampler.Layer> layers : faceLayers) {
            overlayDepth = Math.max(overlayDepth, layers.size() - 1);
        }
        FaceData[] base = new FaceData[6];
        FaceData[][] overlays = new FaceData[overlayDepth][6];
        for (int i = 0; i < 6; i++) {
            List<MimicFaceSampler.Layer> layers = faceLayers[i];
            if (layers.isEmpty()) {
                continue;
            }
            base[i] = toFaceData(layers.get(0));
            for (int d = 1; d < layers.size(); d++) {
                overlays[d - 1][i] = toFaceData(layers.get(d));
            }
        }
        return new BeamLayers(base, overlays);
    }

    private static FaceData toFaceData(MimicFaceSampler.Layer layer) {
        return new FaceData(layer.sprite(), layer.lightmap(), layer.shade());
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
        return remapQuadUVs(orig, sourceSprite, orig.isShade());
    }

    public static BakedQuad remapQuadUVs(BakedQuad orig, TextureAtlasSprite sourceSprite, boolean shade) {
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
        return new BakedQuad(dst, orig.getTintIndex(), orig.getDirection(), sourceSprite, shade);
    }

    public record FaceData(TextureAtlasSprite sprite, int lightmap, boolean shade) {}
}
