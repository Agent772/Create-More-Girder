package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.CMGTags;
import com.agent772.createmoregirder.content.bracket.CopycatBracketAccess;

import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.InteractionResult;

/**
 * Sneak-wrenching a host that owns a copycat bracket destroys the whole
 * block. The default flow leaves the bracket + mimic to be popped by
 * {@code AbstractSimpleShaftBlock#onRemove}, which pops on the ground
 * regardless of game mode. For copycat brackets only, we capture the
 * bracket + mimic before destruction and route them through the player's
 * inventory, skipping creative. Non-copycat brackets fall through to
 * Create's default ground-pop behaviour untouched.
 */
@Mixin(value = IWrenchable.class, remap = false)
public interface IWrenchableMixin {

    @Inject(method = "onSneakWrenched", at = @At("HEAD"))
    default void cmg$captureBracketBeforeDestroy(BlockState state, UseOnContext context,
                                                 CallbackInfoReturnable<InteractionResult> cir) {
        Level world = context.getLevel();
        if (world.isClientSide) return;
        BlockPos pos = context.getClickedPos();
        BracketedBlockEntityBehaviour beh =
            BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);
        if (beh == null || !beh.isBracketPresent()) return;

        BlockState bracket = beh.getBracket();
        if (bracket == null || !bracket.is(CMGTags.COPYCAT_BRACKET_BLOCK)) return;

        ItemStack bracketStack = new ItemStack(bracket.getBlock());

        // removeBracket(false) follows the wrench path — my BBEB mixin stashes
        // the mimic for retrieval instead of popping it on the ground.
        beh.removeBracket(false);

        ItemStack mimicStack = beh instanceof CopycatBracketAccess access
            ? access.cmg$consumePendingMimicReturn()
            : ItemStack.EMPTY;

        Player player = context.getPlayer();
        if (player == null) {
            if (!bracketStack.isEmpty()) Block.popResource(world, pos, bracketStack);
            if (!mimicStack.isEmpty()) Block.popResource(world, pos, mimicStack);
            return;
        }
        if (player.isCreative()) return;
        if (!bracketStack.isEmpty()) player.getInventory().placeItemBackInInventory(bracketStack);
        if (!mimicStack.isEmpty()) player.getInventory().placeItemBackInInventory(mimicStack);
    }
}
