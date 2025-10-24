package com.agent772.createmoregirder.content.weathered_iron_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.simibubi.create.content.decoration.girder.GirderPlacementHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class WeatheredIronGirderPlacementHelper extends GirderPlacementHelper {
    @Override
    public Predicate<BlockState> getStatePredicate() {
        return state -> state.getBlock() instanceof WeatheredIronGirderBlock;
    }

    @Override
    public Predicate<ItemStack> getItemPredicate() {
        return stack -> stack.getItem() == CMGBlocks.WEATHERED_IRON_GIRDER.asItem();
    }
}