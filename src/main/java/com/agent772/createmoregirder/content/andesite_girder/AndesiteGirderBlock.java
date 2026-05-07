package com.agent772.createmoregirder.content.andesite_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.girder.CMGGirderWrenchBehaviour;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import com.simibubi.create.content.decoration.girder.GirderEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class AndesiteGirderBlock extends GirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new AndesiteGirderPlacementHelper());

    public AndesiteGirderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null)
            return null;
        return withIndependentConnectors(state, context.getLevel(), context.getClickedPos());
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

        boolean nowX = result.getValue(X);
        boolean nowZ = result.getValue(Z);

        if (wasHorizontal && !nowX && !nowZ) {
            result = result.setValue(X, prevX).setValue(Z, prevZ);
        }

        boolean isHorizontal = result.getValue(X) ^ result.getValue(Z);

        // super may clear the opposite-side connector via getBlockSupportShape; restore it for non-triggering directions
        if (isHorizontal) {
            if (neighbourState.isAir()) {
                result = result.setValue(TOP, prevTop).setValue(BOTTOM, prevBottom);
            } else if (!wasHorizontal) {
                result = withIndependentConnectors(result, world, pos);
            } else if (direction == Direction.UP) {
                result = result.setValue(BOTTOM, prevBottom);
                result = updateSingleConnector(result, world, pos, Direction.UP);
            } else if (direction == Direction.DOWN) {
                result = result.setValue(TOP, prevTop);
                result = updateSingleConnector(result, world, pos, Direction.DOWN);
            } else {
                result = result.setValue(TOP, prevTop).setValue(BOTTOM, prevBottom);
            }
        }

        return result;
    }

    private static BlockState withIndependentConnectors(BlockState state, LevelAccessor level, BlockPos pos) {
        state = updateSingleConnector(state, level, pos, Direction.UP);
        return updateSingleConnector(state, level, pos, Direction.DOWN);
    }

    private static BlockState updateSingleConnector(BlockState state, LevelAccessor level, BlockPos pos,
                                                    Direction direction) {
        boolean hasX = state.getValue(X);
        boolean hasZ = state.getValue(Z);
        if (hasX == hasZ)
            return state;

        Direction.Axis horizontalAxis = hasX ? Direction.Axis.X : Direction.Axis.Z;
        boolean isUp = direction == Direction.UP;
        BooleanProperty property = isUp ? TOP : BOTTOM;
        BlockPos neighborPos = isUp ? pos.above() : pos.below();

        BlockState probe = state.setValue(AXIS, horizontalAxis).setValue(property, false);
        probe = GirderBlock.updateVerticalProperty(
                level, pos, probe, property, level.getBlockState(neighborPos), direction);

        return state.setValue(AXIS, horizontalAxis).setValue(property, probe.getValue(property));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (AllBlocks.SHAFT.isIn(stack)) {
            KineticBlockEntity.switchToBlockState(level, pos, CMGBlocks.ANDESITE_GIRDER_ENCASED_SHAFT.getDefaultState()
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                    .setValue(TOP, state.getValue(TOP))
                    .setValue(BOTTOM, state.getValue(BOTTOM))
                    .setValue(GirderEncasedShaftBlock.HORIZONTAL_AXIS, state.getValue(X) || hitResult.getDirection()
                            .getAxis() == Direction.Axis.Z ? Direction.Axis.Z : Direction.Axis.X));

            level.playSound(null, pos, SoundEvents.NETHERITE_BLOCK_HIT, SoundSource.BLOCKS, 0.5f, 1.25f);
            if (!level.isClientSide && !player.isCreative()) {
                stack.shrink(1);
                if (stack.isEmpty())
                    player.setItemInHand(hand, ItemStack.EMPTY);
            }

            return ItemInteractionResult.SUCCESS;
        }

        ItemInteractionResult wrenchResult = tryGirderWrenchInteraction(stack, state, level, pos, player, hitResult);
        if (wrenchResult != null)
            return wrenchResult;

        IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
        if (helper.matchesItem(stack))
            return helper.getOffset(player, level, state, pos, hitResult)
                    .placeInWorld(level, (BlockItem) stack.getItem(), player, hand, hitResult);

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    protected static ItemInteractionResult tryGirderWrenchInteraction(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!AllItems.WRENCH.isIn(stack) || player.isShiftKeyDown())
            return null;
        if (CMGGirderWrenchBehaviour.handleClick(level, pos, state, hitResult))
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        return ItemInteractionResult.FAIL;
    }
}
