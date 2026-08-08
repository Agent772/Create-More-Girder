package com.agent772.createmoregirder.content.copycat_girder;

import com.agent772.createmoregirder.CMGPartialModels;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
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
import java.util.EnumSet;
import java.util.List;

public class CopycatGirderBakedModel extends BakedModelWrapper<BakedModel> {

    public static final ModelProperty<BlockState> MIMICKED_STATE = new ModelProperty<>();
    public static final ModelProperty<Integer> FACE_ROTATION = new ModelProperty<>();
    public static final ModelProperty<EnumSet<Direction>> CONNECTED_DIRECTIONS = new ModelProperty<>();

    public static final int ORIENTATION_COUNT = MimicFaceSampler.ORIENTATION_COUNT;

    public CopycatGirderBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public @NotNull ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData blockEntityData) {
        ModelData.Builder builder = ModelData.builder();

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof MimickedBlockEntity mimicked && mimicked.hasMimickedState()) {
            builder.with(MIMICKED_STATE, mimicked.getMimickedState())
                   .with(FACE_ROTATION, mimicked.getFaceRotation());
        }

        EnumSet<Direction> connected = EnumSet.noneOf(Direction.class);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (GirderBlock.isConnected(level, pos, state, direction)) {
                connected.add(direction);
            }
        }
        if (!connected.isEmpty()) {
            builder.with(CONNECTED_DIRECTIONS, connected);
        }

        return builder.build();
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
        EnumSet<Direction> connected = data.get(CONNECTED_DIRECTIONS);

        ChunkRenderTypeSet mimicTypes = hasMimic ? mimicRenderTypes(mimicked, rand) : null;
        if (mimicTypes == null) {
            hasMimic = false;
        }

        boolean rendersOnBase;
        boolean rendersOnMimic;
        if (renderType == null || state == null) {
            rendersOnBase = true;
            rendersOnMimic = hasMimic;
        } else {
            ChunkRenderTypeSet baseTypes = super.getRenderTypes(state, rand, data);
            rendersOnBase = baseTypes.contains(renderType);
            rendersOnMimic = hasMimic && mimicTypes.contains(renderType);
        }

        if (!rendersOnBase && !rendersOnMimic) {
            return Collections.emptyList();
        }

        List<BakedQuad> result = new ArrayList<>();

        // Per-face texture layers, shared between the bracket and pole loops on the
        // mimic path. Sampled per render type so each pass (solid core / translucent
        // glow shell) contributes only its own layer; the particle fallback is only
        // emitted on the primary mimic render type to avoid duplicate draws.
        List<MimicFaceSampler.Layer>[] faceLayers = null;
        if (hasMimic && rendersOnMimic) {
            Integer rot = data.get(FACE_ROTATION);
            int orientation = rot == null ? 0 : Math.floorMod(rot, ORIENTATION_COUNT);
            RenderType sampleType;
            boolean includeFallback;
            if (renderType == null || state == null) {
                sampleType = null;
                includeFallback = true;
            } else {
                sampleType = renderType;
                includeFallback = renderType.equals(MimicFaceSampler.primaryRenderType(mimicTypes));
            }
            faceLayers = MimicFaceSampler.sampleLayers(mimicked, orientation, sampleType, includeFallback);
        }

        if (side == null && state != null && connected != null && !connected.isEmpty()) {
            for (Direction direction : connected) {
                PartialModel partial = CMGPartialModels.getBracketModel(state.getBlock(), direction);
                if (partial == null) continue;
                if (!hasMimic) {
                    if (rendersOnBase) {
                        result.addAll(partial.get().getQuads(state, null, rand, data, renderType));
                    }
                    continue;
                }
                if (!rendersOnMimic || faceLayers == null) {
                    continue;
                }
                // Mimic path: fetch with renderType=null so the bracket JSON's
                // declared layer doesn't strip the quads before we route them
                // onto the mimic's render layer.
                List<BakedQuad> bracketQuads = partial.get().getQuads(state, null, rand, data, null);
                for (BakedQuad quad : bracketQuads) {
                    MimicFaceSampler.emitLayers(result, quad, faceLayers);
                }
            }
        }

        // Pole quads: always fetched with renderType=null so we can route them
        // (remapped to the mimic atlas) onto the mimic's render layer instead of
        // the JSON-declared base layer. Without this, stained glass mimics get
        // forced through cutout/cutoutMipped, rendering them solid or fully
        // transparent.
        List<BakedQuad> poleQuads = getBaseQuads(state, side, rand, data, null);

        if (!hasMimic) {
            if (rendersOnBase) {
                if (renderType == null) {
                    result.addAll(poleQuads);
                } else {
                    // Re-run with the actual renderType so the underlying model
                    // can filter to the requested layer.
                    result.addAll(getBaseQuads(state, side, rand, data, renderType));
                }
            }
            return result;
        }

        for (BakedQuad quad : poleQuads) {
            String spriteName = quad.getSprite().contents().name().getPath();
            if (spriteName.endsWith("bearing_hole_fixed")) {
                if (rendersOnBase) {
                    result.add(quad);
                }
                continue;
            }
            if (!rendersOnMimic || faceLayers == null) {
                continue;
            }
            MimicFaceSampler.emitLayers(result, quad, faceLayers);
        }
        return result;
    }

    /**
     * Returns the base (non-bracket, non-mimic-remapped) pole quads for this girder. Subclasses
     * override to substitute variant-specific pole geometry while still letting {@link #getQuads}
     * append the shared bracket quads and apply mimic UV remapping.
     */
    protected List<BakedQuad> getBaseQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand,
                                           ModelData data, RenderType renderType) {
        return super.getQuads(state, side, rand, data, renderType);
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
            } catch (Exception ignored) {
            }
        }
        return super.getParticleIcon(data);
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
}
