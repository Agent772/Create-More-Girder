package com.agent772.createmoregirder.content.copper_metal_girder;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.CMGBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ExposedCopperMetalGirderEncasedShaftBlock extends CopperMetalGirderEncasedShaftBlock {
    public ExposedCopperMetalGirderEncasedShaftBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getGirderBlock() {
        return CMGBlocks.EXPOSED_COPPER_METAL_GIRDER;
    }

    @Override
    protected BlockEntityType<? extends KineticBlockEntity> getEncasedBEType() {
        return CMGBlockEntityTypes.ENCASED_EXPOSED_COPPER_METAL_GIRDER.get();
    }

    @Override
    public WeatherState getAge() {
        return WeatherState.EXPOSED;
    }
}
