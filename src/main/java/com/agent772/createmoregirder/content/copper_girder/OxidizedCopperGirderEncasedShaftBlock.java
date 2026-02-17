package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.CMGBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class OxidizedCopperGirderEncasedShaftBlock extends CopperGirderEncasedShaftBlock {
    public OxidizedCopperGirderEncasedShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return WeatheringCopper.WeatherState.OXIDIZED;
    }

    @Override
    protected Block getGirderBlock() {
        return CMGBlocks.OXIDIZED_COPPER_GIRDER.get();
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return CMGBlockEntityTypes.ENCASED_OXIDIZED_COPPER_GIRDER.get();
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