package com.agent772.createmoregirder;

import java.util.function.UnaryOperator;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Girder Strut Block implementation
 * 
 * Adapted from Bits-n-Bobs by Industrialists-Of-Create
 * Original: https://github.com/Industrialists-Of-Create/Bits-n-Bobs
 * Licensed under MIT License
 * 
 * Modifications:
 * - Adapted for Create: More Girder mod structure
 */

public class CMGDataComponents {
    
    private static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CreateMoreGirder.MODID);

    public static final DataComponentType<BlockPos> GIRDER_STRUT_FROM = register(
            "girder_strut_from",
            builder -> builder.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC)
    );

    public static final DataComponentType<Direction> GIRDER_STRUT_FROM_FACE = register(
            "girder_strut_from_face",
            builder -> builder.persistent(Direction.CODEC).networkSynchronized(Direction.STREAM_CODEC)
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
        DATA_COMPONENTS.register(name, () -> type);
        return type;
    }

    @ApiStatus.Internal
    public static void register(IEventBus modEventBus) {
        DATA_COMPONENTS.register(modEventBus);
    }
}
