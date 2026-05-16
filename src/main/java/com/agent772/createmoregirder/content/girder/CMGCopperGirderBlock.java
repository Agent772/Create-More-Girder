package com.agent772.createmoregirder.content.girder;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
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
 * Shared base for every copper girder block of the OG-style
 * {@code *_metal_girder} family. Adds copper waxing, axe scraping and
 * random-tick oxidation on top of a self-contained shaft / wrench / placement
 * interaction flow.
 *
 * <p>It is intentionally weathering-map agnostic: concrete subclasses point the
 * {@link #getHoneycombTransition}, {@link #getAxeTransition} and
 * {@link #getNextWeatherBlock} hooks at the appropriate weathering map.
 */
public abstract class CMGCopperGirderBlock extends CMGGirderBlock implements WeatheringCopper {

    public CMGCopperGirderBlock(Properties properties) {
        super(properties);
    }

    /**
     * The encased-shaft block this girder turns into when a shaft is used on it.
     */
    protected abstract BlockEntry<? extends Block> getEncasedShaftBlock();

    /**
     * The id of the {@link PlacementHelpers} entry registered for this variant.
     */
    protected abstract int getPlacementHelperId();

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
    public void onRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.applyChangeOverTime(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return getNextWeatherBlock(state.getBlock()).isPresent();
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return getNextWeatherBlock(state.getBlock())
                .map(block -> copyStateTo(block, state));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player == null)
            return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);

        // Honeycomb waxing
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
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        // Axe scraping / unwaxing
        if (stack.getItem() instanceof AxeItem) {
            Optional<Block> scrapedBlock = getAxeTransition(state.getBlock());
            if (scrapedBlock.isPresent()) {
                if (!level.isClientSide) {
                    level.setBlock(pos, copyStateTo(scrapedBlock.get(), state), 3);
                    level.playSound(null, pos, getAxeSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        // Shaft encasing
        InteractionResult shaftResult = tryShaftEncasing(stack, state, level, pos, player, hand, hitResult,
                getEncasedShaftBlock());
        if (shaftResult != null)
            return shaftResult;

        // Wrench bracket toggle
        InteractionResult wrenchResult = tryGirderWrenchInteraction(stack, state, level, pos, player, hitResult);
        if (wrenchResult != null)
            return wrenchResult;

        // Placement helper
        IPlacementHelper helper = PlacementHelpers.get(getPlacementHelperId());
        if (helper.matchesItem(stack))
            return helper.getOffset(player, level, state, pos, hitResult)
                    .placeInWorld(level, (BlockItem) stack.getItem(), player, hand, hitResult);

        return InteractionResult.PASS;
    }

    private static BlockState copyStateTo(Block target, BlockState source) {
        return target.defaultBlockState()
                .setValue(WATERLOGGED, source.getValue(WATERLOGGED))
                .setValue(TOP, source.getValue(TOP))
                .setValue(BOTTOM, source.getValue(BOTTOM))
                .setValue(X, source.getValue(X))
                .setValue(Z, source.getValue(Z))
                .setValue(AXIS, source.getValue(AXIS));
    }
}
