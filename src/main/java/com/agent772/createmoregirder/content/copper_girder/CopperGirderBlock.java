package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.girder.CMGCopperGirderBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

/**
 * Concrete base copper girder, and the shared parent for every other plain
 * {@code copper_girder} variant. Supplies the {@code WeatheringCopperGirders}
 * weathering maps to the shared logic in {@link CMGCopperGirderBlock}.
 */
public class CopperGirderBlock extends CMGCopperGirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new CopperGirderPlacementHelper());

    public CopperGirderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getEncasedShaftBlock() {
        return CMGBlocks.COPPER_GIRDER_ENCASED_SHAFT;
    }

    @Override
    protected int getPlacementHelperId() {
        return placementHelperId;
    }

    @Override
    public WeatherState getAge() {
        return WeatherState.UNAFFECTED;
    }

    @Override
    protected Optional<Block> getHoneycombTransition(Block self) {
        return WeatheringCopperGirders.getWaxed(self);
    }

    @Override
    protected Optional<Block> getAxeTransition(Block self) {
        return WeatheringCopperGirders.getPrevious(self);
    }

    @Override
    protected Optional<Block> getNextWeatherBlock(Block self) {
        return WeatheringCopperGirders.getNext(self);
    }
}
