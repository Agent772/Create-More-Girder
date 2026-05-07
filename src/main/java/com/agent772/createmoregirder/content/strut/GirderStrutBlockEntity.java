package com.agent772.createmoregirder.content.strut;

import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Block Entity for Girder Struts that tracks connections to other struts
 *
 * Adapted from Bits-n-Bobs by Industrialists-Of-Create
 * Original: https://github.com/Industrialists-Of-Create/Bits-n-Bobs
 * Licensed under MIT License
 *
 * Modifications:
 * - Adapted for Create: More Girder mod structure
 */
public class GirderStrutBlockEntity extends SmartBlockEntity implements IBlockEntityRelighter, SpecialBlockEntityItemRequirement {

    private final Map<BlockPos, Integer> connections = new HashMap<>();
    private boolean needsCostMigration;
    public @Nullable SuperByteBuffer connectionRenderBufferCache;

    public GirderStrutBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static int computeSegmentCost(BlockPos a, Direction faceA, BlockPos b, Direction faceB) {
        Vec3 aAttach = Vec3.atCenterOf(a).relative(faceA, -0.4);
        Vec3 bAttach = Vec3.atCenterOf(b).relative(faceB, -0.4);
        return Math.max(1, (int) Math.ceil(aAttach.distanceTo(bAttach)));
    }

    public void addConnection(BlockPos other, int cost) {
        BlockPos relative = other.immutable().subtract(getBlockPos());
        if (relative.equals(BlockPos.ZERO)) return;
        Integer previous = connections.put(relative, cost);
        if (previous == null || previous.intValue() != cost) {
            setChanged();
            sendData();
            notifyModelChange();
        }
    }

    public void addConnection(BlockPos other) {
        addConnection(other, computeCostForConnection(other));
    }

    private int computeCostForConnection(BlockPos otherAbsolute) {
        if (level == null) return 1;
        Direction myFace = getBlockState().getValue(GirderStrutBlock.FACING);
        Direction otherFace;
        if (level.isLoaded(otherAbsolute)) {
            BlockState otherState = level.getBlockState(otherAbsolute);
            if (otherState.getBlock() instanceof GirderStrutBlock) {
                otherFace = otherState.getValue(GirderStrutBlock.FACING);
            } else {
                // Neighbor may already be destroyed (cascade, chunk corruption); best-effort fallback
                otherFace = myFace.getOpposite();
            }
        } else {
            otherFace = myFace.getOpposite();
        }
        return computeSegmentCost(getBlockPos(), myFace, otherAbsolute, otherFace);
    }

    public void removeConnection(BlockPos pos) {
        if (connections.remove(pos.subtract(getBlockPos())) != null) {
            setChanged();
            sendData();
            notifyModelChange();
        }
    }

    public int getConnectionCost(BlockPos absolutePos) {
        Integer cost = connections.get(absolutePos.subtract(getBlockPos()));
        return cost != null ? cost : 0;
    }

    public boolean hasConnectionTo(BlockPos pos) {
        return connections.containsKey(pos.subtract(getBlockPos()));
    }

    public int connectionCount() {
        return connections.size();
    }

    public Set<BlockPos> getConnectionsCopy() {
        return Set.copyOf(connections.keySet());
    }

    public int totalCost() {
        return connections.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        ListTag list = new ListTag();
        for (Map.Entry<BlockPos, Integer> entry : connections.entrySet()) {
            CompoundTag ct = new CompoundTag();
            ct.putInt("X", entry.getKey().getX());
            ct.putInt("Y", entry.getKey().getY());
            ct.putInt("Z", entry.getKey().getZ());
            ct.putInt("Cost", entry.getValue());
            list.add(ct);
        }
        tag.put("Connections", list);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        connections.clear();
        boolean needsMigration = false;
        if (tag.contains("Connections", Tag.TAG_LIST)) {
            ListTag list = tag.getList("Connections", Tag.TAG_COMPOUND);
            for (Tag t : list) {
                if (t instanceof CompoundTag ct) {
                    BlockPos relative = new BlockPos(ct.getInt("X"), ct.getInt("Y"), ct.getInt("Z"));
                    int cost = ct.contains("Cost") ? ct.getInt("Cost") : 0;
                    connections.put(relative, cost);
                    if (cost <= 0) needsMigration = true;
                }
            }
        }
        if (clientPacket) {
            notifyModelChange();
        }
        if (!clientPacket && needsMigration) {
            needsCostMigration = true;
            if (level != null) {
                migrateLegacyCosts();
                needsCostMigration = false;
            }
        }
    }

    public void migrateLegacyCosts() {
        if (level == null) return;
        boolean changed = false;
        for (Map.Entry<BlockPos, Integer> entry : connections.entrySet()) {
            if (entry.getValue() <= 0) {
                BlockPos otherAbsolute = entry.getKey().offset(getBlockPos());
                entry.setValue(computeCostForConnection(otherAbsolute));
                changed = true;
            }
        }
        if (changed) {
            setChanged();
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (needsCostMigration && level != null && !level.isClientSide) {
            migrateLegacyCosts();
            needsCostMigration = false;
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
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}
