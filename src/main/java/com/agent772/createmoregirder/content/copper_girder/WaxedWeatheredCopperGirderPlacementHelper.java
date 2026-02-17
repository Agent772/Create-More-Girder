package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;

public class WaxedWeatheredCopperGirderPlacementHelper extends BaseCopperGirderPlacementHelper {
    public WaxedWeatheredCopperGirderPlacementHelper() {
        super(WaxedWeatheredCopperGirderBlock.class, () -> CMGBlocks.WAXED_WEATHERED_COPPER_GIRDER.asItem());
    }
}

