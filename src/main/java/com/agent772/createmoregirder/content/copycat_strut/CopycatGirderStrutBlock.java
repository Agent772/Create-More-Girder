package com.agent772.createmoregirder.content.copycat_strut;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.content.strut.GirderStrutBlock;
import com.agent772.createmoregirder.content.strut.GirderStrutBlockEntity;
import com.agent772.createmoregirder.content.strut.StrutModelType;

import com.simibubi.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

public class CopycatGirderStrutBlock extends GirderStrutBlock implements SpecialBlockItemRequirement {

    public CopycatGirderStrutBlock(Properties properties) {
        super(properties, StrutModelType.COPYCAT);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player == null || hand != InteractionHand.MAIN_HAND)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        // If holding a copycat strut item, let normal placement flow handle it
        if (stack.getItem() instanceof CopycatGirderStrutBlockItem)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (stack.getItem() instanceof BlockItem blockItem) {
            Block sourceBlock = blockItem.getBlock();
            BlockState sourceState = sourceBlock.defaultBlockState();

            if (!isValidMaterial(sourceState))
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
                if (sourceBlock == copycatBe.getMimickedState().getBlock()) {
                    // Same block as stored texture -> cycle texture
                    if (!level.isClientSide) {
                        if (!copycatBe.cycleTexture()) {
                            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                        }
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
                        // Propagate cycle to connected anchors
                        propagateCycleToConnections(level, pos, copycatBe);
                    }
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
                // Different block -> treat anchor as solid, place against it
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            // No mimicked state -> treat anchor as solid, place against it
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (be instanceof CopycatGirderStrutBlockEntity strutBe) {
            int totalCost = strutBe.totalCost();
            if (totalCost <= 0) {
                strutBe.migrateLegacyCosts();
                totalCost = strutBe.totalCost();
            }
            if (totalCost <= 0) {
                return List.of();
            }
            List<ItemStack> drops = new ArrayList<>();
            drops.add(new ItemStack(this, totalCost));
            if (strutBe.hasMimickedState()) {
                drops.add(new ItemStack(strutBe.getMimickedState().getBlock(), totalCost));
            }
            return drops;
        }
        return List.of(new ItemStack(this, 1));
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            return copycatBe.getMimickedState().getSignal(level, pos, direction);
        }
        return 0;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
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

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        ItemRequirement req = new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, new ItemStack(this));
        if (be instanceof CopycatGirderStrutBlockEntity copycatBe && copycatBe.hasMimickedState()) {
            req = req.union(new ItemRequirement(ItemRequirement.ItemUseType.CONSUME,
                    new ItemStack(copycatBe.getMimickedState().getBlock())));
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
