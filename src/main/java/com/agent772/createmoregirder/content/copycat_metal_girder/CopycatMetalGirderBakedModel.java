package com.agent772.createmoregirder.content.copycat_metal_girder;

import com.agent772.createmoregirder.CMGPartialModels;
import com.agent772.createmoregirder.content.copycat_girder.CopycatGirderBakedModel;
import com.agent772.createmoregirder.content.girder.MetalGirderConnectedModel.PoleCtState;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Baked model for {@code copycat_metal_girder}. Combines two behaviours:
 *
 * <ul>
 *   <li>When <b>no</b> mimic block is applied, it reproduces the connected-texture
 *       pole behaviour of the rest of the metal girder family
 *       ({@link MetalGirderConnectedModel}): vertical pole segments swap in
 *       the {@code block_pole_top/middle/bottom} models so adjacent poles join up.</li>
 *   <li>When a mimic block <b>is</b> applied, it falls through to
 *       {@link CopycatGirderBakedModel}, which remaps the regular {@code block_pole}
 *       model onto the mimicked texture. The connected-texture pole models are
 *       deliberately skipped in this case: their UVs only cover a small window of
 *       the {@code _pole_side_connected} atlas, so remapping them would stretch the
 *       mimicked texture.</li>
 * </ul>
 */
public class CopycatMetalGirderBakedModel extends CopycatGirderBakedModel {

    private static final ModelProperty<PoleCtState> POLE_CT_PROPERTY = new ModelProperty<>();

    public CopycatMetalGirderBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos,
                                           @NotNull BlockState state, @NotNull ModelData blockEntityData) {
        ModelData data = super.getModelData(level, pos, state, blockEntityData);

        // Connected-texture poles only apply to vertical pole segments.
        if (state.getValue(GirderBlock.X) || state.getValue(GirderBlock.Z)) {
            return data;
        }
        // Skip the connected pole models while mimicking - their partial UV
        // windows would stretch the mimicked texture. The mimic path keeps
        // using the full-size block_pole model via CopycatGirderBakedModel.
        if (data.get(MIMICKED_STATE) != null) {
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
                                           ModelData data, RenderType renderType) {
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
}
