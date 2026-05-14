package com.agent772.createmoregirder.content.brass_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.girder.CMGGirderBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.Block;

public class BrassGirderBlock extends CMGGirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new BrassGirderPlacementHelper());

    public BrassGirderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getEncasedShaftBlock() {
        return CMGBlocks.BRASS_GIRDER_ENCASED_SHAFT;
    }

    @Override
    protected int getPlacementHelperId() {
        return placementHelperId;
    }
}
