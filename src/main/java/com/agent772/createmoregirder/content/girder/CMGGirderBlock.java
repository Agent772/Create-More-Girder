package com.agent772.createmoregirder.content.girder;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Shared base class for all CMG girder blocks.
 *
 * <p>Overrides {@link GirderBlock#updateShape} so that TOP and BOTTOM bracket
 * connectors on horizontal girders are <em>sticky</em>: once enabled they are
 * never cleared by neighbor updates. A connector is enabled when a non-air
 * block appears above (TOP) or below (BOTTOM).
 *
 * <p>This prevents the neighbor cascade triggered by {@code TrackPaver} from
 * undoing the {@code TOP=true} set by the paving mixin, and also ensures that
 * placing a block above a girder always shows the bracket cap.
 */
public class CMGGirderBlock extends GirderBlock {

    public CMGGirderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null)
            return null;

        if (state.getValue(X) ^ state.getValue(Z)) {
            BlockPos pos = context.getClickedPos();
            LevelAccessor level = context.getLevel();
            if (!level.getBlockState(pos.above()).isAir())
                state = state.setValue(TOP, true);
            if (!level.getBlockState(pos.below()).isAir())
                state = state.setValue(BOTTOM, true);
        }
        return state;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState,
                                  LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        boolean prevX = state.getValue(X);
        boolean prevZ = state.getValue(Z);
        boolean wasHorizontal = prevX ^ prevZ;
        boolean prevTop = state.getValue(TOP);
        boolean prevBottom = state.getValue(BOTTOM);

        BlockState result = super.updateShape(state, direction, neighbourState, world, pos, neighbourPos);

        // Preserve horizontal orientation if super cleared it
        if (wasHorizontal && !result.getValue(X) && !result.getValue(Z)) {
            result = result.setValue(X, prevX).setValue(Z, prevZ);
        }

        boolean isHorizontal = result.getValue(X) ^ result.getValue(Z);

        if (isHorizontal) {
            // Restore previous TOP/BOTTOM — never clear via neighbor updates
            result = result.setValue(TOP, prevTop).setValue(BOTTOM, prevBottom);

            // Enable connector when a non-air block appears above or below
            if (direction == Direction.UP && !neighbourState.isAir())
                result = result.setValue(TOP, true);
            else if (direction == Direction.DOWN && !neighbourState.isAir())
                result = result.setValue(BOTTOM, true);
        }

        return result;
    }

    protected static ItemInteractionResult tryGirderWrenchInteraction(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!AllItems.WRENCH.isIn(stack) || player.isShiftKeyDown())
            return null;
        if (CMGGirderWrenchBehaviour.handleClick(level, pos, state, hitResult))
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        return ItemInteractionResult.FAIL;
    }
}
