package com.agent772.createmoregirder.content.strut;

import com.agent772.createmoregirder.config.CMGServerConfig;
import com.cake.struts.content.structure.ConnectionKey;
import com.cake.struts.content.structure.GirderStrutStructureShapes;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
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
    /** Relative offsets whose beam-collision shape is currently registered in the level-wide store. */
    private final Set<BlockPos> registeredShapeOffsets = new HashSet<>();
    /** Subset of {@link #registeredShapeOffsets} registered with a guessed peer facing (peer was unloaded). */
    private final Set<BlockPos> guessedFacingOffsets = new HashSet<>();
    private boolean needsCostMigration;
    private transient int cachedDropCost = -1;
    public @Nullable SuperByteBuffer connectionRenderBufferCache;
    public @Nullable SuperByteBuffer connectionOverlayRenderBufferCache;

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
        registerShape(relative);
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
                otherFace = myFace.getOpposite();
            }
        } else {
            otherFace = myFace.getOpposite();
        }
        return computeSegmentCost(level, getBlockPos(), myFace, otherAbsolute, otherFace);
    }

    public void removeConnection(BlockPos pos) {
        BlockPos relative = pos.subtract(getBlockPos());
        if (connections.remove(relative) != null) {
            unregisterShape(relative);
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

    public boolean canAcceptAdditionalConnection() {
        return connections.size() < CMGServerConfig.MAX_CONNECTIONS_PER_ANCHOR.get();
    }

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
        resyncShapes();
        setChanged();
        sendData();
        notifyModelChange();
    }

    public void mirrorConnections(Mirror mirror) {
        if (mirror == Mirror.NONE) return;
        applyMirrorInPlace(mirror);
        resyncShapes();
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

    private static BlockPos mirrorOffset(BlockPos offset, Mirror mirror) {
        return switch (mirror) {
            case NONE -> offset;
            case FRONT_BACK -> new BlockPos(-offset.getX(), offset.getY(), offset.getZ());
            case LEFT_RIGHT -> new BlockPos(offset.getX(), offset.getY(), -offset.getZ());
        };
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

    @Override
    public AABB getRenderBoundingBox() {
        return super.getRenderBoundingBox().inflate(GirderStrutBlock.MAX_SPAN + 2);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
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
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
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
        connectionOverlayRenderBufferCache = null;
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
        // Periodically re-assert beam-collision shapes the level-wide store has silently dropped
        // (mid-span structure block removed externally, or a peer anchor unloaded while this end stays
        // loaded). Also corrects connections first registered with a guessed peer facing once the peer
        // loads. healShapes() only re-registers what is actually missing, so steady state is cheap.
        if (level != null && !level.isClientSide && !connections.isEmpty()) {
            healShapes();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void onLoad() {
        super.onLoad();
        // Register beam-collision shapes as soon as the BE is added to a level, mirroring the library's
        // own StrutBlockEntity.onLoad. Runs on connections already populated by read() (load precedes
        // onLoad); placement adds its connection separately via addConnection.
        registerAllShapes();
    }

    @Override
    public void invalidate() {
        // Called on both chunk unload and block removal; drop our shapes so the level-wide store
        // (and the invisible structure blocks it manages) stay in sync. onLoad re-registers.
        unregisterAllShapes();
        super.invalidate();
    }

    private void registerAllShapes() {
        if (level == null || level.isClientSide) return;
        for (BlockPos relative : connections.keySet()) {
            registerShape(relative);
        }
    }

    private void unregisterAllShapes() {
        if (level == null || level.isClientSide || registeredShapeOffsets.isEmpty()) return;
        for (BlockPos relative : Set.copyOf(registeredShapeOffsets)) {
            unregisterShape(relative);
        }
    }

    private void resyncShapes() {
        if (level == null || level.isClientSide) return;
        unregisterAllShapes();
        registerAllShapes();
    }

    /**
     * Re-asserts only the connections the level-wide store has actually dropped, instead of blindly
     * re-registering everything each lazy tick. Called from {@link #lazyTick()}, so steady state is one
     * {@link GirderStrutStructureShapes#getConnectionsAt} lookup plus a key check per connection; a
     * re-register happens only when an entry is genuinely missing.
     *
     * <p>Two cases are healed: (1) the library silently drops a connection when a mid-span structure block
     * is removed externally (its recovery callback only fires for its own {@code StrutBlockEntity}, not
     * CMG's); and (2) a shared entry vanishes when the peer anchor unloads and unregisters it while this end
     * is still loaded and rendering the beam. A connection first registered with a guessed peer facing
     * (peer was unloaded) is also corrected here once the peer loads, since {@code registerConnection}
     * no-ops on an existing key and would otherwise keep the guessed geometry.
     */
    private void healShapes() {
        Set<ConnectionKey> present = GirderStrutStructureShapes.getConnectionsAt(level, getBlockPos());
        for (BlockPos relative : connections.keySet()) {
            BlockPos other = getBlockPos().offset(relative);
            if (!present.contains(new ConnectionKey(getBlockPos(), other))) {
                registerShape(relative);
            } else if (guessedFacingOffsets.contains(relative) && peerFacingResolvable(other)) {
                unregisterShape(relative);
                registerShape(relative);
            }
        }
    }

    private void registerShape(BlockPos relative) {
        if (level == null || level.isClientSide) return;
        if (!(getBlockState().getBlock() instanceof GirderStrutBlock block)) return;
        BlockPos other = getBlockPos().offset(relative);
        Direction myFacing = getBlockState().getValue(GirderStrutBlock.FACING);
        boolean resolvable = peerFacingResolvable(other);
        Direction otherFacing = resolvable
                ? level.getBlockState(other).getValue(GirderStrutBlock.FACING)
                : myFacing.getOpposite();
        if (resolvable) {
            guessedFacingOffsets.remove(relative);
        } else {
            guessedFacingOffsets.add(relative);
        }
        GirderStrutStructureShapes.registerConnection(
                level, getBlockPos(), myFacing, other, otherFacing, block.getModelType().toCollisionModelType());
        registeredShapeOffsets.add(relative);
    }

    private void unregisterShape(BlockPos relative) {
        guessedFacingOffsets.remove(relative);
        if (level == null || level.isClientSide || !registeredShapeOffsets.remove(relative)) return;
        GirderStrutStructureShapes.unregisterConnection(level, getBlockPos(), getBlockPos().offset(relative));
    }

    private boolean peerFacingResolvable(BlockPos otherAbsolute) {
        return level != null && level.isLoaded(otherAbsolute)
                && level.getBlockState(otherAbsolute).getBlock() instanceof GirderStrutBlock;
    }

    private void notifyModelChange() {
        connectionRenderBufferCache = null;
        connectionOverlayRenderBufferCache = null;
        if (level != null) {
            if (level.isClientSide) {
                requestModelDataUpdate();
            }
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}
