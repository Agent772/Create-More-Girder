package com.agent772.createmoregirder.content.copycat_girder;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CopycatGirderBlockEntity extends SmartBlockEntity implements MimickedBlockEntity {

    private final MimickedStateStorage storage = new MimickedStateStorage();
    private transient boolean suppressTextureDrop = false;

    public CopycatGirderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void suppressTextureDrop() {
        this.suppressTextureDrop = true;
    }

    public boolean isTextureDropSuppressed() {
        return suppressTextureDrop;
    }

    @Override
    public BlockState getMimickedState() {
        return storage.getMimickedState();
    }

    @Override
    public boolean hasMimickedState() {
        return storage.hasMimickedState();
    }

    public void adoptFrom(BlockState state, int rotation) {
        storage.adoptFrom(state, rotation);
        notifyModelChange();
        setChanged();
        sendData();
    }

    public void clearMimickedState() {
        storage.clear();
        notifyModelChange();
        notifyRedstoneChange();
        setChanged();
        sendData();
    }

    @Override
    public int getFaceRotation() {
        return storage.getFaceRotation();
    }

    public boolean cycleTexture() {
        if (!storage.cycleTexture()) return false;
        notifyModelChange();
        notifyRedstoneChange();
        setChanged();
        sendData();
        return true;
    }

    public void applyMaterial(BlockState state, int rotation) {
        storage.adoptFrom(state, rotation);
        notifyModelChange();
        notifyRedstoneChange();
        setChanged();
        sendData();
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        storage.write(tag);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        boolean changed = storage.read(tag, level);
        if (clientPacket && changed) {
            notifyModelChange();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    private void notifyModelChange() {
        if (level != null) {
            if (level.isClientSide) {
                requestModelDataUpdate();
            }
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private void notifyRedstoneChange() {
        if (level != null && !level.isClientSide) {
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
        }
    }
}
