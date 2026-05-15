package com.agent772.createmoregirder.content.strut;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;

import com.agent772.createmoregirder.config.CMGServerConfig;

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
public class GirderStrutBlockEntity extends SmartBlockEntity implements IBlockEntityRelighter {

    private final Map<BlockPos, Integer> connections = new HashMap<>();
    private boolean needsCostMigration;
    private transient int cachedDropCost = -1;
    public @Nullable SuperByteBuffer connectionRenderBufferCache;

    public GirderStrutBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static int computeSegmentCost(BlockGetter level, BlockPos a, Direction faceA, BlockPos b, Direction faceB) {
        double aDepth = anchorDepth(level, a, faceA);
        double bDepth = anchorDepth(level, b, faceB);
        Vec3 aAttach = Vec3.atCenterOf(a).relative(faceA, aDepth);
        Vec3 bAttach = Vec3.atCenterOf(b).relative(faceB, bDepth);
        return Math.max(1, (int) Math.ceil(aAttach.distanceTo(bAttach)));
    }

    private static double anchorDepth(BlockGetter level, BlockPos pos, Direction facing) {
        double depth = -0.4;
        if (level != null && GirderStrutAnchorOffset.shouldOffset(level, pos, facing)) {
            depth -= GirderStrutAnchorOffset.OFFSET_BLOCKS;
        }
        return depth;
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
        return computeSegmentCost(level, getBlockPos(), myFace, otherAbsolute, otherFace);
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

    /**
     * Returns whether this anchor can accept an additional connection.
     * Checked during placement; not enforced on read/rotate/mirror so legacy worlds are preserved.
     */
    public boolean canAcceptAdditionalConnection() {
        return connections.size() < CMGServerConfig.MAX_CONNECTIONS_PER_ANCHOR.get();
    }

    /**
     * Checks whether the anchor at the given position is at capacity.
     * Returns {@code false} if there is no {@link GirderStrutBlockEntity} at the position.
     * Usable from both server ({@code tryConnect}) and client ({@code GirderStrutPlacementEffects}).
     */
    public static boolean isAnchorAtCapacity(final BlockGetter level, final BlockPos pos) {
        return level.getBlockEntity(pos) instanceof GirderStrutBlockEntity be
                && !be.canAcceptAdditionalConnection();
    }

    public Set<BlockPos> getConnectionsCopy() {
        return Set.copyOf(connections.keySet());
    }

    public Map<BlockPos, Integer> getConnectionsWithCosts() {
        return Map.copyOf(connections);
    }

    public int totalCost() {
        return connections.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void cacheDropCost(int cost) {
        this.cachedDropCost = cost;
    }

    public int getCachedDropCost() {
        return cachedDropCost;
    }

    /**
     * Computes the item share this anchor should report for schematic material lists.
     * Each connection's cost is split between its two endpoints to avoid double-counting:
     * the "primary" end (canonical positive offset) gets ceil(cost/2),
     * the "secondary" end gets floor(cost/2).
     */
    public static int computeAnchorItemShare(Map<BlockPos, Integer> connectionsWithCosts) {
        int share = 0;
        for (Map.Entry<BlockPos, Integer> entry : connectionsWithCosts.entrySet()) {
            int cost = entry.getValue();
            if (cost <= 0) continue;
            if (isPrimaryEnd(entry.getKey())) {
                share += (cost + 1) / 2;
            } else {
                share += cost / 2;
            }
        }
        return share;
    }

    private static boolean isPrimaryEnd(BlockPos relOffset) {
        if (relOffset.getX() != 0) return relOffset.getX() > 0;
        if (relOffset.getY() != 0) return relOffset.getY() > 0;
        return relOffset.getZ() > 0;
    }

    public void rotateConnections(Rotation rotation) {
        if (rotation == Rotation.NONE) return;
        applyRotationInPlace(rotation);
        setChanged();
        sendData();
        notifyModelChange();
    }

    public void mirrorConnections(Mirror mirror) {
        if (mirror == Mirror.NONE) return;
        applyMirrorInPlace(mirror);
        setChanged();
        sendData();
        notifyModelChange();
    }

    private void applyRotationInPlace(Rotation rotation) {
        Map<BlockPos, Integer> transformed = new HashMap<>();
        for (Map.Entry<BlockPos, Integer> entry : connections.entrySet()) {
            transformed.put(rotateOffset(entry.getKey(), rotation), entry.getValue());
        }
        connections.clear();
        connections.putAll(transformed);
    }

    private void applyMirrorInPlace(Mirror mirror) {
        Map<BlockPos, Integer> transformed = new HashMap<>();
        for (Map.Entry<BlockPos, Integer> entry : connections.entrySet()) {
            transformed.put(mirrorOffset(entry.getKey(), mirror), entry.getValue());
        }
        connections.clear();
        connections.putAll(transformed);
    }

    private static BlockPos rotateOffset(BlockPos offset, Rotation rotation) {
        return switch (rotation) {
            case NONE -> offset;
            case CLOCKWISE_90 -> new BlockPos(-offset.getZ(), offset.getY(), offset.getX());
            case CLOCKWISE_180 -> new BlockPos(-offset.getX(), offset.getY(), -offset.getZ());
            case COUNTERCLOCKWISE_90 -> new BlockPos(offset.getZ(), offset.getY(), -offset.getX());
        };
    }

    private static Rotation deduceRotation(Direction from, Direction to) {
        if (from.getAxis() == Direction.Axis.Y || to.getAxis() == Direction.Axis.Y) {
            return Rotation.NONE;
        }
        for (Rotation r : Rotation.values()) {
            if (r.rotate(from) == to) return r;
        }
        return Rotation.NONE;
    }

    private record DeducedTransform(Mirror mirror, Rotation rotation) {
        static final DeducedTransform NONE = new DeducedTransform(Mirror.NONE, Rotation.NONE);
    }

    private static DeducedTransform deduceFullTransform(Direction storedFacing, Direction currentFacing,
                                                         Direction storedRef, Direction currentRef) {
        for (Mirror m : Mirror.values()) {
            for (Rotation r : Rotation.values()) {
                if (r.rotate(m.mirror(storedFacing)) == currentFacing
                        && r.rotate(m.mirror(storedRef)) == currentRef) {
                    return new DeducedTransform(m, r);
                }
            }
        }
        return DeducedTransform.NONE;
    }

    private static BlockPos mirrorOffset(BlockPos offset, Mirror mirror) {
        return switch (mirror) {
            case NONE -> offset;
            case FRONT_BACK -> new BlockPos(-offset.getX(), offset.getY(), offset.getZ());
            case LEFT_RIGHT -> new BlockPos(offset.getX(), offset.getY(), -offset.getZ());
        };
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putInt("StoredFacing", getBlockState().getValue(GirderStrutBlock.FACING).get3DDataValue());
        tag.putInt("StoredRefFacing", getBlockState().getValue(GirderStrutBlock.REFERENCE_FACING).get3DDataValue());
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
        connectionRenderBufferCache = null;
        if (!connections.isEmpty() && tag.contains("StoredFacing")) {
            Direction storedFacing = Direction.from3DDataValue(tag.getInt("StoredFacing"));
            Direction currentFacing = getBlockState().getValue(GirderStrutBlock.FACING);
            boolean transformed = false;
            Direction storedRef = tag.contains("StoredRefFacing")
                    ? Direction.from3DDataValue(tag.getInt("StoredRefFacing"))
                    : Direction.NORTH;
            Direction currentRef = getBlockState().getValue(GirderStrutBlock.REFERENCE_FACING);
            DeducedTransform transform = deduceFullTransform(storedFacing, currentFacing, storedRef, currentRef);
            if (transform.mirror() != Mirror.NONE) {
                applyMirrorInPlace(transform.mirror());
                transformed = true;
            }
            if (transform.rotation() != Rotation.NONE) {
                applyRotationInPlace(transform.rotation());
                transformed = true;
            }
            if (transformed) {
                setChanged();
                notifyModelChange();
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
        connectionRenderBufferCache = null;
        if (level != null) {
            if (level.isClientSide) {
                requestModelDataUpdate();
            }
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}
