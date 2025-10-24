package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.CMGBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class WaxedOxidizedCopperGirderEncasedShaftBlock extends CopperGirderEncasedShaftBlock {

    public WaxedOxidizedCopperGirderEncasedShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return WeatheringCopper.WeatherState.OXIDIZED;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        // Handle axe scraping (removes wax, converts back to unwaxed encased shaft)
        if (stack.getItem() instanceof AxeItem) {
            Optional<Block> unwaxedBlock = WeatheringCopperGirders.getUnwaxedEncasedShaft(state.getBlock());
            if (unwaxedBlock.isPresent()) {
                if (!level.isClientSide) {
                    BlockState unwaxedState = unwaxedBlock.get().defaultBlockState()
                            .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                            .setValue(HORIZONTAL_AXIS, state.getValue(HORIZONTAL_AXIS));
                    level.setBlock(pos, unwaxedState, 3);
                    level.playSound(null, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return CMGBlocks.WAXED_OXIDIZED_COPPER_GIRDER.getDefaultState()
                .setValue(WATERLOGGED, originalState.getValue(WATERLOGGED))
                .setValue(CopperGirderBlock.X, originalState.getValue(HORIZONTAL_AXIS) == Direction.Axis.X)
                .setValue(CopperGirderBlock.Z, originalState.getValue(HORIZONTAL_AXIS) == Direction.Axis.Z);
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return CMGBlockEntityTypes.ENCASED_WAXED_OXIDIZED_COPPER_GIRDER.get();
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return false; // Waxed blocks don't weather
    }
}