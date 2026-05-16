package com.agent772.createmoregirder.content.strut;

import com.agent772.createmoregirder.CMGDataComponents;
import com.agent772.createmoregirder.content.copycat_strut.CopycatGirderStrutBlock;
import com.agent772.createmoregirder.content.copycat_strut.CopycatGirderStrutBlockItem;
import net.createmod.catnip.outliner.Outliner;
import net.createmod.catnip.theme.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * Client-side visual effects for girder strut placement
 *
 * Adapted from Bits-n-Bobs by Industrialists-Of-Create
 * Original: https://github.com/Industrialists-Of-Create/Bits-n-Bobs
 * Licensed under MIT License
 */
public class GirderStrutPlacementEffects {

    private static final float PARTICLE_DENSITY = 0.1f;

    public static void tick(final LocalPlayer player) {
        GirderStrutCostOverlay.reset();

        if (Minecraft.getInstance().isPaused() || Minecraft.getInstance().hitResult == null) return;

        final ItemStack heldItem = (player.getMainHandItem().getItem() instanceof GirderStrutBlockItem || player.getMainHandItem().getItem() instanceof CopycatGirderStrutBlockItem) ? player.getMainHandItem() :
                (player.getOffhandItem().getItem() instanceof GirderStrutBlockItem || player.getOffhandItem().getItem() instanceof CopycatGirderStrutBlockItem) ? player.getOffhandItem() : null;
        if (heldItem != null) {
            display(player, heldItem);
        }
    }

    private static void display(final LocalPlayer player, final ItemStack heldItem) {
        final ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        final BlockPos fromPos = CMGDataComponents.getGirderStrutFrom(heldItem);
        final Direction fromFace = CMGDataComponents.getGirderStrutFromFace(heldItem);

        if (fromPos == null) {
            return;
        }

        final HitResult genericHit = Minecraft.getInstance().hitResult;
        if (!(genericHit instanceof final BlockHitResult hit) || genericHit.getType() != HitResult.Type.BLOCK) {
            return;
        }

        final BlockPos targetPos = resolvePlacementPos(level, hit.getBlockPos(), hit.getDirection());
        if (targetPos == null || targetPos.distSqr(fromPos) > GirderStrutBlock.MAX_SPAN * GirderStrutBlock.MAX_SPAN * 1.5) {
            return;
        }

        if (fromPos.equals(targetPos)) {
            final Vector3f invalidColor = new Vector3f(.85f, .35f, .55f);
            showAnchorBox(fromPos, fromFace != null ? fromFace.getOpposite() : Direction.UP, "from",
                    (int) (invalidColor.x * 256), (int) (invalidColor.y * 256), (int) (invalidColor.z * 256));
            return;
        }

        Direction targetFace = hit.getDirection();

        final BlockState targetState = level.getBlockState(targetPos);
        if (targetState.getBlock() instanceof GirderStrutBlock) {
            targetFace = targetState.getValue(GirderStrutBlock.FACING);
        }

        final Vec3 renderFrom = Vec3.atCenterOf(fromPos);
        final Vec3 renderTo = Vec3.atCenterOf(targetPos);

        final Vec3 delta = renderTo.subtract(renderFrom);
        final double length = delta.length();
        if (length > GirderStrutBlock.MAX_SPAN * 3) {
            return;
        }

        final boolean valid = GirderStrutBlockItem.isValidConnection(level, fromPos, fromFace, targetPos, targetFace);

        final boolean anchorOccupied = GirderStrutBlockEntity.isAnchorAtCapacity(level, fromPos)
                || GirderStrutBlockEntity.isAnchorAtCapacity(level, targetPos);

        final int cost = GirderStrutBlockEntity.computeSegmentCost(level, fromPos, fromFace, targetPos, targetFace);
        final int available = countMatchingItems(player, heldItem);
        final boolean strutFulfilled = player.isCreative() || available >= cost;

        // Check texture block availability for copycat struts
        boolean isCopycatStrut = heldItem.getItem() instanceof CopycatGirderStrutBlockItem;
        boolean hasTextureBlock = false;
        boolean textureFulfilled = true;
        ItemStack textureItem = ItemStack.EMPTY;
        int textureAvailable = 0;
        if (isCopycatStrut) {
            String storedOffhandBlock = CMGDataComponents.getCopycatStrutOffhandBlock(heldItem);
            ItemStack offhand = player.getOffhandItem();
            if (storedOffhandBlock != null && !offhand.isEmpty()
                    && offhand.getItem() instanceof BlockItem blockItem
                    && offhand.getItem().toString().equals(storedOffhandBlock)
                    && CopycatGirderStrutBlock.isValidMaterial(blockItem.getBlock().defaultBlockState())) {
                hasTextureBlock = true;
                textureItem = offhand;
                textureAvailable = countMatchingItems(player, offhand);
                textureFulfilled = player.isCreative() || textureAvailable >= cost;
            }
        }

        final boolean fulfilled = strutFulfilled && textureFulfilled;

        final Vector3f color;
        final Vector3f outlinerColor;
        if (!valid || anchorOccupied) {
            color = new Vector3f(.9f, .3f, .5f);
            outlinerColor = new Vector3f(.85f, .35f, .55f);
        } else if (!fulfilled) {
            color = new Vector3f(.9f, .6f, .2f);
            outlinerColor = new Vector3f(.85f, .55f, .25f);
        } else {
            color = new Vector3f(.3f, .9f, .5f);
            outlinerColor = new Vector3f(.35f, .85f, .55f);
        }

        final Vec3 dir = delta.normalize();
        final double step = 0.25;
        for (double t = 0; t <= length; t += step) {
            final Vec3 lerped = renderFrom.add(dir.scale(t));

            if (level.getRandom().nextFloat() > PARTICLE_DENSITY) {
                continue;
            }

            level.addParticle(
                    new DustParticleOptions(color, 1), true,
                    lerped.x, lerped.y, lerped.z, 0, 0, 0);
        }
        level.addParticle(
                new DustParticleOptions(color, 1), true,
                renderTo.x, renderTo.y, renderTo.z, 0, 0, 0);

        showAnchorBox(fromPos, fromFace.getOpposite(), "from", (int) (outlinerColor.x * 256), (int) (outlinerColor.y * 256), (int) (outlinerColor.z * 256));
        showAnchorBox(targetPos, targetFace.getOpposite(), "to", (int) (outlinerColor.x * 256), (int) (outlinerColor.y * 256), (int) (outlinerColor.z * 256));

        if (!player.isCreative()) {
            if (hasTextureBlock) {
                GirderStrutCostOverlay.displayWithTexture(heldItem, cost, strutFulfilled,
                        textureItem, cost, textureFulfilled);
            } else {
                GirderStrutCostOverlay.display(heldItem, cost, strutFulfilled);
            }
        }

        if (anchorOccupied) {
            player.displayClientMessage(Component.translatable("message.createmoregirder.strut_anchor_occupied")
                    .withStyle(ChatFormatting.RED), true);
        } else if (!valid) {
            player.displayClientMessage(Component.translatable("message.createmoregirder.strut_invalid_connection")
                    .withStyle(ChatFormatting.RED), true);
        } else if (!strutFulfilled) {
            player.displayClientMessage(Component.translatable("message.createmoregirder.strut_not_enough_items")
                    .withStyle(ChatFormatting.RED), true);
        } else if (!textureFulfilled) {
            player.displayClientMessage(Component.translatable("message.createmoregirder.missing_texture_blocks",
                    cost - textureAvailable).withStyle(ChatFormatting.RED), true);
        } else {
            player.displayClientMessage(Component.translatable("message.createmoregirder.strut_valid_connection")
                    .withStyle(ChatFormatting.GREEN), true);
        }
    }

    private static int countMatchingItems(final LocalPlayer player, final ItemStack reference) {
        final Inventory inventory = player.getInventory();
        int total = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            final ItemStack slotStack = inventory.getItem(i);
            if (!slotStack.isEmpty() && slotStack.getItem() == reference.getItem()) {
                total += slotStack.getCount();
            }
        }
        return total;
    }

    private static void showAnchorBox(final BlockPos targetPos, final Direction targetFace, final String id, final int r, final int g, final int b) {
        AABB box = new AABB(
                0, 0, 0, (targetFace.getStepX() == 0 ? 0.25f : 0f) + 0.25f, (targetFace.getStepY() == 0 ? 0.25f : 0f) + 0.25f, (targetFace.getStepZ() == 0 ? 0.25f : 0f) + 0.25f
        );
        box = box
                .move(targetPos.getX(), targetPos.getY(), targetPos.getZ())
                .move(-box.getXsize() * 0.5f + 0.5f, -box.getYsize() * 0.5f + 0.5f, -box.getZsize() * 0.5f + 0.5f)
                .move(targetFace.getStepX() * 0.5f, targetFace.getStepY() * 0.5f, targetFace.getStepZ() * 0.5f);
        Outliner.getInstance().showAABB(id, box).colored(new Color(r, g, b)).lineWidth(1 / 16f);
    }

    private static BlockPos resolvePlacementPos(final ClientLevel level, final BlockPos clickedPos, final Direction face) {
        BlockPos pos = clickedPos;
        if (!(level.getBlockState(pos).getBlock() instanceof GirderStrutBlock)) {
            pos = pos.relative(face);
            if (!(level.getBlockState(pos).canBeReplaced() || level.getBlockState(pos).getBlock() instanceof GirderStrutBlock)) {
                return null;
            }
        }
        return pos;
    }
}
