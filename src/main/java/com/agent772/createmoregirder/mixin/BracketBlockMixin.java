package com.agent772.createmoregirder.mixin;

import java.util.Optional;

import com.simibubi.create.content.decoration.bracket.BracketBlock;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractSimpleShaftBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Vanilla Create selects the {@code COG} bracket variant via
 * {@code blockState.getBlock() instanceof CogWheelBlock}. Third-party cogs
 * (TFMG, Diesel Generators, ...) extend {@code AbstractSimpleShaftBlock} and
 * implement {@link ICogWheel} but are NOT instances of Create's concrete
 * {@code CogWheelBlock}, so Create falls through to the SHAFT bracket — visually
 * wrong on a cog.
 */
@Mixin(value = BracketBlock.class, remap = false)
public abstract class BracketBlockMixin {

    @Inject(
        method = "getSuitableBracket(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Ljava/util/Optional;",
        at = @At("HEAD"), cancellable = true)
    private void cmg$pickCogBracketForThirdPartyCogs(BlockState blockState, Direction direction,
                                                     CallbackInfoReturnable<Optional<BlockState>> cir) {
        Block block = blockState.getBlock();
        if (!(block instanceof AbstractSimpleShaftBlock)) return;
        if (block instanceof CogWheelBlock) return;
        if (!ICogWheel.isDedicatedCogWheel(block)) return;

        Axis targetAxis = blockState.getValue(RotatedPillarKineticBlock.AXIS);
        Axis axis = direction.getAxis();
        if (targetAxis == axis) {
            cir.setReturnValue(Optional.empty());
            return;
        }
        boolean alongFirst = axis != Axis.Z ? targetAxis == Axis.Z : targetAxis == Axis.Y;
        BlockState bracket = ((BracketBlock) (Object) this).defaultBlockState()
            .setValue(BracketBlock.TYPE, BracketBlock.BracketType.COG)
            .setValue(DirectionalBlock.FACING, direction)
            .setValue(BracketBlock.AXIS_ALONG_FIRST_COORDINATE, !alongFirst);
        cir.setReturnValue(Optional.of(bracket));
    }
}
