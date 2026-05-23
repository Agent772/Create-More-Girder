package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.content.bracket.CopycatBracketAccess;

import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.equipment.wrench.IWrenchableWithBracket;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * After the wrench has removed a (potentially copycat) bracket from the host,
 * deposit any stashed mimic block into the player's inventory — but skip in
 * creative mode, matching the existing bracket-return behaviour in
 * {@link IWrenchableWithBracket#tryRemoveBracket}.
 */
@Mixin(value = IWrenchableWithBracket.class, remap = false)
public interface IWrenchableWithBracketMixin {

    @Inject(method = "tryRemoveBracket", at = @At("RETURN"))
    default void cmg$returnMimicToPlayer(UseOnContext context, CallbackInfoReturnable<Boolean> cir) {
        if (!Boolean.TRUE.equals(cir.getReturnValue())) return;
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
            net.minecraft.world.level.block.Block.popResource(world, pos, drop);
            return;
        }
        if (player.isCreative()) return;
        player.getInventory().placeItemBackInInventory(drop);
    }
}
