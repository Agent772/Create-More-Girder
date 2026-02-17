package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.andesite_girder.AndesiteGirderBlock;
import com.agent772.createmoregirder.content.andesite_girder.AndesiteGirderWrenchBehaviour;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.girder.GirderEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;
import java.util.function.Supplier;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class CopperGirderBlock extends AndesiteGirderBlock implements WeatheringCopper {
    private static final int placementHelperId = PlacementHelpers.register(new CopperGirderPlacementHelper());

    public CopperGirderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return WeatheringCopper.WeatherState.UNAFFECTED;
    }
    
    /**
     * Returns the encased shaft block for this girder variant.
     * Override in subclasses to provide the correct variant-specific encased shaft.
     */
    protected BlockState getEncasedShaftBlock() {
        return CMGBlocks.COPPER_GIRDER_ENCASED_SHAFT.getDefaultState();
    }
    
    /**
     * Returns the placement helper ID for this girder variant.
     * Override in subclasses to provide the correct variant-specific placement helper.
     */
    protected int getPlacementHelperId() {
        return placementHelperId;
    }
    
    /**
     * Checks if this block is waxed (should handle axe unwaxing instead of scraping).
     */
    protected boolean isWaxed() {
        return false;
    }
    
    /**
     * Copies all girder-specific properties from one state to another.
     */
    protected BlockState copyGirderProperties(BlockState from, BlockState to) {
        return to.setValue(WATERLOGGED, from.getValue(WATERLOGGED))
                .setValue(TOP, from.getValue(TOP))
                .setValue(BOTTOM, from.getValue(BOTTOM))
                .setValue(X, from.getValue(X))
                .setValue(Z, from.getValue(Z))
                .setValue(AXIS, from.getValue(AXIS));
    }

    @Override
    public void onRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.applyChangeOverTime(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopperGirders.getNext(state.getBlock()).isPresent();
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return WeatheringCopperGirders.getNext(state.getBlock())
                .map(block -> copyGirderProperties(state, block.defaultBlockState()));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        
        // Handle honeycomb waxing (for non-waxed blocks)
        if (!isWaxed() && stack.is(Items.HONEYCOMB)) {
            InteractionResult result = handleWaxing(state, level, pos, player, stack);
            if (result != InteractionResult.PASS) return result;
        }

        // Handle axe interactions (scraping for non-waxed, unwaxing for waxed)
        if (stack.getItem() instanceof AxeItem) {
            InteractionResult result = isWaxed() ? handleUnwaxing(state, level, pos) : handleScraping(state, level, pos);
            if (result != InteractionResult.PASS) return result;
        }

        // Handle shaft encasing
        if (AllBlocks.SHAFT.isIn(stack)) {
            return handleShaftEncasing(state, level, pos, player, hand, stack, hitResult);
        }

        // Handle wrench behavior
        if (AllItems.WRENCH.isIn(stack) && !player.isShiftKeyDown()) {
            if (AndesiteGirderWrenchBehaviour.handleClick(level, pos, state, hitResult))
                return InteractionResult.sidedSuccess(level.isClientSide);
            return InteractionResult.FAIL;
        }

        // Handle placement helper
        IPlacementHelper helper = PlacementHelpers.get(getPlacementHelperId());
        if (helper.matchesItem(stack))
            return helper.getOffset(player, level, state, pos, hitResult)
                    .placeInWorld(level, (BlockItem) stack.getItem(), player, hand, hitResult);

        return InteractionResult.PASS;
    }
    
    /**
     * Handles honeycomb waxing interaction.
     */
    protected InteractionResult handleWaxing(BlockState state, Level level, BlockPos pos, Player player, ItemStack stack) {
        Optional<Block> waxedBlock = WeatheringCopperGirders.getWaxed(state.getBlock());
        if (waxedBlock.isPresent()) {
            if (!level.isClientSide) {
                level.setBlock(pos, copyGirderProperties(state, waxedBlock.get().defaultBlockState()), 3);
                level.playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    /**
     * Handles axe scraping interaction (for weathered variants).
     */
    protected InteractionResult handleScraping(BlockState state, Level level, BlockPos pos) {
        Optional<Block> scrapedBlock = WeatheringCopperGirders.getPrevious(state.getBlock());
        if (scrapedBlock.isPresent()) {
            if (!level.isClientSide) {
                level.setBlock(pos, copyGirderProperties(state, scrapedBlock.get().defaultBlockState()), 3);
                level.playSound(null, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    /**
     * Handles axe unwaxing interaction (for waxed variants).
     */
    protected InteractionResult handleUnwaxing(BlockState state, Level level, BlockPos pos) {
        Optional<Block> unwaxedBlock = WeatheringCopperGirders.getUnwaxed(state.getBlock());
        if (unwaxedBlock.isPresent()) {
            if (!level.isClientSide) {
                level.setBlock(pos, copyGirderProperties(state, unwaxedBlock.get().defaultBlockState()), 3);
                level.playSound(null, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    /**
     * Handles shaft encasing interaction.
     */
    protected InteractionResult handleShaftEncasing(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, BlockHitResult hitResult) {
        KineticBlockEntity.switchToBlockState(level, pos, getEncasedShaftBlock()
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

        return InteractionResult.SUCCESS;
    }
}