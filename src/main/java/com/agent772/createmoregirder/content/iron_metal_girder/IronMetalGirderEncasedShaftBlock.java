package com.agent772.createmoregirder.content.iron_metal_girder;

import com.agent772.createmoregirder.CMGBlockEntityTypes;
import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.content.girder.CMGGirderEncasedShaftBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class IronMetalGirderEncasedShaftBlock extends CMGGirderEncasedShaftBlock {
    public IronMetalGirderEncasedShaftBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntry<? extends Block> getGirderBlock() {
        return CMGBlocks.IRON_METAL_GIRDER;
    }

    @Override
    protected BlockEntityType<? extends KineticBlockEntity> getEncasedBEType() {
        return CMGBlockEntityTypes.ENCASED_IRON_METAL_GIRDER.get();
    }
}
