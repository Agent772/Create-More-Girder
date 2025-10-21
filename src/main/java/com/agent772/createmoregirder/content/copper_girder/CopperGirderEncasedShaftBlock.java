package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.CMGBlocks;
import com.jesz.createdieselgenerators.content.andesite_girder.AndesiteGirderEncasedShaftBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import com.simibubi.create.content.decoration.girder.GirderEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class CopperGirderEncasedShaftBlock extends AndesiteGirderEncasedShaftBlock implements WeatheringCopper {
    public CopperGirderEncasedShaftBlock(Properties properties) {
        super(properties);
    }

    @Override
    public WeatherState getAge() {
        return WeatherState.UNAFFECTED;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        // Handle honeycomb waxing
        if (stack.is(Items.HONEYCOMB)) {
            Optional<Block> waxedBlock = WeatheringCopperGirders.getWaxedEncasedShaft(state.getBlock());
            if (waxedBlock.isPresent()) {
                if (!level.isClientSide) {
                    BlockState waxedState = waxedBlock.get().defaultBlockState()
                            .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                            .setValue(TOP, state.getValue(TOP))
                            .setValue(BOTTOM, state.getValue(BOTTOM))
                            .setValue(HORIZONTAL_AXIS, state.getValue(HORIZONTAL_AXIS));
                    level.setBlock(pos, waxedState, 3);
                    level.playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0f, 1.0f);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        // Handle axe scraping
        if (stack.getItem() instanceof AxeItem) {
            Optional<Block> scrapedBlock = WeatheringCopperGirders.getPreviousEncasedShaft(state.getBlock());
            if (scrapedBlock.isPresent()) {
                if (!level.isClientSide) {
                    BlockState scrapedState = scrapedBlock.get().defaultBlockState()
                            .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                            .setValue(TOP, state.getValue(TOP))
                            .setValue(BOTTOM, state.getValue(BOTTOM))
                            .setValue(HORIZONTAL_AXIS, state.getValue(HORIZONTAL_AXIS));
                    level.setBlock(pos, scrapedState, 3);
                    level.playSound(null, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopperGirders.getNextEncasedShaft(state.getBlock()).isPresent();
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return WeatheringCopperGirders.getNextEncasedShaft(state.getBlock())
                .map(block -> block.defaultBlockState()
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                        .setValue(TOP, state.getValue(TOP))
                        .setValue(BOTTOM, state.getValue(BOTTOM))
                        .setValue(HORIZONTAL_AXIS, state.getValue(HORIZONTAL_AXIS)));
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return CMGBlocks.COPPER_GIRDER.getDefaultState()
                .setValue(WATERLOGGED, originalState.getValue(WATERLOGGED))
                .setValue(GirderBlock.X, originalState.getValue(HORIZONTAL_AXIS) == Direction.Axis.Z)
                .setValue(GirderBlock.Z, originalState.getValue(HORIZONTAL_AXIS) == Direction.Axis.X)
                .setValue(GirderBlock.AXIS, originalState.getValue(HORIZONTAL_AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X)
                .setValue(GirderBlock.BOTTOM, originalState.getValue(BOTTOM))
                .setValue(GirderBlock.TOP, originalState.getValue(TOP));
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        return ItemRequirement.of(AllBlocks.SHAFT.getDefaultState(), be)
                .union(ItemRequirement.of(CMGBlocks.COPPER_GIRDER.getDefaultState(), be));
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return CMGBlockEntityTypes.ENCASED_COPPER_GIRDER.get();
    }
}