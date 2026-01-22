package com.agent772.createmoregirder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Data Components for Girder Struts
 * 
 * Stores anchor position and direction for 2-step placement
 */
public class CMGDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = 
        DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, CreateMoreGirder.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> STRUT_ANCHOR_POS = 
        DATA_COMPONENTS.register("strut_anchor_pos",
            () -> DataComponentType.<BlockPos>builder()
                .persistent(BlockPos.CODEC)
                .networkSynchronized(BlockPos.STREAM_CODEC)
                .build()
        );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Direction>> STRUT_ANCHOR_DIR = 
        DATA_COMPONENTS.register("strut_anchor_dir",
            () -> DataComponentType.<Direction>builder()
                .persistent(Direction.CODEC)
                .networkSynchronized(Direction.STREAM_CODEC)
                .build()
        );

    public static void register() {
        // Called to ensure static initialization
    }
}
