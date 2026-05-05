package com.agent772.createmoregirder.mixin;

import com.agent772.createmoregirder.CMGBezierData;
import com.agent772.createmoregirder.CMGPartialModels;
import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.foundation.render.SpecialModels;
import dev.engine_room.flywheel.api.instance.InstanceType;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.instance.InstancerProvider;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Couple;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.simibubi.create.content.trains.track.TrackVisual$BezierTrackVisual$GirderVisual", remap = false)
public class GirderVisualMixin {

    @Shadow
    @Final
    private Couple<TransformedInstance[]> beams;

    @Unique
    private BezierConnection cmg$bc;

    @Unique
    private InstancerProvider cmg$provider;

    @Unique
    @SuppressWarnings("rawtypes")
    private InstanceType cmg$instanceType;

    @Redirect(
        method = "<init>(Lcom/simibubi/create/content/trains/track/TrackVisual$BezierTrackVisual;Lcom/simibubi/create/content/trains/track/BezierConnection;)V",
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/track/BezierConnection;getSegmentCount()I")
    )
    private int cmg$captureSegmentCount(BezierConnection bc) {
        this.cmg$bc = bc;
        return bc.getSegmentCount();
    }

    @Redirect(
        method = {
            "<init>(Lcom/simibubi/create/content/trains/track/TrackVisual$BezierTrackVisual;Lcom/simibubi/create/content/trains/track/BezierConnection;)V",
            "lambda$new$3(Lnet/createmod/catnip/data/Couple;Ljava/lang/Boolean;)V"
        },
        at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/render/SpecialModels;flatChunk(Ldev/engine_room/flywheel/lib/model/baked/PartialModel;)Ldev/engine_room/flywheel/api/model/Model;")
    )
    private Model cmg$swapModel(PartialModel original) {
        if (cmg$bc != null) {
            Block girder = ((CMGBezierData) cmg$bc).cmg$getGirderBlock();
            if (girder != null) {
                PartialModel cmgModel = CMGPartialModels.getSegmentModel(girder, original);
                if (cmgModel != null) return SpecialModels.flatChunk(cmgModel);
            }
        }
        return SpecialModels.flatChunk(original);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Redirect(
        method = "<init>(Lcom/simibubi/create/content/trains/track/TrackVisual$BezierTrackVisual;Lcom/simibubi/create/content/trains/track/BezierConnection;)V",
        at = @At(value = "INVOKE", target = "Ldev/engine_room/flywheel/api/instance/InstancerProvider;instancer(Ldev/engine_room/flywheel/api/instance/InstanceType;Ldev/engine_room/flywheel/api/model/Model;)Ldev/engine_room/flywheel/api/instance/Instancer;")
    )
    private Instancer cmg$captureProvider(InstancerProvider provider, InstanceType type, Model model) {
        this.cmg$provider = provider;
        this.cmg$instanceType = type;
        return provider.instancer(type, model);
    }

    @SuppressWarnings("unchecked")
    @Inject(
        method = "<init>(Lcom/simibubi/create/content/trains/track/TrackVisual$BezierTrackVisual;Lcom/simibubi/create/content/trains/track/BezierConnection;)V",
        at = @At("RETURN")
    )
    private void cmg$alternateBeamInstances(CallbackInfo ci) {
        if (cmg$bc == null || cmg$provider == null) return;

        Block girder = ((CMGBezierData) cmg$bc).cmg$getGirderBlock();
        if (girder == null) return;

        PartialModel alt = CMGPartialModels.getAltMiddleModel(girder);
        if (alt == null) return;

        Instancer<TransformedInstance> altInstancer = cmg$provider.instancer(
            cmg$instanceType, SpecialModels.flatChunk(alt));

        beams.forEach(array -> {
            for (int i = 1; i < array.length; i += 2) {
                altInstancer.stealInstance(array[i]);
            }
        });
    }
}
