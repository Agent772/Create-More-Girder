package com.agent772.createmoregirder.content.copper_metal_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WeatheredCopperMetalGirderBlock extends CopperMetalGirderBlock {
    private static final int placementHelperId =
            PlacementHelpers.register(new WeatheredCopperMetalGirderPlacementHelper());

    public WeatheredCopperMetalGirderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getEncasedShaftBlock() {
        return CMGBlocks.WEATHERED_COPPER_METAL_GIRDER_ENCASED_SHAFT;
    }

    @Override
    protected int getPlacementHelperId() {
        return placementHelperId;
    }

    @Override
    public WeatherState getAge() {
        return WeatherState.WEATHERED;
    }
}
