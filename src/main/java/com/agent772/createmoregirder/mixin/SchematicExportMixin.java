package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.content.strut.GirderStrutBlock;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.schematics.SchematicExport;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Strips incomplete strut connections from schematics at save time.
 * When a schematic boundary cuts through a strut, orphaned anchors whose
 * partner is outside the captured region are removed entirely — no anchor
 * block, no material list entry, no placement.
 */
@Mixin(value = SchematicExport.class)
public abstract class SchematicExportMixin {

    @WrapOperation(method = "saveSchematic",
            at = @At(value = "INVOKE",
                    target = "Lcom/simibubi/create/content/schematics/SchematicAndQuillItem;replaceStructureVoidWithAir(Lnet/minecraft/nbt/CompoundTag;)V", remap = false),
            remap = false)
    private static void cmg$pruneIncompleteStrutsBeforeVoidReplace(CompoundTag tag, Operation<Void> original) {
        cmg$pruneIncompleteStruts(tag);
        original.call(tag);
    }

    @Unique
    private static void cmg$pruneIncompleteStruts(CompoundTag schematicTag) {
        ListTag palette = cmg$resolvePalette(schematicTag);
        if (palette == null) return;

        Set<Integer> strutIndices = new HashSet<>();
        for (int i = 0; i < palette.size(); i++) {
            CompoundTag entry = palette.getCompound(i);
            String name = entry.getString("Name");
            ResourceLocation blockId = ResourceLocation.tryParse(name);
            if (blockId != null) {
                Optional<Block> block = BuiltInRegistries.BLOCK.getOptional(blockId);
                if (block.isPresent() && block.get() instanceof GirderStrutBlock) {
                    strutIndices.add(i);
                }
            }
        }
        if (strutIndices.isEmpty()) return;

        ListTag blocks = schematicTag.getList("blocks", Tag.TAG_COMPOUND);

        // Map template-local positions to block-list indices for strut blocks only
        Set<BlockPos> strutPositions = new HashSet<>();
        Map<BlockPos, Integer> strutPosToIndex = new HashMap<>();

        for (int i = 0; i < blocks.size(); i++) {
            CompoundTag block = blocks.getCompound(i);
            int stateIdx = block.getInt("state");
            BlockPos pos = cmg$readPos(block);
            if (strutIndices.contains(stateIdx)) {
                strutPositions.add(pos);
                strutPosToIndex.put(pos, i);
            }
        }

        List<Integer> toRemove = new ArrayList<>();

        for (Map.Entry<BlockPos, Integer> entry : strutPosToIndex.entrySet()) {
            BlockPos myPos = entry.getKey();
            int blockIdx = entry.getValue();
            CompoundTag block = blocks.getCompound(blockIdx);

            if (!block.contains("nbt", Tag.TAG_COMPOUND)) continue;
            CompoundTag nbt = block.getCompound("nbt");
            if (!nbt.contains("Connections", Tag.TAG_LIST)) continue;

            ListTag connections = nbt.getList("Connections", Tag.TAG_COMPOUND);
            if (connections.isEmpty()) continue;

            ListTag validConnections = new ListTag();
            for (Tag ct : connections) {
                if (ct instanceof CompoundTag conn) {
                    BlockPos relOffset = new BlockPos(conn.getInt("X"), conn.getInt("Y"), conn.getInt("Z"));
                    BlockPos partnerPos = myPos.offset(relOffset);
                    if (strutPositions.contains(partnerPos)) {
                        validConnections.add(conn.copy());
                    }
                }
            }

            if (validConnections.size() < connections.size()) {
                if (validConnections.isEmpty()) {
                    toRemove.add(blockIdx);
                } else {
                    nbt.put("Connections", validConnections);
                }
            }
        }

        toRemove.sort(Collections.reverseOrder());
        for (int idx : toRemove) {
            blocks.remove(idx);
        }
    }

    @Unique
    private static ListTag cmg$resolvePalette(CompoundTag tag) {
        if (tag.contains("palette", Tag.TAG_LIST)) {
            return tag.getList("palette", Tag.TAG_COMPOUND);
        }
        if (tag.contains("palettes", Tag.TAG_LIST)) {
            ListTag palettes = tag.getList("palettes", Tag.TAG_LIST);
            if (!palettes.isEmpty()) {
                return palettes.getList(0);
            }
        }
        return null;
    }

    @Unique
    private static BlockPos cmg$readPos(CompoundTag block) {
        ListTag posTag = block.getList("pos", Tag.TAG_INT);
        return new BlockPos(posTag.getInt(0), posTag.getInt(1), posTag.getInt(2));
    }
}
