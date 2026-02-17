package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;

public class WaxedCopperGirderPlacementHelper extends BaseCopperGirderPlacementHelper {
    public WaxedCopperGirderPlacementHelper() {
        super(WaxedCopperGirderBlock.class, () -> CMGBlocks.WAXED_COPPER_GIRDER.asItem());
    }
}

