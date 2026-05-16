package com.agent772.createmoregirder.content.copper_metal_girder;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.girder.CMGCopperGirderEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Optional;

/**
 * Concrete base OG-style copper metal girder encased shaft, and the shared
 * parent for every other {@code copper_metal_girder} encased-shaft variant.
 * Supplies the {@code WeatheringCopperMetalGirders} encased-shaft weathering
 * maps to the shared logic in {@link CMGCopperGirderEncasedShaftBlock}.
 */
public class CopperMetalGirderEncasedShaftBlock extends CMGCopperGirderEncasedShaftBlock {

    public CopperMetalGirderEncasedShaftBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getGirderBlock() {
        return CMGBlocks.COPPER_METAL_GIRDER;
    }

    @Override
    protected BlockEntityType<? extends KineticBlockEntity> getEncasedBEType() {
        return CMGBlockEntityTypes.ENCASED_COPPER_METAL_GIRDER.get();
    }

    @Override
    public WeatherState getAge() {
        return WeatherState.UNAFFECTED;
    }

    @Override
    protected Optional<Block> getHoneycombTransition(Block self) {
        return WeatheringCopperMetalGirders.getWaxedEncasedShaft(self);
    }

    @Override
    protected Optional<Block> getAxeTransition(Block self) {
        return WeatheringCopperMetalGirders.getPreviousEncasedShaft(self);
    }

    @Override
    protected Optional<Block> getNextWeatherBlock(Block self) {
        return WeatheringCopperMetalGirders.getNextEncasedShaft(self);
    }
}
