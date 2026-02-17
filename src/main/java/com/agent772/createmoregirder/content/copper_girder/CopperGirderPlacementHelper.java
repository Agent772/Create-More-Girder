package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;

public class CopperGirderPlacementHelper extends BaseCopperGirderPlacementHelper {
    public CopperGirderPlacementHelper() {
        super(CopperGirderBlock.class, () -> CMGBlocks.COPPER_GIRDER.asItem());
    }
}

