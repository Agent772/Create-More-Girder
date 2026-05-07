package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.CMGBezierData;
import com.simibubi.create.content.trains.track.BezierConnection;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BezierConnection.class, remap = false)
public abstract class BezierConnectionDropMixin implements CMGBezierData {

    @Redirect(
        method = "addItemsToPlayer",
        at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/BlockEntry;asStack(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 0)
    )
    private ItemStack cmg$replaceGirderStackForPlayer(BlockEntry<?> entry, int count) {
        Block cmgBlock = cmg$getGirderBlock();
        if (cmgBlock != null) {
            return new ItemStack(cmgBlock, count);
        }
        return entry.asStack(count);
    }

    @Redirect(
        method = "spawnItems",
        at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/BlockEntry;asStack()Lnet/minecraft/world/item/ItemStack;", ordinal = 0)
    )
    private ItemStack cmg$replaceGirderStackForWorld(BlockEntry<?> entry) {
        Block cmgBlock = cmg$getGirderBlock();
        if (cmgBlock != null) {
            return new ItemStack(cmgBlock);
        }
        return entry.asStack();
    }
}
