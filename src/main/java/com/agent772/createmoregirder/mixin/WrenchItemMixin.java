package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.content.bracket.CopycatBracketAccess;

import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.equipment.wrench.WrenchItem;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Sneak-wrenching a Create host (shaft / cog / pipe) is a pickup, not a
 * destroy: Create's {@link IWrenchable#onSneakWrenched} places the host
 * itself into the player's inventory and then calls
 * {@code Level.destroyBlock(pos, false)}. That destroy path runs the host's
 * {@code onRemove}, which pops any attached bracket on the ground regardless
 * of game mode — and the BBEB mimic mixin pops the copycat mimic on the
 * ground too. Both should follow the host into the player's inventory in
 * survival, or be discarded in creative.
 *
 * Mixin 0.8.5 on Forge 1.20.1 does not support {@code @Inject} on interface
 * default methods, so we cannot mix into {@code IWrenchable} directly.
 * Instead we intercept at the head of {@link WrenchItem#useOn}: when the
 * player is sneaking and the clicked block is an {@code IWrenchable} that
 * owns a bracket, capture the bracket (and any stashed copycat mimic)
 * before Create's dispatch to {@code onSneakWrenched} so the destroy that
 * follows can no longer pop them on the ground.
 *
 * Non-sneak wrench taps fall through untouched. The pickaxe / hand path is
 * handled separately by {@link BlockBracketDropMixin}.
 */
@Mixin(WrenchItem.class)
public abstract class WrenchItemMixin {

    @Inject(method = "useOn", at = @At("HEAD"))
    private void cmg$captureBracketBeforeSneakWrench(UseOnContext context,
                                                     CallbackInfoReturnable<InteractionResult> cir) {
        Player player = context.getPlayer();
        if (player == null || !player.mayBuild()) return;
        if (!player.isShiftKeyDown()) return;

        Level world = context.getLevel();
        if (world.isClientSide) return;

        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof IWrenchable)) return;

        BracketedBlockEntityBehaviour beh =
            BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);
        if (beh == null || !beh.isBracketPresent()) return;

        BlockState bracket = beh.getBracket();
        if (bracket == null) return;

        ItemStack bracketStack = new ItemStack(bracket.getBlock());

        // removeBracket(false) follows the wrench path — the BBEB mixin stashes
        // the copycat mimic for retrieval instead of popping it on the ground.
        beh.removeBracket(false);

        ItemStack mimicStack = beh instanceof CopycatBracketAccess access
            ? access.cmg$consumePendingMimicReturn()
            : ItemStack.EMPTY;

        if (player.isCreative()) return;
        // placeItemBackInInventory pops on the ground if the inventory is full.
        if (!bracketStack.isEmpty()) player.getInventory().placeItemBackInInventory(bracketStack);
        if (!mimicStack.isEmpty()) player.getInventory().placeItemBackInInventory(mimicStack);
    }
}
