package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class WaxedCopperGirderBlock extends CopperGirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new WaxedCopperGirderPlacementHelper());

    public WaxedCopperGirderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return WeatheringCopper.WeatherState.UNAFFECTED;
    }

    @Override
    protected BlockState getEncasedShaftBlock() {
        return CMGBlocks.WAXED_COPPER_GIRDER_ENCASED_SHAFT.getDefaultState();
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

