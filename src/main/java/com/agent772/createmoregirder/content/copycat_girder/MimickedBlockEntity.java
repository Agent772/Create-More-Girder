package com.agent772.createmoregirder.content.copycat_girder;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Common contract for block entities that store a mimicked block state for the
 * copycat girder family (regular and encased-shaft variants).
 */
public interface MimickedBlockEntity {

    BlockState getMimickedState();

    boolean hasMimickedState();

    int getFaceRotation();
}
