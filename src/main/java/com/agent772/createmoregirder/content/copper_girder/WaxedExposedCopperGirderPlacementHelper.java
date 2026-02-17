package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;

public class WaxedExposedCopperGirderPlacementHelper extends BaseCopperGirderPlacementHelper {
    public WaxedExposedCopperGirderPlacementHelper() {
        super(WaxedExposedCopperGirderBlock.class, () -> CMGBlocks.WAXED_EXPOSED_COPPER_GIRDER.asItem());
    }
}

