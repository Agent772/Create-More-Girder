package com.agent772.createmoregirder.content.bracket;

import com.agent772.createmoregirder.content.copycat_girder.MimicFaceSampler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Baked model wrapper for copycat bracket blocks. Reads the mimicked block
 * state from {@link CopycatBracketModelProperties} (populated on the host's
 * {@link ModelData} by {@code BracketedKineticBlockModelMixin}) and remaps
 * the bracket's plate/arm UVs onto the mimicked block's textures.
 *
 * <p>Layers are routed per {@link RenderType} exactly like the girder path:
 * a solid core renders on the mimic's primary pass and a translucent glow
 * shell blends over it on the translucent pass. The bracket is rendered
 * through a host model (Create's {@code BracketedKineticBlockModel} or
 * {@code PipeAttachmentModel}) whose render-type set decides which passes
 * are iterated, so the host mixins union {@link #unionWithMimicTypes} into it.
 */
public class CopycatBracketBakedModel extends BakedModelWrapper<BakedModel> {

    private static final int ORIENTATION_COUNT = MimicFaceSampler.ORIENTATION_COUNT;

    public CopycatBracketBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    /**
     * Unions the mimicked block's render types into {@code base} when the
     * given {@code data} carries a mimicked state. Host models decide which
     * render passes get iterated for the bracket's quads, so their
     * {@code getRenderTypes} must advertise the mimic's passes too — the
     * host mixins delegate here.
     */
    public static ChunkRenderTypeSet unionWithMimicTypes(ChunkRenderTypeSet base, RandomSource rand, ModelData data) {
        BlockState mimicked = data.get(CopycatBracketModelProperties.MIMICKED_STATE);
        if (mimicked == null || mimicked.isAir()) {
            return base;
        }
        ChunkRenderTypeSet mimicTypes = mimicRenderTypes(mimicked, rand);
        if (mimicTypes == null) {
            return base;
        }
        return ChunkRenderTypeSet.union(base, mimicTypes);
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand,
                                                     @NotNull ModelData data) {
        return unionWithMimicTypes(super.getRenderTypes(state, rand, data), rand, data);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand,
                                    ModelData data, RenderType renderType) {
        BlockState mimicked = data.get(CopycatBracketModelProperties.MIMICKED_STATE);
        boolean hasMimic = mimicked != null && !mimicked.isAir();

        if (!hasMimic) {
            return super.getQuads(state, side, rand, data, renderType);
        }

        ChunkRenderTypeSet mimicTypes = mimicRenderTypes(mimicked, rand);

        RenderType sampleType;
        boolean includeFallback;
        if (renderType == null || mimicTypes == null) {
            // Item / layer-less query (or lookup failure): emit every layer once.
            sampleType = null;
            includeFallback = true;
        } else if (!mimicTypes.contains(renderType)) {
            // The host is iterating one of its own passes; our layers render on
            // the mimic's passes, which the host mixins union into its render types.
            return Collections.emptyList();
        } else {
            sampleType = renderType;
            includeFallback = renderType.equals(MimicFaceSampler.primaryRenderType(mimicTypes));
        }

        // Fetch with renderType=null so the bracket JSON's declared layer doesn't
        // strip the quads before we route them onto the mimic's render layer.
        List<BakedQuad> base = super.getQuads(state, side, rand, data, null);
        if (base.isEmpty()) {
            return base;
        }

        Integer rot = data.get(CopycatBracketModelProperties.FACE_ROTATION);
        int orientation = rot == null ? 0 : Math.floorMod(rot, ORIENTATION_COUNT);

        List<MimicFaceSampler.Layer>[] faceLayers =
                MimicFaceSampler.sampleLayers(mimicked, orientation, sampleType, includeFallback);
        if (faceLayers == null) {
            return base;
        }

        List<BakedQuad> out = new ArrayList<>(base.size());
        for (BakedQuad quad : base) {
            MimicFaceSampler.emitLayers(out, quad, faceLayers);
        }
        return out;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
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
}
