package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;

public class WeatheredCopperGirderPlacementHelper extends BaseCopperGirderPlacementHelper {
    public WeatheredCopperGirderPlacementHelper() {
        super(WeatheredCopperGirderBlock.class, () -> CMGBlocks.WEATHERED_COPPER_GIRDER.asItem());
    }
}

