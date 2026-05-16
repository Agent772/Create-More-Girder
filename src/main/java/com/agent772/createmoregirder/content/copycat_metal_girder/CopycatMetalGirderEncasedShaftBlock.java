package com.agent772.createmoregirder.content.copycat_metal_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.copycat_girder.CopycatGirderEncasedShaftBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.Block;

/**
 * Encased-shaft form of the copycat metal girder. Reuses
 * {@link CopycatGirderEncasedShaftBlock} behaviour and only overrides the
 * variant-specific hooks so it reverts to the metal-girder silhouette.
 */
public class CopycatMetalGirderEncasedShaftBlock extends CopycatGirderEncasedShaftBlock {
    private static final int placementHelperId = PlacementHelpers.register(new CopycatMetalGirderPlacementHelper());

    public CopycatMetalGirderEncasedShaftBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getGirderBlock() {
        return CMGBlocks.COPYCAT_METAL_GIRDER;
    }

    @Override
    protected int getPlacementHelperId() {
        return placementHelperId;
    }
}
