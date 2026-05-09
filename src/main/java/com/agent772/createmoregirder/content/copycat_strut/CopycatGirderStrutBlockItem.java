package com.agent772.createmoregirder.content.copycat_strut;

import com.agent772.createmoregirder.CMGDataComponents;
import com.agent772.createmoregirder.content.strut.GirderStrutBlock;
import com.agent772.createmoregirder.content.strut.GirderStrutBlockEntity;
import com.agent772.createmoregirder.content.strut.GirderStrutBlockItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CopycatGirderStrutBlockItem extends BlockItem {

    private static final double MAX_ANGLE_DEGREES = 90;
    private static final double MIN_DOT_THRESHOLD = Math.cos(Math.toRadians(MAX_ANGLE_DEGREES));

    public CopycatGirderStrutBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        final ItemStack stack = context.getItemInHand();
        final Level level = context.getLevel();
        final BlockPos clickedPos = context.getClickedPos();
        final Direction face = context.getClickedFace();

        if (context.isSecondaryUseActive()) {
            if (CMGDataComponents.getGirderStrutFrom(stack) != null
                    || CMGDataComponents.getGirderStrutFromFace(stack) != null) {
                CMGDataComponents.clear(stack);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            return InteractionResult.PASS;
        }

        final BlockPos placementPos = resolvePlacementPos(level, clickedPos, face);
        Direction targetFace = face;
        if (placementPos != null && level.getBlockState(placementPos).getBlock().equals(getBlock())) {
            targetFace = level.getBlockState(placementPos).getValue(GirderStrutBlock.FACING);
        }

        if (CMGDataComponents.getGirderStrutFrom(stack) == null) {
            if (placementPos == null) {
                return InteractionResult.FAIL;
            }

            if (GirderStrutBlockEntity.isAnchorAtCapacity(level, placementPos)) {
                if (!level.isClientSide && context.getPlayer() != null) {
                    notifyPlayer(context.getPlayer(), Component.translatable("message.createmoregirder.strut_anchor_occupied")
                            .withStyle(ChatFormatting.RED));
                }
                return InteractionResult.FAIL;
            }

            CMGDataComponents.setGirderStrutFrom(stack, placementPos);
            CMGDataComponents.setGirderStrutFromFace(stack, targetFace);

            // Capture offhand texture block on first click
            if (context.getPlayer() != null) {
                ItemStack offhand = context.getPlayer().getOffhandItem();
                if (!offhand.isEmpty() && offhand.getItem() instanceof BlockItem blockItem) {
                    BlockState offhandState = blockItem.getBlock().defaultBlockState();
                    if (CopycatGirderStrutBlock.isValidMaterial(offhandState)) {
                        CMGDataComponents.setCopycatStrutOffhandBlock(stack, offhand.getItem().toString());
                    }
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        final BlockPos fromPos = CMGDataComponents.getGirderStrutFrom(stack);
        Direction fromFace = CMGDataComponents.getGirderStrutFromFace(stack);
        if (fromPos == null) {
            CMGDataComponents.clear(stack);
            return InteractionResult.FAIL;
        }

        if (placementPos == null) {
            return InteractionResult.FAIL;
        }

        if (fromFace == null) {
            final BlockState fromState = level.getBlockState(fromPos);
            if (fromState.getBlock().equals(getBlock())) {
                fromFace = fromState.getValue(GirderStrutBlock.FACING);
            } else {
                fromFace = targetFace.getOpposite();
            }
        }

        if (!level.isClientSide) {
            final ConnectionResult result = tryConnect(context, fromPos, fromFace, placementPos, targetFace, stack);
            if (result != ConnectionResult.SUCCESS) {
                if (result == ConnectionResult.INVALID) {
                    CMGDataComponents.clear(stack);
                }
                if (result == ConnectionResult.ANCHOR_OCCUPIED && context.getPlayer() != null) {
                    notifyPlayer(context.getPlayer(), Component.translatable("message.createmoregirder.strut_anchor_occupied")
                            .withStyle(ChatFormatting.RED));
                }
                return InteractionResult.FAIL;
            }
        }

        CMGDataComponents.clear(stack);

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return CMGDataComponents.getGirderStrutFrom(stack) != null || super.isFoil(stack);
    }

    private ConnectionResult tryConnect(UseOnContext context, BlockPos fromPos, Direction fromFace, BlockPos targetPos, Direction targetFace, ItemStack stack) {
        final Level level = context.getLevel();
        final Player player = context.getPlayer();

        if (!GirderStrutBlockItem.isValidConnection(level, fromPos, fromFace, targetPos, targetFace)) {
            return ConnectionResult.INVALID;
        }

        final BlockState fromState = level.getBlockState(fromPos);
        final BlockState targetState = level.getBlockState(targetPos);

        final boolean fromNeedsPlacement = !(fromState.getBlock().equals(getBlock()));
        final boolean targetNeedsPlacement = !(targetState.getBlock().equals(getBlock()));

        if (!fromNeedsPlacement && GirderStrutBlockEntity.isAnchorAtCapacity(level, fromPos)) {
            return ConnectionResult.ANCHOR_OCCUPIED;
        }
        if (!targetNeedsPlacement && GirderStrutBlockEntity.isAnchorAtCapacity(level, targetPos)) {
            return ConnectionResult.ANCHOR_OCCUPIED;
        }

        final int segmentCost = GirderStrutBlockEntity.computeSegmentCost(fromPos, fromFace, targetPos, targetFace);

        if (fromNeedsPlacement && !canOccupy(level, fromPos)) {
            return ConnectionResult.INVALID;
        }
        if (targetNeedsPlacement && !canOccupy(level, targetPos)) {
            return ConnectionResult.INVALID;
        }

        // Determine if offhand texture block matches between click 1 and click 2
        BlockState textureState = null;
        boolean applyTexture = false;
        if (player != null) {
            String storedOffhandBlock = CMGDataComponents.getCopycatStrutOffhandBlock(stack);
            ItemStack currentOffhand = player.getOffhandItem();

            if (storedOffhandBlock != null && !currentOffhand.isEmpty()
                    && currentOffhand.getItem() instanceof BlockItem blockItem
                    && currentOffhand.getItem().toString().equals(storedOffhandBlock)) {
                BlockState candidateState = blockItem.getBlock().defaultBlockState();
                if (CopycatGirderStrutBlock.isValidMaterial(candidateState)) {
                    textureState = candidateState;
                    applyTexture = true;
                }
            }
        }

        if (player != null && !player.getAbilities().instabuild) {
            if (!hasRequiredItems(player, stack, segmentCost)) {
                return ConnectionResult.MISSING_ITEMS;
            }
        }

        int placedCount = 0;

        if (fromNeedsPlacement) {
            if (!placeAnchor(level, fromPos, fromFace, player, stack.copy())) {
                return ConnectionResult.INVALID;
            }
            placedCount++;
        } else if (fromState.getValue(GirderStrutBlock.FACING) != fromFace) {
            level.setBlock(fromPos, fromState.setValue(GirderStrutBlock.FACING, fromFace), Block.UPDATE_ALL);
        }

        if (targetNeedsPlacement) {
            if (!placeAnchor(level, targetPos, targetFace, player, stack.copy())) {
                if (fromNeedsPlacement) {
                    level.removeBlock(fromPos, false);
                }
                return ConnectionResult.INVALID;
            }
            placedCount++;
        } else if (targetState.getValue(GirderStrutBlock.FACING) != targetFace) {
            level.setBlock(targetPos, targetState.setValue(GirderStrutBlock.FACING, targetFace), Block.UPDATE_ALL);
        }

        final BlockState newFromState = level.getBlockState(fromPos);
        final BlockState newTargetState = level.getBlockState(targetPos);

        if (!(newFromState.getBlock().equals(getBlock())) || !(newTargetState.getBlock().equals(getBlock()))) {
            return ConnectionResult.INVALID;
        }

        // Consume strut items based on segment cost
        consumeItems(player, stack, segmentCost);

        // Consume offhand texture blocks based on segment cost
        if (applyTexture && player != null && !player.getAbilities().instabuild) {
            consumeOffhandItems(player, segmentCost);
        }

        // Force inventory sync to client
        if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            serverPlayer.inventoryMenu.broadcastChanges();
        }

        final SoundType soundType = getBlock().defaultBlockState().getSoundType(level, targetPos, context.getPlayer());
        level.playSound(null, targetPos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);

        connect(level, fromPos, targetPos, segmentCost);

        // Apply mimicked state to both anchors
        if (applyTexture && textureState != null) {
            if (level.getBlockEntity(fromPos) instanceof CopycatGirderStrutBlockEntity fromBe) {
                fromBe.applyMaterial(textureState, 0);
            }
            if (level.getBlockEntity(targetPos) instanceof CopycatGirderStrutBlockEntity targetBe) {
                targetBe.applyMaterial(textureState, 0);
            }
        }

        return ConnectionResult.SUCCESS;
    }

    private void connect(Level level, BlockPos fromPos, BlockPos targetPos, int cost) {
        if (!(level.getBlockEntity(fromPos) instanceof GirderStrutBlockEntity from)) {
            return;
        }
        if (!(level.getBlockEntity(targetPos) instanceof GirderStrutBlockEntity target)) {
            return;
        }
        from.addConnection(targetPos, cost);
        target.addConnection(fromPos, cost);

        final BlockState updatedFromState = level.getBlockState(fromPos);
        final BlockState updatedTargetState = level.getBlockState(targetPos);
        level.sendBlockUpdated(fromPos, updatedFromState, updatedFromState, Block.UPDATE_ALL);
        level.sendBlockUpdated(targetPos, updatedTargetState, updatedTargetState, Block.UPDATE_ALL);
    }

    private boolean hasRequiredItems(Player player, ItemStack heldStack, int required) {
        if (required <= 0) return true;
        if (player == null) return heldStack.getCount() >= required;

        int strutAvailable = countMatchingItems(player, heldStack);
        if (strutAvailable < required) {
            notifyPlayer(player, Component.translatable("message.createmoregirder.missing_girder_struts", required - strutAvailable)
                    .withStyle(ChatFormatting.RED));
            return false;
        }

        return true;
    }

    private void consumeItems(Player player, ItemStack heldStack, int amount) {
        if (amount <= 0 || player == null || player.getAbilities().instabuild) return;

        int remaining = amount;
        final Inventory inventory = player.getInventory();
        final int heldSlot = inventory.selected;
        final net.minecraft.world.item.Item itemType = heldStack.getItem();

        remaining -= drainStack(heldStack, remaining);

        for (int i = 0; i < inventory.getContainerSize() && remaining > 0; i++) {
            if (i == heldSlot) continue;
            final ItemStack slotStack = inventory.getItem(i);
            if (slotStack.isEmpty() || slotStack.getItem() != itemType) continue;
            remaining -= drainStack(slotStack, remaining);
        }
    }

    private void consumeOffhandItems(Player player, int amount) {
        if (amount <= 0) return;

        int remaining = amount;
        ItemStack offhand = player.getOffhandItem();
        ItemStack reference = offhand.copy();

        remaining -= drainStack(offhand, remaining);
        if (offhand.isEmpty()) {
            player.setItemInHand(net.minecraft.world.InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }

        if (remaining > 0) {
            Inventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getContainerSize() && remaining > 0; i++) {
                ItemStack slotStack = inventory.getItem(i);
                if (!ItemStack.isSameItem(slotStack, reference)) continue;
                remaining -= drainStack(slotStack, remaining);
            }
        }
    }

    private int drainStack(ItemStack stack, int amount) {
        if (amount <= 0) return 0;
        int toRemove = Math.min(stack.getCount(), amount);
        if (toRemove > 0) stack.shrink(toRemove);
        return toRemove;
    }

    private int countMatchingItems(Player player, ItemStack reference) {
        Inventory inventory = player.getInventory();
        int total = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slotStack = inventory.getItem(i);
            if (!slotStack.isEmpty() && slotStack.getItem() == reference.getItem()) {
                total += slotStack.getCount();
            }
        }
        return total;
    }

    private boolean placeAnchor(Level level, BlockPos pos, Direction face, Player player, ItemStack stackSnapshot) {
        final BlockState newState = getBlock().defaultBlockState()
                .setValue(GirderStrutBlock.FACING, face)
                .setValue(BlockStateProperties.WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
        if (!level.setBlock(pos, newState, Block.UPDATE_ALL)) {
            return false;
        }

        final Block block = newState.getBlock();
        block.setPlacedBy(level, pos, newState, player, stackSnapshot);

        final SoundType soundType = newState.getSoundType();
        level.playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS,
                (soundType.getVolume() + 1.0F) / 2.0F,
                soundType.getPitch() * 0.8F);
        level.gameEvent(player, net.minecraft.world.level.gameevent.GameEvent.BLOCK_PLACE, pos);
        return true;
    }

    private boolean canOccupy(Level level, BlockPos pos) {
        final BlockState state = level.getBlockState(pos);
        return state.canBeReplaced() || state.getBlock().equals(getBlock());
    }

    private BlockPos resolvePlacementPos(Level level, BlockPos clickedPos, Direction face) {
        final BlockState clickedState = level.getBlockState(clickedPos);
        if (clickedState.getBlock().equals(getBlock())) {
            return clickedPos;
        }
        final BlockPos pos = clickedPos.relative(face);
        final BlockState state = level.getBlockState(pos);
        if (!state.canBeReplaced() && !(state.getBlock().equals(getBlock()))) {
            return null;
        }
        return pos;
    }

    private static void notifyPlayer(Player player, Component message) {
        player.displayClientMessage(message, true);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.createmoregirder.copycat_strut.placement")
                .withStyle(ChatFormatting.GOLD));

        if (net.minecraft.client.gui.screens.Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.createmoregirder.copycat_strut.cycle_detail")
                    .withStyle(ChatFormatting.GOLD));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.createmoregirder.copycat_strut.hold_shift")
                    .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }
    }

    private enum ConnectionResult {
        SUCCESS,
        INVALID,
        MISSING_ITEMS,
        ANCHOR_OCCUPIED
    }
}
