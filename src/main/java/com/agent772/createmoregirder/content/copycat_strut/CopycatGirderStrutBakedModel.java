package com.agent772.createmoregirder.content.copycat_strut;

import com.agent772.createmoregirder.content.copycat_girder.MimicFaceSampler;
import com.agent772.createmoregirder.content.strut.GirderStrutBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CopycatGirderStrutBakedModel extends BakedModelWrapper<BakedModel> {

    public static final ModelProperty<BlockState> MIMICKED_STATE = new ModelProperty<>();
    public static final ModelProperty<Integer> FACE_ROTATION = new ModelProperty<>();

    public CopycatGirderStrutBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public @NotNull ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData blockEntityData) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            // Also invalidate the render caches so beams re-render with new texture
            copycatBe.connectionRenderBufferCache = null;
            copycatBe.connectionOverlayRenderBufferCache = null;
            return ModelData.builder()
                    .with(MIMICKED_STATE, copycatBe.getMimickedState())
                    .with(FACE_ROTATION, copycatBe.getFaceRotation())
                    .build();
        }
        if (be instanceof GirderStrutBlockEntity strutBe) {
            strutBe.connectionRenderBufferCache = null;
            strutBe.connectionOverlayRenderBufferCache = null;
        }
        return ModelData.EMPTY;
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand,
                                                     @NotNull ModelData data) {
        ChunkRenderTypeSet baseTypes = super.getRenderTypes(state, rand, data);
        BlockState mimicked = data.get(MIMICKED_STATE);
        if (mimicked == null || mimicked.isAir()) {
            return baseTypes;
        }
        ChunkRenderTypeSet mimicTypes = mimicRenderTypes(mimicked, rand);
        if (mimicTypes == null) {
            return baseTypes;
        }
        return ChunkRenderTypeSet.union(baseTypes, mimicTypes);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand,
                                    ModelData data, RenderType renderType) {
        BlockState mimicked = data.get(MIMICKED_STATE);
        boolean hasMimic = mimicked != null && !mimicked.isAir();

        if (!hasMimic) {
            return super.getQuads(state, side, rand, data, renderType);
        }

        ChunkRenderTypeSet mimicTypes = mimicRenderTypes(mimicked, rand);
        if (mimicTypes == null) {
            return super.getQuads(state, side, rand, data, renderType);
        }
        if (renderType != null && state != null && !mimicTypes.contains(renderType)) {
            return Collections.emptyList();
        }

        // Pass renderType=null so the underlying JSON-declared filter doesn't strip
        // quads when we're routing them onto the mimic's render layer (e.g. translucent).
        List<BakedQuad> base = super.getQuads(state, side, rand, data, null);
        if (base.isEmpty()) {
            return base;
        }
        Integer rot = data.get(FACE_ROTATION);
        int orientation = rot == null ? 0 : Math.floorMod(rot, MimicFaceSampler.ORIENTATION_COUNT);

        RenderType sampleType;
        boolean includeFallback;
        if (renderType == null || state == null) {
            sampleType = null;
            includeFallback = true;
        } else {
            sampleType = renderType;
            includeFallback = renderType.equals(MimicFaceSampler.primaryRenderType(mimicTypes));
        }
        List<MimicFaceSampler.Layer>[] faceLayers =
                MimicFaceSampler.sampleLayers(mimicked, orientation, sampleType, includeFallback);
        if (faceLayers == null) {
            return base;
        }

        List<BakedQuad> out = new ArrayList<>(base.size());
        for (BakedQuad quad : base) {
            Direction face = quad.getDirection();
            List<MimicFaceSampler.Layer> layers = face != null ? faceLayers[face.get3DDataValue()] : faceLayers[0];
            if (layers == null || layers.isEmpty()) {
                out.add(quad);
                continue;
            }
            for (MimicFaceSampler.Layer layer : layers) {
                BakedQuad remapped = MimicFaceSampler.remapLayerQuad(quad, layer);
                if (remapped != null) {
                    out.add(remapped);
                }
            }
        }
        return out;
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        BlockState mimicked = data.get(MIMICKED_STATE);
        if (mimicked != null && !mimicked.isAir()) {
            try {
                BakedModel src = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimicked);
                TextureAtlasSprite particle = src.getParticleIcon(ModelData.EMPTY);
                if (particle != null) {
                    return particle;
                }
            } catch (Exception ignored) {}
        }
        return super.getParticleIcon(data);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
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
}
