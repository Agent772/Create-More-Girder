package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.content.bracket.CopycatBracketAccess;

import com.simibubi.create.content.decoration.bracket.BracketBlockItem;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BracketBlockItem.class, remap = false)
public class BracketBlockItemMixin {

    @Inject(method = "useOn", at = @At("RETURN"))
    private void cmg$returnMimicAfterApply(UseOnContext context,
                                           CallbackInfoReturnable<InteractionResult> cir) {
        if (cir.getReturnValue() != InteractionResult.SUCCESS) return;
        Level world = context.getLevel();
        if (world.isClientSide) return;
        BlockPos pos = context.getClickedPos();
        BracketedBlockEntityBehaviour beh =
            BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);
        if (!(beh instanceof CopycatBracketAccess access)) return;
        ItemStack drop = access.cmg$consumePendingMimicReturn();
        if (drop.isEmpty()) return;
        Player player = context.getPlayer();
        if (player == null) {
            Block.popResource(world, pos, drop);
            return;
        }
        if (player.isCreative()) return;
        player.getInventory().placeItemBackInInventory(drop);
    }
}
