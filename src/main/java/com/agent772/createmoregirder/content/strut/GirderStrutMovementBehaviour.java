package com.agent772.createmoregirder.content.strut;

import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.*;

public class GirderStrutMovementBehaviour implements MovementBehaviour {

    @Override
    public void startMoving(MovementContext context) {
        validateConnections(context);
    }

    private void validateConnections(MovementContext context) {
        if (context.blockEntityData == null) return;

        List<BlockPos> connections = readConnectionsFromNBT(context.blockEntityData);
        if (connections.isEmpty()) return;

        Map<BlockPos, StructureTemplate.StructureBlockInfo> contraptionBlocks = context.contraption.getBlocks();
        BlockPos myLocalPos = context.localPos;

        List<BlockPos> invalidConnections = new ArrayList<>();
        for (BlockPos relativeOffset : connections) {
            BlockPos otherLocalPos = myLocalPos.offset(relativeOffset);
            StructureTemplate.StructureBlockInfo otherInfo = contraptionBlocks.get(otherLocalPos);
            if (otherInfo == null || !(otherInfo.state().getBlock() instanceof GirderStrutBlock)) {
                invalidConnections.add(relativeOffset);
            }
        }

        if (!invalidConnections.isEmpty()) {
            removeInvalidConnections(context, invalidConnections);
        }
    }

    private void removeInvalidConnections(MovementContext context, List<BlockPos> invalidRelativeOffsets) {
        if (context.blockEntityData == null) return;

        ListTag connectionsList = context.blockEntityData.getList("Connections", Tag.TAG_COMPOUND);
        ListTag newList = new ListTag();

        int droppedCost = 0;
        for (Tag t : connectionsList) {
            if (t instanceof CompoundTag ct) {
                BlockPos rel = new BlockPos(ct.getInt("X"), ct.getInt("Y"), ct.getInt("Z"));
                if (invalidRelativeOffsets.contains(rel)) {
                    droppedCost += ct.contains("Cost") ? ct.getInt("Cost") : 1;
                } else {
                    newList.add(ct.copy());
                }
            }
        }

        context.blockEntityData.put("Connections", newList);

        boolean noConnectionsLeft = newList.isEmpty();

        BlockPos anchorPos = context.contraption.anchor;
        if (anchorPos != null && context.world != null) {
            BlockPos worldPos = anchorPos.offset(context.localPos);

            if (droppedCost > 0) {
                // Refund exactly the segments the player paid for when placing
                // the connection — same total as a normal in-world break.
                Block.popResource(context.world, worldPos, new ItemStack(context.state.getBlock(), droppedCost));
            }

            if (noConnectionsLeft) {
                // Anchor has no valid in-contraption partner left. Destroy it at
                // its current world position (still in the world at this point —
                // startMoving runs before Contraption#removeBlocksFromWorld).
                // Contraption#removeBlocksFromWorld will then see a block
                // mismatch at this position, call iterator.remove() on its
                // blocks map, and the anchor will neither be carried by the
                // contraption nor re-placed on disassemble.
                destroyOrphanAnchorAt(context.world, worldPos);
            }
        }

        cleanupWorldOrphans(context, invalidRelativeOffsets);
    }

    /**
     * Destroys an orphan anchor in the world without triggering its
     * {@code destroyConnectedStrut} cascade. The cascade would clobber the
     * partners' connections (which can include valid in-contraption neighbours)
     * via the in-world BE, so we clear the in-world BE's connection map first
     * and then remove the block.
     */
    private void destroyOrphanAnchorAt(Level world, BlockPos worldPos) {
        BlockEntity be = world.getBlockEntity(worldPos);
        if (be instanceof GirderStrutBlockEntity strutBe) {
            BlockPos beOrigin = strutBe.getBlockPos();
            for (BlockPos rel : strutBe.getConnectionsCopy()) {
                strutBe.removeConnection(beOrigin.offset(rel));
            }
        }
        world.removeBlock(worldPos, false);
    }

    private void cleanupWorldOrphans(MovementContext context, List<BlockPos> invalidRelativeOffsets) {
        Level world = context.world;
        if (world == null || world.isClientSide) return;

        BlockPos anchorPos = context.contraption.anchor;
        if (anchorPos == null) return;

        BlockPos myWorldPos = anchorPos.offset(context.localPos);

        for (BlockPos relativeOffset : invalidRelativeOffsets) {
            BlockPos orphanWorldPos = myWorldPos.offset(relativeOffset);
            BlockEntity be = world.getBlockEntity(orphanWorldPos);
            if (be instanceof GirderStrutBlockEntity orphanBe) {
                orphanBe.removeConnection(myWorldPos);
                if (orphanBe.connectionCount() == 0) {
                    // No item drop: the connection cost was already popped at
                    // the contraption-side anchor's worldPos above, and
                    // GirderStrutBlock#getDrops returns List.of() for an orphan
                    // with zero remaining connections and no cached cost — so
                    // destroyBlock(..., true) is a silent remove for the visual
                    // partner.
                    world.destroyBlock(orphanWorldPos, true);
                }
            }
        }
    }

    private List<BlockPos> readConnectionsFromNBT(CompoundTag beData) {
        if (beData == null || !beData.contains("Connections", Tag.TAG_LIST)) {
            return Collections.emptyList();
        }
        ListTag list = beData.getList("Connections", Tag.TAG_COMPOUND);
        List<BlockPos> result = new ArrayList<>();
        for (Tag t : list) {
            if (t instanceof CompoundTag ct) {
                result.add(new BlockPos(ct.getInt("X"), ct.getInt("Y"), ct.getInt("Z")));
            }
        }
        return result;
    }
}
