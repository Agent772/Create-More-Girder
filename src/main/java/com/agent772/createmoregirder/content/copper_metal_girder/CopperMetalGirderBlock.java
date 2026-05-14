package com.agent772.createmoregirder.content.copper_metal_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.girder.CMGCopperGirderBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Optional;

/**
 * Concrete base OG-style copper metal girder, and the shared parent for every
 * other {@code copper_metal_girder} variant. Supplies the
 * {@code WeatheringCopperMetalGirders} weathering maps to the shared logic in
 * {@link CMGCopperGirderBlock}.
 */
public class CopperMetalGirderBlock extends CMGCopperGirderBlock {

    private static final int placementHelperId =
            PlacementHelpers.register(new CopperMetalGirderPlacementHelper());

    public CopperMetalGirderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getEncasedShaftBlock() {
        return CMGBlocks.COPPER_METAL_GIRDER_ENCASED_SHAFT;
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
        return WeatheringCopperMetalGirders.getWaxed(self);
    }

    @Override
    protected Optional<Block> getAxeTransition(Block self) {
        return WeatheringCopperMetalGirders.getPrevious(self);
    }

    @Override
    protected Optional<Block> getNextWeatherBlock(Block self) {
        return WeatheringCopperMetalGirders.getNext(self);
    }
}
