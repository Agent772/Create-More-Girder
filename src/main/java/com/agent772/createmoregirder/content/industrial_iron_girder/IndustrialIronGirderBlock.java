package com.agent772.createmoregirder.content.industrial_iron_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.girder.CMGGirderBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.Block;

public class IndustrialIronGirderBlock extends CMGGirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new IndustrialIronGirderPlacementHelper());

    public IndustrialIronGirderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getEncasedShaftBlock() {
        return CMGBlocks.INDUSTRIAL_IRON_GIRDER_ENCASED_SHAFT;
    }

    @Override
    protected int getPlacementHelperId() {
        return placementHelperId;
    }
}
