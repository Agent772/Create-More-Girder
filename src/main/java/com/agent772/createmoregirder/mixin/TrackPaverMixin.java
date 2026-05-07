package com.agent772.createmoregirder.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import com.simibubi.create.content.trains.track.TrackPaver;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TrackPaver.class, remap = false)
public abstract class TrackPaverMixin {

    @Redirect(
        method = "paveCurve",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/track/TrackPaver;isWallLike(Lnet/minecraft/world/level/block/state/BlockState;)Z")
    )
    private static boolean cmg$isWallLikeCurve(BlockState state) {
        if (state.getBlock() instanceof GirderBlock) return true;
        return state.getBlock() instanceof WallBlock || AllBlocks.METAL_GIRDER.has(state);
    }

    @Redirect(
        method = "paveStraight",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/track/TrackPaver;isWallLike(Lnet/minecraft/world/level/block/state/BlockState;)Z")
    )
    private static boolean cmg$isWallLikeStraight(BlockState state) {
        if (state.getBlock() instanceof GirderBlock) return true;
        return state.getBlock() instanceof WallBlock || AllBlocks.METAL_GIRDER.has(state);
    }

    @Redirect(
        method = "paveCurve",
        at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 0)
    )
    private static boolean cmg$isGirder(BlockEntry<?> entry, BlockState state) {
        if (state.getBlock() instanceof GirderBlock) return true;
        return entry.has(state);
    }
}
