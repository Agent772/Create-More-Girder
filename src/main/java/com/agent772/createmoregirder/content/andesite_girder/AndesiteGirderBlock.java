package com.agent772.createmoregirder.content.andesite_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.girder.CMGGirderBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.Block;

public class AndesiteGirderBlock extends CMGGirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new AndesiteGirderPlacementHelper());

    public AndesiteGirderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getEncasedShaftBlock() {
        return CMGBlocks.ANDESITE_GIRDER_ENCASED_SHAFT;
    }

    @Override
    protected int getPlacementHelperId() {
        return placementHelperId;
    }
}
