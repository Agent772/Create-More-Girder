package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.CMGTags;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import com.simibubi.create.content.trains.track.TrackPaver;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TrackPaver.class, remap = false)
public abstract class TrackPaverMixin {

    @Redirect(
        method = "paveCurve",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/track/TrackPaver;isWallLike(Lnet/minecraft/world/level/block/state/BlockState;)Z")
    )
    private static boolean cmg$isWallLikeCurve(BlockState state) {
        if (state.is(CMGTags.PAVING_GIRDER)) return true;
        return state.getBlock() instanceof WallBlock || AllBlocks.METAL_GIRDER.has(state);
    }

    @Redirect(
        method = "paveStraight",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/track/TrackPaver;isWallLike(Lnet/minecraft/world/level/block/state/BlockState;)Z")
    )
    private static boolean cmg$isWallLikeStraight(BlockState state) {
        if (state.is(CMGTags.GIRDER_BLOCK)) return true;
        return state.getBlock() instanceof WallBlock || AllBlocks.METAL_GIRDER.has(state);
    }

    @Redirect(
        method = "paveCurve",
        at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 0)
    )
    private static boolean cmg$isGirder(BlockEntry<?> entry, BlockState state) {
        if (state.is(CMGTags.PAVING_GIRDER)) return true;
        return entry.has(state);
    }

    /**
     * Sets TOP bracket for CMG girders during straight track paving.
     * The base class {@code CMGGirderBlock.updateShape} preserves TOP once set,
     * so the neighbor cascade from placement cannot undo this.
     */
    @ModifyArg(
        method = "paveStraight",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/track/TrackPaver;placeBlockIfFree(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)Z"),
        index = 2
    )
    private static BlockState cmg$setTopBracketForPaving(BlockState state) {
        if (state.is(CMGTags.GIRDER_BLOCK)) {
            return state.setValue(GirderBlock.TOP, true);
        }
        return state;
    }

    /**
     * Same for girders placed during curve paving — straight sections
     * adjacent to curves also need the TOP bracket.
     */
    @ModifyArg(
        method = "paveCurve",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/track/TrackPaver;placeBlockIfFree(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)Z"),
        index = 2
    )
    private static BlockState cmg$setTopBracketForCurvePaving(BlockState state) {
        if (state.is(CMGTags.GIRDER_BLOCK)) {
            return state.setValue(GirderBlock.TOP, true);
        }
        return state;
    }
}
