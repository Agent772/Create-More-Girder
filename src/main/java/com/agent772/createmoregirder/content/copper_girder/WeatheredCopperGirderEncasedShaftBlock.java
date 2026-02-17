package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.CMGBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WeatheredCopperGirderEncasedShaftBlock extends CopperGirderEncasedShaftBlock {
    public WeatheredCopperGirderEncasedShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public WeatheringCopper.WeatherState getAge() {
        return WeatheringCopper.WeatherState.WEATHERED;
    }

    @Override
    protected Block getGirderBlock() {
        return CMGBlocks.WEATHERED_COPPER_GIRDER.get();
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return CMGBlockEntityTypes.ENCASED_WEATHERED_COPPER_GIRDER.get();
    }
}

