package com.agent772.createmoregirder.mixin;

import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.content.trains.track.TrackPlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = TrackPlacement.PlacementInfo.class, remap = false)
public interface PlacementInfoAccessor {
    @Accessor("curve")
    BezierConnection getCurve();

    @Accessor("end1Extent")
    int getEnd1Extent();

    @Accessor("end2Extent")
    int getEnd2Extent();

    @Accessor("axis1")
    Vec3 getAxis1();

    @Accessor("axis2")
    Vec3 getAxis2();

    @Accessor("pos1")
    BlockPos getPos1();

    @Accessor("pos2")
    BlockPos getPos2();
}
