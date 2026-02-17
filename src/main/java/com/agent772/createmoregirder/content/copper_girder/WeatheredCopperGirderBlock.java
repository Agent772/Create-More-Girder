package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheredCopperGirderBlock extends CopperGirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new WeatheredCopperGirderPlacementHelper());

    public WeatheredCopperGirderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return WeatheringCopper.WeatherState.WEATHERED;
    }

    @Override
    protected BlockState getEncasedShaftBlock() {
        return CMGBlocks.WEATHERED_COPPER_GIRDER_ENCASED_SHAFT.getDefaultState();
    }

    @Override
    protected int getPlacementHelperId() {
        return placementHelperId;
    }
}

