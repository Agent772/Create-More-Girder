package com.agent772.createmoregirder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class CMGDataComponents {

    private static final String GIRDER_STRUT_FROM = "GirderStrutFrom";
    private static final String GIRDER_STRUT_FROM_FACE = "GirderStrutFromFace";
    private static final String COPYCAT_STRUT_OFFHAND_BLOCK = "CopycatStrutOffhandBlock";

    // =============================
    // BlockPos
    // =============================

    public static void setGirderStrutFrom(ItemStack stack, BlockPos pos) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putIntArray(GIRDER_STRUT_FROM, new int[]{pos.getX(), pos.getY(), pos.getZ()});
    }

    public static BlockPos getGirderStrutFrom(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(GIRDER_STRUT_FROM)) return null;

        int[] data = tag.getIntArray(GIRDER_STRUT_FROM);
        if (data.length != 3) return null;

        return new BlockPos(data[0], data[1], data[2]);
    }

    // =============================
    // Direction
    // =============================

    public static void setGirderStrutFromFace(ItemStack stack, Direction direction) {
        stack.getOrCreateTag().putString(GIRDER_STRUT_FROM_FACE, direction.getName());
    }

    public static Direction getGirderStrutFromFace(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(GIRDER_STRUT_FROM_FACE)) return null;

        return Direction.byName(tag.getString(GIRDER_STRUT_FROM_FACE));
    }

    // =============================
    // Copycat Strut Offhand Block
    // =============================

    public static void setCopycatStrutOffhandBlock(ItemStack stack, String blockId) {
        stack.getOrCreateTag().putString(COPYCAT_STRUT_OFFHAND_BLOCK, blockId);
    }

    public static String getCopycatStrutOffhandBlock(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(COPYCAT_STRUT_OFFHAND_BLOCK)) return null;

        return tag.getString(COPYCAT_STRUT_OFFHAND_BLOCK);
    }

    // =============================
    // Clear
    // =============================

    public static void clear(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return;

        tag.remove(GIRDER_STRUT_FROM);
        tag.remove(GIRDER_STRUT_FROM_FACE);
        tag.remove(COPYCAT_STRUT_OFFHAND_BLOCK);

        // Remove the tag entirely if empty so items stack with fresh ones
        if (tag.isEmpty()) {
            stack.setTag(null);
        }
    }
}
