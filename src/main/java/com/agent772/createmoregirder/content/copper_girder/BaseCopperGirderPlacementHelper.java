package com.agent772.createmoregirder.content.copper_girder;

import com.simibubi.create.content.decoration.girder.GirderPlacementHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Base placement helper for copper girder variants that accepts block class and item supplier.
 * This eliminates the need for separate placement helper classes for each variant.
 */
public class BaseCopperGirderPlacementHelper extends GirderPlacementHelper {
    private final Class<? extends Block> blockClass;
    private final Supplier<Item> itemSupplier;

    public BaseCopperGirderPlacementHelper(Class<? extends Block> blockClass, Supplier<Item> itemSupplier) {
        this.blockClass = blockClass;
        this.itemSupplier = itemSupplier;
    }

    @Override
    public Predicate<BlockState> getStatePredicate() {
        return state -> blockClass.isInstance(state.getBlock());
    }

    @Override
    public Predicate<ItemStack> getItemPredicate() {
        return stack -> stack.getItem() == itemSupplier.get();
    }
}

