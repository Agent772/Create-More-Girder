package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.content.copycat_girder.CopycatGirderBlock;
import com.simibubi.create.content.trains.track.TrackPaver;
import com.simibubi.create.content.trains.track.TrackPlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * Create's paveTracks rejects blocks that implement EntityBlock.
 * CopycatGirderBlock has a block entity (via IBE) so it's rejected.
 * This mixin handles copycat girders by calling paveStraight directly,
 * bypassing the EntityBlock check while skipping paveCurve (copycat
 * girders should not be placed on curves).
 */
@Mixin(value = TrackPlacement.class)
public abstract class TrackPlacementMixin {

    @Inject(method = "paveTracks", at = @At("HEAD"), cancellable = true, remap = false)
    private static void cmg$bypassEntityBlockCheckForCopycat(
            Level level, TrackPlacement.PlacementInfo info, BlockItem blockItem,
            boolean dryRun, CallbackInfo ci
    ) {
        Block block = blockItem.getBlock();
        if (!(block instanceof CopycatGirderBlock))
            return;

        PlacementInfoAccessor acc = (PlacementInfoAccessor) info;
        Set<BlockPos> posSet = new HashSet<>();
        info.requiredPavement = 0;

        for (int i = 0; i < 2; i++) {
            boolean firstEnd = i == 0;
            int extent = firstEnd ? acc.getEnd1Extent() : acc.getEnd2Extent();
            if (acc.getCurve() != null) extent++;
            Vec3 axis = firstEnd ? acc.getAxis1() : acc.getAxis2();
            BlockPos pos = firstEnd ? acc.getPos1() : acc.getPos2();
            info.requiredPavement += TrackPaver.paveStraight(
                    level, pos.below(), axis, extent, block, dryRun, posSet);
        }
        // Intentionally skip paveCurve — copycat girders are not placed on curves
        ci.cancel();
    }
}
