package com.agent772.createmoregirder.content.iron_metal_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.girder.CMGGirderBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class IronMetalGirderBlock extends CMGGirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new IronMetalGirderPlacementHelper());

    public IronMetalGirderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getEncasedShaftBlock() {
        return CMGBlocks.IRON_METAL_GIRDER_ENCASED_SHAFT;
    }

    @Override
    protected int getPlacementHelperId() {
        return placementHelperId;
    }
}
