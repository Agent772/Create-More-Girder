package com.agent772.createmoregirder.content.brass_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.simibubi.create.content.decoration.girder.GirderPlacementHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class BrassGirderPlacementHelper extends GirderPlacementHelper {
    @Override
    public Predicate<BlockState> getStatePredicate() {
        return state -> state.getBlock() instanceof BrassGirderBlock;
    }

    @Override
    public Predicate<ItemStack> getItemPredicate() {
        return stack -> stack.getItem() == CMGBlocks.BRASS_GIRDER.asItem();
    }
}