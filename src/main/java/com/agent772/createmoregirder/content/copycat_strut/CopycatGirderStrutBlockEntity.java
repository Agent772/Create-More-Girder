package com.agent772.createmoregirder.content.copycat_strut;

import com.agent772.createmoregirder.content.copycat_girder.MimickedBlockEntity;
import com.agent772.createmoregirder.content.copycat_girder.MimickedStateStorage;
import com.agent772.createmoregirder.content.strut.GirderStrutBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatGirderStrutBlockEntity extends GirderStrutBlockEntity implements MimickedBlockEntity {

    private final MimickedStateStorage storage = new MimickedStateStorage();

    public CopycatGirderStrutBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public BlockState getMimickedState() {
        return storage.getMimickedState();
    }

    @Override
    public boolean hasMimickedState() {
        return storage.hasMimickedState();
    }

    @Override
    public int getFaceRotation() {
        return storage.getFaceRotation();
    }

    public void applyMaterial(BlockState state, int rotation) {
        storage.adoptFrom(state, rotation);
        notifyCopycatModelChange();
        notifyRedstoneChange();
        setChanged();
        sendData();
    }

    public void clearMimickedState() {
        storage.clear();
        notifyCopycatModelChange();
        notifyRedstoneChange();
        setChanged();
        sendData();
    }

    public boolean cycleTexture() {
        if (!storage.cycleTexture()) return false;
        notifyCopycatModelChange();
        notifyRedstoneChange();
        setChanged();
        sendData();
        return true;
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
            notifyCopycatModelChange();
        }
    }

    private void notifyCopycatModelChange() {
        connectionRenderBufferCache = null;
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
