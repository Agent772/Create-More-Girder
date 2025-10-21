package com.agent772.createmoregirder.content.copper_girder;

import com.agent772.createmoregirder.CMGBlocks;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;

import java.util.Optional;
import java.util.function.Supplier;

public class WeatheringCopperGirders {
    public static final Supplier<BiMap<Block, Block>> NEXT_BY_BLOCK = Suppliers.memoize(() -> {
        return ImmutableBiMap.<Block, Block>builder()
                .put(CMGBlocks.COPPER_GIRDER.get(), CMGBlocks.EXPOSED_COPPER_GIRDER.get())
                .put(CMGBlocks.EXPOSED_COPPER_GIRDER.get(), CMGBlocks.WEATHERED_COPPER_GIRDER.get())
                .put(CMGBlocks.WEATHERED_COPPER_GIRDER.get(), CMGBlocks.OXIDIZED_COPPER_GIRDER.get())
                .build();
    });

    public static final Supplier<BiMap<Block, Block>> PREVIOUS_BY_BLOCK = Suppliers.memoize(() -> {
        return NEXT_BY_BLOCK.get().inverse();
    });

    // Waxing maps
    public static final Supplier<BiMap<Block, Block>> WAXABLES = Suppliers.memoize(() -> {
        return ImmutableBiMap.<Block, Block>builder()
                .put(CMGBlocks.COPPER_GIRDER.get(), CMGBlocks.WAXED_COPPER_GIRDER.get())
                .put(CMGBlocks.EXPOSED_COPPER_GIRDER.get(), CMGBlocks.WAXED_EXPOSED_COPPER_GIRDER.get())
                .put(CMGBlocks.WEATHERED_COPPER_GIRDER.get(), CMGBlocks.WAXED_WEATHERED_COPPER_GIRDER.get())
                .put(CMGBlocks.OXIDIZED_COPPER_GIRDER.get(), CMGBlocks.WAXED_OXIDIZED_COPPER_GIRDER.get())
                .build();
    });

    public static final Supplier<BiMap<Block, Block>> UNWAXABLES = Suppliers.memoize(() -> {
        return WAXABLES.get().inverse();
    });

    // Encased shaft weathering maps
    public static final Supplier<BiMap<Block, Block>> NEXT_ENCASED_SHAFT = Suppliers.memoize(() -> {
        return ImmutableBiMap.<Block, Block>builder()
                .put(CMGBlocks.COPPER_GIRDER_ENCASED_SHAFT.get(), CMGBlocks.EXPOSED_COPPER_GIRDER_ENCASED_SHAFT.get())
                .put(CMGBlocks.EXPOSED_COPPER_GIRDER_ENCASED_SHAFT.get(), CMGBlocks.WEATHERED_COPPER_GIRDER_ENCASED_SHAFT.get())
                .put(CMGBlocks.WEATHERED_COPPER_GIRDER_ENCASED_SHAFT.get(), CMGBlocks.OXIDIZED_COPPER_GIRDER_ENCASED_SHAFT.get())
                .build();
    });

    public static final Supplier<BiMap<Block, Block>> PREVIOUS_ENCASED_SHAFT = Suppliers.memoize(() -> {
        return NEXT_ENCASED_SHAFT.get().inverse();
    });

    // Encased shaft waxing maps (only basic copper for now since others aren't registered)
    public static final Supplier<BiMap<Block, Block>> WAXABLES_ENCASED_SHAFT = Suppliers.memoize(() -> {
        return ImmutableBiMap.<Block, Block>builder()
                .put(CMGBlocks.COPPER_GIRDER_ENCASED_SHAFT.get(), CMGBlocks.WAXED_COPPER_GIRDER_ENCASED_SHAFT.get())
                .build();
    });

    public static final Supplier<BiMap<Block, Block>> UNWAXABLES_ENCASED_SHAFT = Suppliers.memoize(() -> {
        return WAXABLES_ENCASED_SHAFT.get().inverse();
    });

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

    // Encased shaft methods
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

    public static Block getFirst(Block block) {
        Block current = block;
        while (getPrevious(current).isPresent()) {
            current = getPrevious(current).get();
        }
        return current;
    }

    public static WeatheringCopper.WeatherState getAge(Block block) {
        if (block == CMGBlocks.COPPER_GIRDER.get()) {
            return WeatheringCopper.WeatherState.UNAFFECTED;
        } else if (block == CMGBlocks.EXPOSED_COPPER_GIRDER.get()) {
            return WeatheringCopper.WeatherState.EXPOSED;
        } else if (block == CMGBlocks.WEATHERED_COPPER_GIRDER.get()) {
            return WeatheringCopper.WeatherState.WEATHERED;
        } else if (block == CMGBlocks.OXIDIZED_COPPER_GIRDER.get()) {
            return WeatheringCopper.WeatherState.OXIDIZED;
        }
        return WeatheringCopper.WeatherState.UNAFFECTED;
    }
}