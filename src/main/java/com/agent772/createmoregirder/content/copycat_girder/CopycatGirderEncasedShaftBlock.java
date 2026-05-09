package com.agent772.createmoregirder.content.copycat_girder;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.andesite_girder.AndesiteGirderWrenchBehaviour;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import com.simibubi.create.content.decoration.girder.GirderEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class CopycatGirderEncasedShaftBlock extends GirderEncasedShaftBlock {
    private static final int placementHelperId = PlacementHelpers.register(new CopycatGirderPlacementHelper());

    public CopycatGirderEncasedShaftBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        boolean hasVerticalConnection = originalState.getValue(TOP) || originalState.getValue(BOTTOM);
        if (hasVerticalConnection) {
            return CMGBlocks.COPYCAT_GIRDER.get().defaultBlockState()
                    .setValue(WATERLOGGED, originalState.getValue(WATERLOGGED))
                    .setValue(GirderBlock.X, false)
                    .setValue(GirderBlock.Z, false)
                    .setValue(GirderBlock.AXIS, Direction.Axis.Y)
                    .setValue(GirderBlock.BOTTOM, originalState.getValue(BOTTOM))
                    .setValue(GirderBlock.TOP, originalState.getValue(TOP));
        } else {
            return CMGBlocks.COPYCAT_GIRDER.get().defaultBlockState()
                    .setValue(WATERLOGGED, originalState.getValue(WATERLOGGED))
                    .setValue(GirderBlock.X, originalState.getValue(HORIZONTAL_AXIS) == Direction.Axis.Z)
                    .setValue(GirderBlock.Z, originalState.getValue(HORIZONTAL_AXIS) == Direction.Axis.X)
                    .setValue(GirderBlock.AXIS, originalState.getValue(HORIZONTAL_AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X)
                    .setValue(GirderBlock.BOTTOM, false)
                    .setValue(GirderBlock.TOP, false);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player == null || hand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);

        if (AllItems.WRENCH.isIn(stack) && !player.isShiftKeyDown()) {
            if (AndesiteGirderWrenchBehaviour.handleClick(level, pos, state, hitResult))
                return InteractionResult.sidedSuccess(level.isClientSide);

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CopycatGirderEncasedShaftBlockEntity encasedBe && encasedBe.hasMimickedState()) {
                if (!level.isClientSide) {
                    ItemStack dropStack = new ItemStack(encasedBe.getMimickedState().getBlock());
                    if (!player.isCreative()) {
                        if (!player.getInventory().add(dropStack)) {
                            Block.popResource(level, pos, dropStack);
                        }
                    }
                    encasedBe.clearMimickedState();
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            return InteractionResult.PASS;
        }

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
            if (be instanceof CopycatGirderEncasedShaftBlockEntity encasedBe) {
                if (encasedBe.hasMimickedState()) {
                    if (sourceBlock == encasedBe.getMimickedState().getBlock()) {
                        if (!level.isClientSide) {
                            if (!encasedBe.cycleTexture()) {
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
                    encasedBe.applyMaterial(materialState, neighborRotation);
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
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState carriedMimic = null;
        int carriedRotation = 0;
        BlockEntity preBe = level.getBlockEntity(pos);
        if (preBe instanceof CopycatGirderEncasedShaftBlockEntity encasedBe && encasedBe.hasMimickedState()) {
            carriedMimic = encasedBe.getMimickedState();
            carriedRotation = encasedBe.getFaceRotation();
        }

        InteractionResult result = super.onWrenched(state, context);

        if (carriedMimic != null && result != InteractionResult.FAIL) {
            BlockEntity postBe = level.getBlockEntity(pos);
            if (postBe instanceof CopycatGirderBlockEntity girderBe) {
                girderBe.adoptFrom(carriedMimic, carriedRotation);
            }
        }
        return result;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!level.isClientSide) {
            Player player = context.getPlayer();
            BlockEntity be = level.getBlockEntity(pos);
            if (player != null && be instanceof CopycatGirderEncasedShaftBlockEntity encasedBe && encasedBe.hasMimickedState()) {
                ItemStack dropStack = new ItemStack(encasedBe.getMimickedState().getBlock());
                if (!player.isCreative()) {
                    if (!player.getInventory().add(dropStack)) {
                        Block.popResource(level, pos, dropStack);
                    }
                }
                encasedBe.clearMimickedState();
            }
        }
        return super.onSneakWrenched(state, context);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CopycatGirderEncasedShaftBlockEntity encasedBe && encasedBe.hasMimickedState()) {
            return encasedBe.getMimickedState().getSignal(level, pos, direction);
        }
        return 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CopycatGirderEncasedShaftBlockEntity encasedBe && encasedBe.hasMimickedState()) {
            return encasedBe.getMimickedState().getDirectSignal(level, pos, direction);
        }
        return 0;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CopycatGirderEncasedShaftBlockEntity encasedBe && encasedBe.hasMimickedState()) {
            return encasedBe.getMimickedState().getLightEmission();
        }
        return 0;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        ItemRequirement req = new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, new ItemStack(AllBlocks.SHAFT.get()))
                .union(new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, new ItemStack(CMGBlocks.COPYCAT_GIRDER.get())));
        if (be instanceof CopycatGirderEncasedShaftBlockEntity encasedBe && encasedBe.hasMimickedState()) {
            req = req.union(new ItemRequirement(ItemRequirement.ItemUseType.CONSUME,
                    new ItemStack(encasedBe.getMimickedState().getBlock())));
        }
        return req;
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return CMGBlockEntityTypes.ENCASED_COPYCAT_GIRDER.get();
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !(newState.getBlock() instanceof CopycatGirderBlock)) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CopycatGirderEncasedShaftBlockEntity copycatBe && copycatBe.hasMimickedState()) {
                Block.popResource(level, pos, new ItemStack(copycatBe.getMimickedState().getBlock()));
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
