package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class WaxedExposedCopperGirderBlock extends CopperGirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new WaxedExposedCopperGirderPlacementHelper());

    public WaxedExposedCopperGirderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return WeatheringCopper.WeatherState.EXPOSED;
    }

    @Override
    protected BlockState getEncasedShaftBlock() {
        return CMGBlocks.WAXED_EXPOSED_COPPER_GIRDER_ENCASED_SHAFT.getDefaultState();
    }

    @Override
    protected int getPlacementHelperId() {
        return placementHelperId;
    }

    @Override
    protected boolean isWaxed() {
        return true;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return false; // Waxed blocks don't weather
    }
}

