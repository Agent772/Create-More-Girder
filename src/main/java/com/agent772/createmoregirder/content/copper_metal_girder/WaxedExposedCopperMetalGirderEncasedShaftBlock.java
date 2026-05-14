package com.agent772.createmoregirder.content.copper_metal_girder;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.CMGBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class WaxedExposedCopperMetalGirderEncasedShaftBlock extends CopperMetalGirderEncasedShaftBlock {
    public WaxedExposedCopperMetalGirderEncasedShaftBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getGirderBlock() {
        return CMGBlocks.WAXED_EXPOSED_COPPER_METAL_GIRDER;
    }

    @Override
    protected BlockEntityType<? extends KineticBlockEntity> getEncasedBEType() {
        return CMGBlockEntityTypes.ENCASED_WAXED_EXPOSED_COPPER_METAL_GIRDER.get();
    }

    @Override
    public WeatherState getAge() {
        return WeatherState.EXPOSED;
    }

    @Override
    protected Optional<Block> getHoneycombTransition(Block self) {
        return Optional.empty();
    }

    @Override
    protected Optional<Block> getAxeTransition(Block self) {
        return WeatheringCopperMetalGirders.getUnwaxedEncasedShaft(self);
    }

    @Override
    protected SoundEvent getAxeSound() {
        return SoundEvents.AXE_WAX_OFF;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return false;
    }
}
