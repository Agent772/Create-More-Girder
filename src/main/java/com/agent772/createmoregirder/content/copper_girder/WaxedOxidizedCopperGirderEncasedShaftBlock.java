package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.CMGBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Optional;

public class WaxedOxidizedCopperGirderEncasedShaftBlock extends CopperGirderEncasedShaftBlock {
    public WaxedOxidizedCopperGirderEncasedShaftBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getGirderBlock() {
        return CMGBlocks.WAXED_OXIDIZED_COPPER_GIRDER;
    }

    @Override
    protected BlockEntityType<? extends KineticBlockEntity> getEncasedBEType() {
        return CMGBlockEntityTypes.ENCASED_WAXED_OXIDIZED_COPPER_GIRDER.get();
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
        return WeatheringCopperGirders.getUnwaxedEncasedShaft(self);
    }

    @Override
    protected SoundEvent getAxeSound() {
        return SoundEvents.AXE_WAX_OFF;
    }
}
