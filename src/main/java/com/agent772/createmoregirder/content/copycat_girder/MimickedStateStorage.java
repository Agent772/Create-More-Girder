package com.agent772.createmoregirder.content.copycat_girder;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Shared storage for the mimicked block state and face rotation used by both
 * {@link CopycatGirderBlockEntity} and {@link CopycatGirderEncasedShaftBlockEntity}.
 */
public class MimickedStateStorage {

    private BlockState mimickedState = Blocks.AIR.defaultBlockState();
    private int faceRotation = 0;

    public BlockState getMimickedState() {
        return mimickedState;
    }

    public boolean hasMimickedState() {
        return !mimickedState.isAir();
    }

    public int getFaceRotation() {
        return faceRotation;
    }

    public void setMimickedState(BlockState state) {
        this.mimickedState = state;
        this.faceRotation = 0;
    }

    public void adoptFrom(BlockState state, int rotation) {
        this.mimickedState = state == null ? Blocks.AIR.defaultBlockState() : state;
        this.faceRotation = Math.floorMod(rotation, 6);
    }

    public void clear() {
        this.mimickedState = Blocks.AIR.defaultBlockState();
        this.faceRotation = 0;
    }

    public void cycleFaceRotation() {
        this.faceRotation = (this.faceRotation + 1) % 6;
    }

    public boolean cycleTexture() {
        if (mimickedState.hasProperty(BlockStateProperties.FACING)) {
            mimickedState = mimickedState.cycle(BlockStateProperties.FACING);
            return true;
        }
        if (mimickedState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            Direction current = mimickedState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            mimickedState = mimickedState.setValue(BlockStateProperties.HORIZONTAL_FACING, current.getClockWise());
            return true;
        }
        if (mimickedState.hasProperty(BlockStateProperties.AXIS)) {
            mimickedState = mimickedState.cycle(BlockStateProperties.AXIS);
            return true;
        }
        if (mimickedState.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
            mimickedState = mimickedState.cycle(BlockStateProperties.HORIZONTAL_AXIS);
            return true;
        }
        if (mimickedState.hasProperty(BlockStateProperties.LIT)) {
            mimickedState = mimickedState.cycle(BlockStateProperties.LIT);
            return true;
        }
        return false;
    }

    public void write(CompoundTag tag) {
        if (hasMimickedState()) {
            tag.put("MimickedState", NbtUtils.writeBlockState(mimickedState));
            tag.putInt("FaceRotation", faceRotation);
        }
    }

    public boolean read(CompoundTag tag, Level level) {
        BlockState previous = mimickedState;
        int previousRotation = faceRotation;
        if (tag.contains("MimickedState")) {
            HolderLookup<Block> lookup = level != null
                ? level.holderLookup(Registries.BLOCK)
                : BuiltInRegistries.BLOCK.asLookup();
            mimickedState = NbtUtils.readBlockState(lookup, tag.getCompound("MimickedState"));
            faceRotation = tag.getInt("FaceRotation");
        } else {
            mimickedState = Blocks.AIR.defaultBlockState();
            faceRotation = 0;
        }
        return !previous.equals(mimickedState) || previousRotation != faceRotation;
    }
}
