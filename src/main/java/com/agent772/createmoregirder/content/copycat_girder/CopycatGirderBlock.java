package com.agent772.createmoregirder.content.copycat_girder;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.andesite_girder.AndesiteGirderWrenchBehaviour;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import com.simibubi.create.content.decoration.girder.GirderEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class CopycatGirderBlock extends GirderBlock implements IBE<CopycatGirderBlockEntity>, SpecialBlockItemRequirement {
    private static final int placementHelperId = PlacementHelpers.register(new CopycatGirderPlacementHelper());

    public CopycatGirderBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player == null || hand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);

        // Shaft insertion
        if (AllBlocks.SHAFT.isIn(stack)) {
            BlockState carriedMimic = null;
            int carriedRotation = 0;
            BlockEntity preBe = level.getBlockEntity(pos);
            if (preBe instanceof CopycatGirderBlockEntity copycatBe && copycatBe.hasMimickedState()) {
                carriedMimic = copycatBe.getMimickedState();
                carriedRotation = copycatBe.getFaceRotation();
            }

            KineticBlockEntity.switchToBlockState(level, pos, CMGBlocks.COPYCAT_GIRDER_ENCASED_SHAFT.getDefaultState()
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                    .setValue(TOP, state.getValue(TOP))
                    .setValue(BOTTOM, state.getValue(BOTTOM))
                    .setValue(GirderEncasedShaftBlock.HORIZONTAL_AXIS, (state.getValue(X) || hitResult.getDirection()
                            .getAxis() == Direction.Axis.Z) ? Direction.Axis.Z : Direction.Axis.X));

            if (carriedMimic != null) {
                BlockEntity postBe = level.getBlockEntity(pos);
                if (postBe instanceof CopycatGirderEncasedShaftBlockEntity encasedBe) {
                    encasedBe.adoptFrom(carriedMimic, carriedRotation);
                }
            }

            level.playSound(null, pos, SoundEvents.NETHERITE_BLOCK_HIT, SoundSource.BLOCKS, 0.5f, 1.25f);
            if (!level.isClientSide && !player.isCreative()) {
                stack.shrink(1);
                if (stack.isEmpty())
                    player.setItemInHand(hand, ItemStack.EMPTY);
            }

            return InteractionResult.SUCCESS;
        }

        // Wrench interaction: toggle bracket first if available, otherwise remove material
        if (AllItems.WRENCH.isIn(stack) && !player.isShiftKeyDown()) {
            if (AndesiteGirderWrenchBehaviour.handleClick(level, pos, state, hitResult))
                return InteractionResult.sidedSuccess(level.isClientSide);

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CopycatGirderBlockEntity copycatBe && copycatBe.hasMimickedState()) {
                if (!level.isClientSide) {
                    ItemStack dropStack = new ItemStack(copycatBe.getMimickedState().getBlock());
                    if (!player.isCreative()) {
                        if (!player.getInventory().add(dropStack)) {
                            Block.popResource(level, pos, dropStack);
                        }
                    }
                    copycatBe.clearMimickedState();
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            return InteractionResult.FAIL;
        }

        // Block application / face rotation cycling
        if (stack.getItem() instanceof BlockItem blockItem) {
            Block sourceBlock = blockItem.getBlock();
            BlockState sourceState = sourceBlock.defaultBlockState();

            IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
            if (helper.matchesItem(stack))
                return helper.getOffset(player, level, state, pos, hitResult)
                        .placeInWorld(level, (BlockItem) stack.getItem(), player, hand, hitResult);

            if (!isValidMaterial(sourceState))
                return InteractionResult.PASS;

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CopycatGirderBlockEntity copycatBe) {
                if (copycatBe.hasMimickedState()) {
                    if (sourceBlock == copycatBe.getMimickedState().getBlock()) {
                        if (!level.isClientSide) {
                            if (!copycatBe.cycleTexture()) {
                                return InteractionResult.PASS;
                            }
                            level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                    return InteractionResult.PASS;
                }

                if (!level.isClientSide) {
                    MimickedBlockEntity neighbor = findAdjacentMimic(level, pos, sourceBlock);
                    BlockState materialState = neighbor != null ? neighbor.getMimickedState() : sourceState;
                    int neighborRotation = neighbor != null ? neighbor.getFaceRotation() : 0;
                    copycatBe.applyMaterial(materialState, neighborRotation);
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                        if (stack.isEmpty())
                            player.setItemInHand(hand, ItemStack.EMPTY);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    private boolean isValidMaterial(BlockState state) {
        return state.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO) && !state.hasBlockEntity();
    }

    private MimickedBlockEntity findAdjacentMimic(Level level, BlockPos pos, Block material) {
        for (Direction dir : Direction.values()) {
            BlockEntity adjBe = level.getBlockEntity(pos.relative(dir));
            if (adjBe instanceof MimickedBlockEntity mimicked && mimicked.hasMimickedState()) {
                if (mimicked.getMimickedState().getBlock() == material) {
                    return mimicked;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CopycatGirderBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            return copycatBe.getMimickedState().getSignal(level, pos, direction);
        }
        return 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CopycatGirderBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            return copycatBe.getMimickedState().getDirectSignal(level, pos, direction);
        }
        return 0;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide || !(placer instanceof Player player))
            return;

        ItemStack offhand = player.getOffhandItem();
        if (offhand.isEmpty() || !(offhand.getItem() instanceof BlockItem blockItem))
            return;

        BlockState sourceState = blockItem.getBlock().defaultBlockState();
        if (!isValidMaterial(sourceState))
            return;

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CopycatGirderBlockEntity copycatBe && !copycatBe.hasMimickedState()) {
            MimickedBlockEntity neighbor = findAdjacentMimic(level, pos, blockItem.getBlock());
            BlockState materialState = neighbor != null ? neighbor.getMimickedState() : sourceState;
            int neighborRotation = neighbor != null ? neighbor.getFaceRotation() : 0;
            copycatBe.applyMaterial(materialState, neighborRotation);
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (!player.isCreative()) {
                offhand.shrink(1);
                if (offhand.isEmpty())
                    player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!level.isClientSide) {
            Player player = context.getPlayer();
            BlockEntity be = level.getBlockEntity(pos);
            if (player != null && be instanceof CopycatGirderBlockEntity copycatBe && copycatBe.hasMimickedState()) {
                ItemStack dropStack = new ItemStack(copycatBe.getMimickedState().getBlock());
                if (!player.isCreative()) {
                    if (!player.getInventory().add(dropStack)) {
                        Block.popResource(level, pos, dropStack);
                    }
                }
                copycatBe.clearMimickedState();
            }
        }
        return super.onSneakWrenched(state, context);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !(newState.getBlock() instanceof CopycatGirderEncasedShaftBlock)) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CopycatGirderBlockEntity copycatBe && copycatBe.hasMimickedState()) {
                Block.popResource(level, pos, new ItemStack(copycatBe.getMimickedState().getBlock()));
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CopycatGirderBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            return copycatBe.getMimickedState().getLightEmission();
        }
        return 0;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        ItemRequirement req = new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, new ItemStack(this));
        if (be instanceof CopycatGirderBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            req = req.union(new ItemRequirement(ItemRequirement.ItemUseType.CONSUME,
                    new ItemStack(copycatBe.getMimickedState().getBlock())));
        }
        return req;
    }

    @Override
    public Class<CopycatGirderBlockEntity> getBlockEntityClass() {
        return CopycatGirderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CopycatGirderBlockEntity> getBlockEntityType() {
        return CMGBlockEntityTypes.COPYCAT_GIRDER.get();
    }
}
