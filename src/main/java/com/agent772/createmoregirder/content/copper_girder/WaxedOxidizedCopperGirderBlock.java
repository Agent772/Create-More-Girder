package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class WaxedOxidizedCopperGirderBlock extends CopperGirderBlock {
    private static final int placementHelperId = PlacementHelpers.register(new WaxedOxidizedCopperGirderPlacementHelper());

    public WaxedOxidizedCopperGirderBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getEncasedShaftBlock() {
        return CMGBlocks.WAXED_OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT;
    }

    @Override
    protected int getPlacementHelperId() {
        return placementHelperId;
    }

    @Override
    public WeatherState getAge() {
        return WeatherState.OXIDIZED;
    }

    @Override
    protected Optional<Block> getHoneycombTransition(Block self) {
        return Optional.empty();
    }

    @Override
    protected Optional<Block> getAxeTransition(Block self) {
        return WeatheringCopperGirders.getUnwaxed(self);
    }

    @Override
    protected SoundEvent getAxeSound() {
        return SoundEvents.AXE_WAX_OFF;
    }
}
