package com.agent772.createmoregirder;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CMGCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateMoreGirder.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("main_tab",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + CreateMoreGirder.MOD_ID + ".main_tab"))
            .icon(() -> new ItemStack(CMGBlocks.BRASS_GIRDER.get()))
            .displayItems((pParameters, output) -> {
                output.accept(CMGBlocks.ANDESITE_GIRDER.get());
                output.accept(CMGBlocks.ANDESITE_GIRDER_STRUT.get());
                output.accept(CMGBlocks.BRASS_GIRDER.get());
                output.accept(CMGBlocks.BRASS_GIRDER_STRUT.get());
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
                output.accept(CMGBlocks.INDUSTRIAL_IRON_GIRDER.get());
                output.accept(CMGBlocks.INDUSTRIAL_IRON_GIRDER_STRUT.get());
                output.accept(CMGBlocks.WEATHERED_IRON_GIRDER.get());
                output.accept(CMGBlocks.WEATHERED_IRON_GIRDER_STRUT.get());
            })
            .build()
            
    );

    public static void register(IEventBus modEventBus) {
        CREATIVE_TABS.register(modEventBus);
    }
}
