package com.agent772.createmoregirder.content.copycat_strut;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.content.strut.GirderStrutBlock;
import com.agent772.createmoregirder.content.strut.GirderStrutBlockEntity;
import com.agent772.createmoregirder.content.strut.StrutModelType;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
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

import java.util.List;

public class CopycatGirderStrutBlock extends GirderStrutBlock {

    public CopycatGirderStrutBlock(Properties properties) {
        super(properties, StrutModelType.COPYCAT);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player == null || hand != InteractionHand.MAIN_HAND)
            return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof CopycatGirderStrutBlockItem)
            return InteractionResult.PASS;

        if (stack.getItem() instanceof BlockItem blockItem) {
            Block sourceBlock = blockItem.getBlock();
            BlockState sourceState = sourceBlock.defaultBlockState();

            if (!isValidMaterial(sourceState))
                return InteractionResult.PASS;

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
                if (sourceBlock == copycatBe.getMimickedState().getBlock()) {
                    if (!level.isClientSide) {
                        if (!copycatBe.cycleTexture()) {
                            return InteractionResult.PASS;
                        }
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
                        propagateCycleToConnections(level, pos, copycatBe);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
                return InteractionResult.PASS;
            }
            return InteractionResult.PASS;
        }

        return InteractionResult.PASS;
    }

    private void propagateCycleToConnections(Level level, BlockPos pos, CopycatGirderStrutBlockEntity sourceBe) {
        for (BlockPos relOffset : sourceBe.getConnectionsCopy()) {
            BlockPos otherPos = relOffset.offset(pos);
            BlockEntity otherBe = level.getBlockEntity(otherPos);
            if (otherBe instanceof CopycatGirderStrutBlockEntity otherCopycat) {
                otherCopycat.applyMaterial(sourceBe.getMimickedState(), sourceBe.getFaceRotation());
            }
        }
    }

    public static boolean isValidMaterial(BlockState state) {
        return state.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO) && !state.hasBlockEntity();
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof GirderStrutBlockEntity strutBe) {
                // Determine the texture material and compute cost-based texture drop count
                BlockState textureState = null;
                if (be instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
                    textureState = copycatBe.getMimickedState();
                }
                // Also check connected anchors for texture if this one doesn't have it
                if (textureState == null) {
                    for (BlockPos relOffset : strutBe.getConnectionsCopy()) {
                        BlockPos otherPos = relOffset.offset(pos);
                        BlockEntity otherBe = level.getBlockEntity(otherPos);
                        if (otherBe instanceof CopycatGirderStrutBlockEntity otherCopycat && otherCopycat.hasMimickedState()) {
                            textureState = otherCopycat.getMimickedState();
                            break;
                        }
                    }
                }

                if (textureState != null && player != null && !player.isCreative()) {
                    // Compute total cost (same as strut drop count) for texture drops
                    int textureCost = strutBe.totalCost();
                    if (textureCost <= 0) {
                        strutBe.migrateLegacyCosts();
                        textureCost = strutBe.totalCost();
                    }
                    if (textureCost > 0) {
                        player.getInventory().placeItemBackInInventory(
                                new ItemStack(textureState.getBlock(), textureCost));
                    }
                }

                // Clear mimicked state on this anchor and all connected anchors
                // so onRemove won't double-drop texture materials
                if (be instanceof CopycatGirderStrutBlockEntity copycatBe) {
                    copycatBe.clearMimickedState();
                }
                for (BlockPos relOffset : strutBe.getConnectionsCopy()) {
                    BlockPos otherPos = relOffset.offset(pos);
                    BlockEntity otherBe = level.getBlockEntity(otherPos);
                    if (otherBe instanceof CopycatGirderStrutBlockEntity otherCopycat) {
                        otherCopycat.clearMimickedState();
                    }
                }
            }
        }
        // super handles strut item drops + block destruction
        return super.onSneakWrenched(state, context);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock() && !isMoving) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                // Only pop texture as world drop if it hasn't been cleared already (e.g., by wrench)
                if (be instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
                    Block.popResource(level, pos, new ItemStack(copycatBe.getMimickedState().getBlock()));
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            return copycatBe.getMimickedState().getSignal(level, pos, direction);
        }
        return 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            return copycatBe.getMimickedState().getDirectSignal(level, pos, direction);
        }
        return 0;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            return copycatBe.getMimickedState().getLightEmission();
        }
        return 0;
    }

    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        int count = 1;
        if (be instanceof GirderStrutBlockEntity gbe) {
            count = Math.max(1, gbe.connectionCount());
        }
        ItemRequirement req = new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, new ItemStack(this, count));
        if (be instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            req = req.union(new ItemRequirement(ItemRequirement.ItemUseType.CONSUME,
                    new ItemStack(copycatBe.getMimickedState().getBlock(), count)));
        }
        return req;
    }

    @Override
    public Class<GirderStrutBlockEntity> getBlockEntityClass() {
        return GirderStrutBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GirderStrutBlockEntity> getBlockEntityType() {
        return CMGBlockEntityTypes.COPYCAT_GIRDER_STRUT.get();
    }
}
