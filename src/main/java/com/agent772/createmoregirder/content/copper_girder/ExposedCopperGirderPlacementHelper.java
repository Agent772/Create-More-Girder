package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;

public class ExposedCopperGirderPlacementHelper extends BaseCopperGirderPlacementHelper {
    public ExposedCopperGirderPlacementHelper() {
        super(ExposedCopperGirderBlock.class, () -> CMGBlocks.EXPOSED_COPPER_GIRDER.asItem());
    }
}

