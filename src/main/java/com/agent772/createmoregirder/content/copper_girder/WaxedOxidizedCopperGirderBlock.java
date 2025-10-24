package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.andesite_girder.AndesiteGirderWrenchBehaviour;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
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
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class WaxedOxidizedCopperGirderBlock extends CopperGirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new WaxedOxidizedCopperGirderPlacementHelper());

    public WaxedOxidizedCopperGirderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return WeatheringCopper.WeatherState.OXIDIZED; // Waxed oxidized copper stage
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        // Handle axe scraping (removes wax)
        if (stack.getItem() instanceof AxeItem) {
            Optional<Block> unwaxedBlock = WeatheringCopperGirders.getUnwaxed(state.getBlock());
            if (unwaxedBlock.isPresent()) {
                if (!level.isClientSide) {
                    BlockState unwaxedState = unwaxedBlock.get().defaultBlockState()
                            .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                            .setValue(TOP, state.getValue(TOP))
                            .setValue(BOTTOM, state.getValue(BOTTOM))
                            .setValue(X, state.getValue(X))
                            .setValue(Z, state.getValue(Z))
                            .setValue(AXIS, state.getValue(AXIS));
                    level.setBlock(pos, unwaxedState, 3);
                    level.playSound(null, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        if (AllBlocks.SHAFT.isIn(stack)) {
            KineticBlockEntity.switchToBlockState(level, pos, CMGBlocks.WAXED_OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT.getDefaultState()
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

        if (AllItems.WRENCH.isIn(stack) && !player.isShiftKeyDown()) {
            if (AndesiteGirderWrenchBehaviour.handleClick(level, pos, state, hitResult))
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            return ItemInteractionResult.FAIL;
        }

        IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
        if (helper.matchesItem(stack))
            return helper.getOffset(player, level, state, pos, hitResult)
                    .placeInWorld(level, (BlockItem) stack.getItem(), player, hand, hitResult);

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return false; // Waxed blocks don't weather
    }
}