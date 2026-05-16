package com.agent772.createmoregirder.content.brass_metal_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.google.common.base.Predicates;
import com.simibubi.create.content.decoration.girder.GirderPlacementHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class BrassMetalGirderPlacementHelper extends GirderPlacementHelper {
    @Override
    public Predicate<BlockState> getStatePredicate() {
        return Predicates.or(CMGBlocks.BRASS_METAL_GIRDER::has, CMGBlocks.BRASS_METAL_GIRDER_ENCASED_SHAFT::has);
    }

    @Override
    public Predicate<ItemStack> getItemPredicate() {
        return CMGBlocks.BRASS_METAL_GIRDER::isIn;
    }
}
