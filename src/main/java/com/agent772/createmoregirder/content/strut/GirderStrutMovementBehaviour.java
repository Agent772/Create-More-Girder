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

            if (noConnectionsLeft) {
                context.contraption.getBlocks().remove(context.localPos);
                Block.popResource(context.world, worldPos, new ItemStack(context.state.getBlock(), 1 + droppedCost));
            } else if (droppedCost > 0) {
                Block.popResource(context.world, worldPos, new ItemStack(context.state.getBlock(), droppedCost));
            }
        }

        cleanupWorldOrphans(context, invalidRelativeOffsets);
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
