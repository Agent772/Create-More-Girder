package com.agent772.createmoregirder.content.girder;

import com.agent772.createmoregirder.CMGPartialModels;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Connected-texture model wrapper for OG-style metal girder poles
 * (andesite_metal_girder, brass_metal_girder, ...). Swaps in one of three
 * alternate pole models (top / middle / bottom of stack) sampled from the
 * variant's {@code _side_connected} atlas whenever a vertical neighbor is
 * also the same metal girder block.
 *
 * <p>The variant-specific partial models are resolved at render time from
 * {@link CMGPartialModels#getMetalGirderConnectedPole(Block, String)},
 * so a single model class covers every metal girder added by CMG.
 */
public class MetalGirderConnectedModel extends ConnectedGirderModel {

    private static final ModelProperty<PoleCtState> POLE_CT_PROPERTY = new ModelProperty<>();

    public MetalGirderConnectedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos,
                                           @NotNull BlockState state, @NotNull ModelData modelData) {
        ModelData data = super.getModelData(level, pos, state, modelData);
        if (state.getValue(GirderBlock.X) || state.getValue(GirderBlock.Z)) {
            return data;
        }
        boolean above = isSamePole(level.getBlockState(pos.above()), state);
        boolean below = isSamePole(level.getBlockState(pos.below()), state);
        if (!above && !below) {
            return data;
        }
        PoleCtState ctState;
        if (above && below) {
            ctState = PoleCtState.MIDDLE;
        } else if (above) {
            ctState = PoleCtState.BOTTOM;
        } else {
            ctState = PoleCtState.TOP;
        }
        return data.derive().with(POLE_CT_PROPERTY, ctState).build();
    }

    @Override
    protected List<BakedQuad> getBaseQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand,
                                           ModelData data, @Nullable RenderType renderType) {
        PoleCtState ctState = data.get(POLE_CT_PROPERTY);
        if (ctState != null && state != null) {
            PartialModel partial = CMGPartialModels.getMetalGirderConnectedPole(state.getBlock(), ctState.modelKey());
            if (partial != null) {
                return partial.get().getQuads(state, side, rand, data, renderType);
            }
        }
        return super.getBaseQuads(state, side, rand, data, renderType);
    }

    private static boolean isSamePole(BlockState neighbor, BlockState self) {
        if (neighbor.getBlock() != self.getBlock()) return false;
        return !neighbor.getValue(GirderBlock.X) && !neighbor.getValue(GirderBlock.Z);
    }

    public enum PoleCtState {
        TOP("top"),
        MIDDLE("middle"),
        BOTTOM("bottom");

        private final String key;

        PoleCtState(String key) {
            this.key = key;
        }

        public String modelKey() {
            return key;
        }
    }
}
