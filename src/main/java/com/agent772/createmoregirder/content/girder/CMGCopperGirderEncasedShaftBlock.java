package com.agent772.createmoregirder.content.girder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * Shared base for the encased-shaft variant of every copper girder, both the
 * plain {@code copper_girder} family and the OG-style
 * {@code copper_metal_girder} family. Adds copper waxing, axe scraping and
 * random-tick oxidation on top of the shared wrench / rotation behavior from
 * {@link CMGGirderEncasedShaftBlock}.
 *
 * <p>Like {@link CMGCopperGirderBlock} it is weathering-map agnostic: concrete
 * subclasses point the {@link #getHoneycombTransition}, {@link #getAxeTransition}
 * and {@link #getNextWeatherBlock} hooks at the appropriate map.
 */
public abstract class CMGCopperGirderEncasedShaftBlock extends CMGGirderEncasedShaftBlock implements WeatheringCopper {

    public CMGCopperGirderEncasedShaftBlock(Properties properties) {
        super(properties);
    }

    /**
     * The waxed counterpart of {@code self}, or empty if it cannot be waxed.
     */
    protected abstract Optional<Block> getHoneycombTransition(Block self);

    /**
     * The block {@code self} becomes when scraped with an axe — the previous
     * weather stage for unwaxed copper, or the unwaxed block for waxed copper.
     */
    protected abstract Optional<Block> getAxeTransition(Block self);

    /**
     * The next weather stage of {@code self}, or empty if it does not weather.
     */
    protected abstract Optional<Block> getNextWeatherBlock(Block self);

    /**
     * The sound played when {@link #getAxeTransition} is applied. Defaults to
     * the scrape sound; waxed variants override it with the wax-off sound.
     */
    protected SoundEvent getAxeSound() {
        return SoundEvents.AXE_SCRAPE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return getNextWeatherBlock(state.getBlock()).isPresent();
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return getNextWeatherBlock(state.getBlock())
                .map(block -> copyStateTo(block, state));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (stack.is(Items.HONEYCOMB)) {
            Optional<Block> waxedBlock = getHoneycombTransition(state.getBlock());
            if (waxedBlock.isPresent()) {
                if (!level.isClientSide) {
                    level.setBlock(pos, copyStateTo(waxedBlock.get(), state), 3);
                    level.playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0f, 1.0f);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        if (stack.getItem() instanceof AxeItem) {
            Optional<Block> scrapedBlock = getAxeTransition(state.getBlock());
            if (scrapedBlock.isPresent()) {
                if (!level.isClientSide) {
                    level.setBlock(pos, copyStateTo(scrapedBlock.get(), state), 3);
                    level.playSound(null, pos, getAxeSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    private static BlockState copyStateTo(Block target, BlockState source) {
        return target.defaultBlockState()
                .setValue(WATERLOGGED, source.getValue(WATERLOGGED))
                .setValue(TOP, source.getValue(TOP))
                .setValue(BOTTOM, source.getValue(BOTTOM))
                .setValue(HORIZONTAL_AXIS, source.getValue(HORIZONTAL_AXIS));
    }
}
