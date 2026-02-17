package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;

public class WaxedOxidizedCopperGirderPlacementHelper extends BaseCopperGirderPlacementHelper {
    public WaxedOxidizedCopperGirderPlacementHelper() {
        super(WaxedOxidizedCopperGirderBlock.class, () -> CMGBlocks.WAXED_OXIDIZED_COPPER_GIRDER.asItem());
    }
}

