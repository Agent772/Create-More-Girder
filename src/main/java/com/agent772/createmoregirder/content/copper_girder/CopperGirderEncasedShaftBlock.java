package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.girder.CMGCopperGirderEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Optional;

/**
 * Concrete base copper girder encased shaft, and the shared parent for every
 * other plain {@code copper_girder} encased-shaft variant. Supplies the
 * {@code WeatheringCopperGirders} encased-shaft weathering maps to the shared
 * logic in {@link CMGCopperGirderEncasedShaftBlock}.
 */
public class CopperGirderEncasedShaftBlock extends CMGCopperGirderEncasedShaftBlock {
    public CopperGirderEncasedShaftBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getGirderBlock() {
        return CMGBlocks.COPPER_GIRDER;
    }

    @Override
    protected BlockEntityType<? extends KineticBlockEntity> getEncasedBEType() {
        return CMGBlockEntityTypes.ENCASED_COPPER_GIRDER.get();
    }

    @Override
    public WeatherState getAge() {
        return WeatherState.UNAFFECTED;
    }

    @Override
    protected Optional<Block> getHoneycombTransition(Block self) {
        return WeatheringCopperGirders.getWaxedEncasedShaft(self);
    }

    @Override
    protected Optional<Block> getAxeTransition(Block self) {
        return WeatheringCopperGirders.getPreviousEncasedShaft(self);
    }

    @Override
    protected Optional<Block> getNextWeatherBlock(Block self) {
        return WeatheringCopperGirders.getNextEncasedShaft(self);
    }
}
