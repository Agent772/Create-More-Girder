package com.agent772.createmoregirder;

import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public interface CMGBezierData {
    @Nullable
    Block cmg$getGirderBlock();

    void cmg$setGirderBlock(@Nullable Block block);
}
