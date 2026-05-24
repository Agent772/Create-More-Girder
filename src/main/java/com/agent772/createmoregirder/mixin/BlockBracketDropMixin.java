package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.content.bracket.CopycatBracketAccess;

import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.fluids.pipes.AxisPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractSimpleShaftBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * When a creative-mode player breaks a Create shaft / cog / fluid-pipe host
 * with a pickaxe, the host's {@code onRemove} unconditionally pops the
 * attached bracket on the ground, and the BBEB mimic mixin pops the copycat
 * mimic on the ground too. Creative breaks should leave nothing behind.
 *
 * We intercept at the head of {@link Block#playerWillDestroy} (which fires
 * for the pickaxe / hand break path but NOT for sneak-wrench, since
 * {@code IWrenchable#onSneakWrenched} uses {@code Level.destroyBlock} which
 * skips {@code playerWillDestroy}) and, when the destroyer is in creative,
 * pre-clear the bracket and consume the stashed mimic so nothing pops.
 *
 * Survival pickaxe breaks fall through untouched: Create's own onRemove
 * still pops the bracket on the ground and the BBEB mimic mixin still pops
 * the copycat mimic, matching the rule that "destroying with pickaxe / hand
 * should pop both host + bracket (+ mimic) into the world".
 *
 * Non-player destruction (explosions, pistons, {@code /setblock}) does not
 * reach {@code playerWillDestroy} either, so it also keeps Create's vanilla
 * ground-pop behaviour.
 */
@Mixin(Block.class)
public abstract class BlockBracketDropMixin {

    @Inject(
        method = "playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)V",
        at = @At("HEAD"))
    private void cmg$suppressBracketDropInCreative(Level world, BlockPos pos, BlockState state, Player player,
                                                   CallbackInfo ci) {
        if (world.isClientSide) return;
        if (player == null || !player.isCreative()) return;
        Block block = state.getBlock();
        if (!(block instanceof AbstractSimpleShaftBlock
            || block instanceof AxisPipeBlock
            || block instanceof FluidPipeBlock)) return;

        BracketedBlockEntityBehaviour beh =
            BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);
        if (beh == null || !beh.isBracketPresent()) return;
        if (beh.getBracket() == null) return;

        // removeBracket(false) takes the wrench path — BBEB mixin stashes
        // the copycat mimic for retrieval instead of popping it on the ground.
        beh.removeBracket(false);

        // Consume and discard the mimic stash so it doesn't leak into a later
        // operation on the same block entity.
        if (beh instanceof CopycatBracketAccess access) {
            access.cmg$consumePendingMimicReturn();
        }
        // The bracket itself is now gone — Create's onRemove will see an
        // empty Optional and pop nothing.
    }
}
