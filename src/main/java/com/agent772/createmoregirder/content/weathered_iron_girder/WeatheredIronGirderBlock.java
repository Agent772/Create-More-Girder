package com.agent772.createmoregirder.content.weathered_iron_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.girder.CMGGirderBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.Block;

public class WeatheredIronGirderBlock extends CMGGirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new WeatheredIronGirderPlacementHelper());

    public WeatheredIronGirderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getEncasedShaftBlock() {
        return CMGBlocks.WEATHERED_IRON_GIRDER_ENCASED_SHAFT;
    }

    @Override
    protected int getPlacementHelperId() {
        return placementHelperId;
    }
}
