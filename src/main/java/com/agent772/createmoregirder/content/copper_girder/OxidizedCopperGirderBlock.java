package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class OxidizedCopperGirderBlock extends CopperGirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new OxidizedCopperGirderPlacementHelper());

    public OxidizedCopperGirderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return WeatheringCopper.WeatherState.OXIDIZED;
    }

    @Override
    protected BlockState getEncasedShaftBlock() {
        return CMGBlocks.OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT.getDefaultState();
    }

    @Override
    protected int getPlacementHelperId() {
        return placementHelperId;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return false; // Oxidized is the final stage, no further weathering
    }

    @Override
    public Optional<BlockState> getNext(BlockState state) {
        return Optional.empty(); // Oxidized is the final stage
    }
}

