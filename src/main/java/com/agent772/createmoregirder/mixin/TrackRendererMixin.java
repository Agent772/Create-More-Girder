package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.CMGBezierData;
import com.agent772.createmoregirder.CMGPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.content.trains.track.TrackRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TrackRenderer.class, remap = false)
public class TrackRendererMixin {

    @Unique
    private static final ThreadLocal<Block> cmg$currentGirder = new ThreadLocal<>();

    @Unique
    private static final ThreadLocal<int[]> cmg$middleCallCounter = ThreadLocal.withInitial(() -> new int[]{0});

    @Inject(method = "renderGirder", at = @At("HEAD"))
    private static void cmg$captureGirder(Level level, BezierConnection bc, PoseStack ms,
                                          VertexConsumer vb, BlockPos tePosition, CallbackInfo ci) {
        cmg$currentGirder.set(((CMGBezierData) bc).cmg$getGirderBlock());
        cmg$middleCallCounter.get()[0] = 0;
    }

    @Inject(method = "renderGirder", at = @At("RETURN"))
    private static void cmg$clearGirder(Level level, BezierConnection bc, PoseStack ms,
                                        VertexConsumer vb, BlockPos tePosition, CallbackInfo ci) {
        cmg$currentGirder.remove();
        cmg$middleCallCounter.remove();
    }

    @Redirect(
        method = "renderGirder",
        at = @At(value = "INVOKE", target = "Lnet/createmod/catnip/render/CachedBuffers;partial(Ldev/engine_room/flywheel/lib/model/baked/PartialModel;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/createmod/catnip/render/SuperByteBuffer;")
    )
    private static SuperByteBuffer cmg$swapRendererModel(PartialModel original, BlockState state) {
        Block girder = cmg$currentGirder.get();
        if (girder == null) return CachedBuffers.partial(original, state);

        if (original == AllPartialModels.GIRDER_SEGMENT_MIDDLE) {
            // 2 partial() calls per segment (first=true, first=false); flip on every other segment
            int callIndex = cmg$middleCallCounter.get()[0]++;
            boolean useAlt = (callIndex / 2) % 2 != 0;
            PartialModel model = useAlt
                ? CMGPartialModels.getAltMiddleModel(girder)
                : CMGPartialModels.getSegmentModel(girder, original);
            if (model != null) return CachedBuffers.partial(model, state);
        } else {
            PartialModel cmgModel = CMGPartialModels.getSegmentModel(girder, original);
            if (cmgModel != null) return CachedBuffers.partial(cmgModel, state);
        }

        return CachedBuffers.partial(original, state);
    }
}
