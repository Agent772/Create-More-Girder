package com.agent772.createmoregirder;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CMGCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = 
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateMoreGirder.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("main_tab",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + CreateMoreGirder.MODID + ".main_tab"))
            .icon(() -> new ItemStack(CMGBlocks.BRASS_GIRDER.get()))
            .displayItems((pParameters, output) -> {
                output.accept(CMGBlocks.ANDESITE_GIRDER.get());
                output.accept(CMGBlocks.ANDESITE_GIRDER_STRUT.get());
                output.accept(CMGBlocks.ANDESITE_METAL_GIRDER.get());
                output.accept(CMGBlocks.ANDESITE_METAL_GIRDER_STRUT.get());
                output.accept(CMGBlocks.BRASS_GIRDER.get());
                output.accept(CMGBlocks.BRASS_GIRDER_STRUT.get());
                output.accept(CMGBlocks.BRASS_METAL_GIRDER.get());
                output.accept(CMGBlocks.BRASS_METAL_GIRDER_STRUT.get());
                output.accept(CMGBlocks.COPPER_GIRDER.get());
                output.accept(CMGBlocks.EXPOSED_COPPER_GIRDER.get());
                output.accept(CMGBlocks.WEATHERED_COPPER_GIRDER.get());
                output.accept(CMGBlocks.OXIDIZED_COPPER_GIRDER.get());
                output.accept(CMGBlocks.WAXED_COPPER_GIRDER.get());
                output.accept(CMGBlocks.WAXED_COPPER_GIRDER_STRUT.get());
                output.accept(CMGBlocks.WAXED_EXPOSED_COPPER_GIRDER.get());
                output.accept(CMGBlocks.WAXED_EXPOSED_COPPER_GIRDER_STRUT.get());
                output.accept(CMGBlocks.WAXED_WEATHERED_COPPER_GIRDER.get());
                output.accept(CMGBlocks.WAXED_WEATHERED_COPPER_GIRDER_STRUT.get());
                output.accept(CMGBlocks.WAXED_OXIDIZED_COPPER_GIRDER.get());
                output.accept(CMGBlocks.WAXED_OXIDIZED_COPPER_GIRDER_STRUT.get());
                output.accept(CMGBlocks.COPPER_METAL_GIRDER.get());
                output.accept(CMGBlocks.EXPOSED_COPPER_METAL_GIRDER.get());
                output.accept(CMGBlocks.WEATHERED_COPPER_METAL_GIRDER.get());
                output.accept(CMGBlocks.OXIDIZED_COPPER_METAL_GIRDER.get());
                output.accept(CMGBlocks.WAXED_COPPER_METAL_GIRDER.get());
                output.accept(CMGBlocks.WAXED_COPPER_METAL_GIRDER_STRUT.get());
                output.accept(CMGBlocks.WAXED_EXPOSED_COPPER_METAL_GIRDER.get());
                output.accept(CMGBlocks.WAXED_EXPOSED_COPPER_METAL_GIRDER_STRUT.get());
                output.accept(CMGBlocks.WAXED_WEATHERED_COPPER_METAL_GIRDER.get());
                output.accept(CMGBlocks.WAXED_WEATHERED_COPPER_METAL_GIRDER_STRUT.get());
                output.accept(CMGBlocks.WAXED_OXIDIZED_COPPER_METAL_GIRDER.get());
                output.accept(CMGBlocks.WAXED_OXIDIZED_COPPER_METAL_GIRDER_STRUT.get());
                output.accept(CMGBlocks.INDUSTRIAL_IRON_GIRDER.get());
                output.accept(CMGBlocks.INDUSTRIAL_IRON_GIRDER_STRUT.get());
                output.accept(CMGBlocks.WEATHERED_IRON_GIRDER.get());
                output.accept(CMGBlocks.WEATHERED_IRON_GIRDER_STRUT.get());
                output.accept(CMGBlocks.WEATHERED_IRON_METAL_GIRDER.get());
                output.accept(CMGBlocks.WEATHERED_IRON_METAL_GIRDER_STRUT.get());
                if (!ModList.get().isLoaded("bits_n_bobs")) {
                    output.accept(CMGBlocks.CREATE_METAL_GIRDER_STRUT.get());
                }
                output.accept(CMGBlocks.COPYCAT_GIRDER.get());
                output.accept(CMGBlocks.COPYCAT_GIRDER_STRUT.get());
                output.accept(CMGBlocks.COPYCAT_METAL_GIRDER.get());
                output.accept(CMGBlocks.COPYCAT_METAL_GIRDER_STRUT.get());
            })
            .build()
            
    );

    public static void register(IEventBus modEventBus) {
        CREATIVE_TABS.register(modEventBus);
    }
}
