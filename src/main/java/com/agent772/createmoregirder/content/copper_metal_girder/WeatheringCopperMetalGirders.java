package com.agent772.createmoregirder.content.copper_metal_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;

import java.util.Optional;
import java.util.function.Supplier;

public class WeatheringCopperMetalGirders {
    public static final Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() ->
            ImmutableBiMap.<Block, Block>builder()
                    .put(CMGBlocks.COPPER_METAL_GIRDER.get(), CMGBlocks.EXPOSED_COPPER_METAL_GIRDER.get())
                    .put(CMGBlocks.EXPOSED_COPPER_METAL_GIRDER.get(), CMGBlocks.WEATHERED_COPPER_METAL_GIRDER.get())
                    .put(CMGBlocks.WEATHERED_COPPER_METAL_GIRDER.get(), CMGBlocks.OXIDIZED_COPPER_METAL_GIRDER.get())
                    .build());

    public static final Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> NEXT_BY_BLOCK.get().inverse());

    public static final Supplier<BiMap<Block, Block>> WAXABLES = Suppliers.memoize(() ->
            ImmutableBiMap.<Block, Block>builder()
                    .put(CMGBlocks.COPPER_METAL_GIRDER.get(), CMGBlocks.WAXED_COPPER_METAL_GIRDER.get())
                    .put(CMGBlocks.EXPOSED_COPPER_METAL_GIRDER.get(), CMGBlocks.WAXED_EXPOSED_COPPER_METAL_GIRDER.get())
                    .put(CMGBlocks.WEATHERED_COPPER_METAL_GIRDER.get(), CMGBlocks.WAXED_WEATHERED_COPPER_METAL_GIRDER.get())
                    .put(CMGBlocks.OXIDIZED_COPPER_METAL_GIRDER.get(), CMGBlocks.WAXED_OXIDIZED_COPPER_METAL_GIRDER.get())
                    .build());

    public static final Supplier<BiMap<Block, Block>> UNWAXABLES = Suppliers.memoize(() -> WAXABLES.get().inverse());

    public static final Supplier<BiMap<Block, Block>> NEXT_ENCASED_SHAFT = Suppliers.memoize(() ->
            ImmutableBiMap.<Block, Block>builder()
                    .put(CMGBlocks.COPPER_METAL_GIRDER_ENCASED_SHAFT.get(), CMGBlocks.EXPOSED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get())
                    .put(CMGBlocks.EXPOSED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get(), CMGBlocks.WEATHERED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get())
                    .put(CMGBlocks.WEATHERED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get(), CMGBlocks.OXIDIZED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get())
                    .build());

    public static final Supplier<BiMap<Block, Block>> PREVIOUS_ENCASED_SHAFT = Suppliers.memoize(() -> NEXT_ENCASED_SHAFT.get().inverse());

    public static final Supplier<BiMap<Block, Block>> WAXABLES_ENCASED_SHAFT = Suppliers.memoize(() ->
            ImmutableBiMap.<Block, Block>builder()
                    .put(CMGBlocks.COPPER_METAL_GIRDER_ENCASED_SHAFT.get(), CMGBlocks.WAXED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get())
                    .put(CMGBlocks.EXPOSED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get(), CMGBlocks.WAXED_EXPOSED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get())
                    .put(CMGBlocks.WEATHERED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get(), CMGBlocks.WAXED_WEATHERED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get())
                    .put(CMGBlocks.OXIDIZED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get(), CMGBlocks.WAXED_OXIDIZED_COPPER_METAL_GIRDER_ENCASED_SHAFT.get())
                    .build());

    public static final Supplier<BiMap<Block, Block>> UNWAXABLES_ENCASED_SHAFT = Suppliers.memoize(() -> WAXABLES_ENCASED_SHAFT.get().inverse());

    public static Optional<Block> getNext(Block block) {
        return Optional.ofNullable(NEXT_BY_BLOCK.get().get(block));
    }

    public static Optional<Block> getPrevious(Block block) {
        return Optional.ofNullable(PREVIOUS_BY_BLOCK.get().get(block));
    }

    public static Optional<Block> getWaxed(Block block) {
        return Optional.ofNullable(WAXABLES.get().get(block));
    }

    public static Optional<Block> getUnwaxed(Block block) {
        return Optional.ofNullable(UNWAXABLES.get().get(block));
    }

    public static Optional<Block> getNextEncasedShaft(Block block) {
        return Optional.ofNullable(NEXT_ENCASED_SHAFT.get().get(block));
    }

    public static Optional<Block> getPreviousEncasedShaft(Block block) {
        return Optional.ofNullable(PREVIOUS_ENCASED_SHAFT.get().get(block));
    }

    public static Optional<Block> getWaxedEncasedShaft(Block block) {
        return Optional.ofNullable(WAXABLES_ENCASED_SHAFT.get().get(block));
    }

    public static Optional<Block> getUnwaxedEncasedShaft(Block block) {
        return Optional.ofNullable(UNWAXABLES_ENCASED_SHAFT.get().get(block));
    }

    public static WeatheringCopper.WeatherState getAge(Block block) {
        if (block == CMGBlocks.COPPER_METAL_GIRDER.get() || block == CMGBlocks.WAXED_COPPER_METAL_GIRDER.get()) {
            return WeatheringCopper.WeatherState.UNAFFECTED;
        } else if (block == CMGBlocks.EXPOSED_COPPER_METAL_GIRDER.get() || block == CMGBlocks.WAXED_EXPOSED_COPPER_METAL_GIRDER.get()) {
            return WeatheringCopper.WeatherState.EXPOSED;
        } else if (block == CMGBlocks.WEATHERED_COPPER_METAL_GIRDER.get() || block == CMGBlocks.WAXED_WEATHERED_COPPER_METAL_GIRDER.get()) {
            return WeatheringCopper.WeatherState.WEATHERED;
        } else if (block == CMGBlocks.OXIDIZED_COPPER_METAL_GIRDER.get() || block == CMGBlocks.WAXED_OXIDIZED_COPPER_METAL_GIRDER.get()) {
            return WeatheringCopper.WeatherState.OXIDIZED;
        }
        return WeatheringCopper.WeatherState.UNAFFECTED;
    }
}
