package com.agent772.createmoregirder.mixin;

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
 * Sneak-wrenching a host is a pickup, not a destroy: the shaft / pipe goes
 * into the player's inventory (Create's own code), and its attached bracket
 * (plus any copycat mimic) should follow the same route. The default flow,
 * however, leaves the bracket to be popped on the ground by the host's
 * {@code onRemove} regardless of game mode.
 *
 * We intercept at the head of {@code onSneakWrenched} for any bracket-bearing
 * host: capture the bracket (and any stashed mimic) before destruction and
 * route them via the player's inventory in survival, or discard in creative.
 * Hosts without a bracket fall through untouched.
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
        if (bracket == null) return;

        ItemStack bracketStack = new ItemStack(bracket.getBlock());

        // removeBracket(false) follows the wrench path — my BBEB mixin stashes
        // the copycat mimic for retrieval instead of popping it on the ground.
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
