package com.agent772.createmoregirder.content.copycat_metal_girder;

import com.agent772.createmoregirder.content.copycat_strut.CopycatGirderStrutBlock;
import com.agent772.createmoregirder.content.strut.StrutModelType;

/**
 * Strut variant of the copycat metal girder. Reuses
 * {@link CopycatGirderStrutBlock} behaviour and only swaps the model type so
 * anchors and beam segments render with the metal-strut silhouette.
 */
public class CopycatMetalGirderStrutBlock extends CopycatGirderStrutBlock {
    public CopycatMetalGirderStrutBlock(Properties properties) {
        super(properties, StrutModelType.COPYCAT_METAL);
    }
}
