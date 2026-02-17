package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;

public class OxidizedCopperGirderPlacementHelper extends BaseCopperGirderPlacementHelper {
    public OxidizedCopperGirderPlacementHelper() {
        super(OxidizedCopperGirderBlock.class, () -> CMGBlocks.OXIDIZED_COPPER_GIRDER.asItem());
    }
}

