package com.agent772.createmoregirder.content.copycat_metal_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.copycat_girder.CopycatGirderBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.Block;

/**
 * Copycat variant that mimics adjacent block textures on the OG
 * {@code _metal_girder} silhouette. Reuses {@link CopycatGirderBlock} behaviour
 * and only overrides the variant-specific hooks.
 */
public class CopycatMetalGirderBlock extends CopycatGirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new CopycatMetalGirderPlacementHelper());

    public CopycatMetalGirderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getEncasedShaftBlock() {
        return CMGBlocks.COPYCAT_METAL_GIRDER_ENCASED_SHAFT;
    }

    @Override
    protected int getPlacementHelperId() {
        return placementHelperId;
    }
}
