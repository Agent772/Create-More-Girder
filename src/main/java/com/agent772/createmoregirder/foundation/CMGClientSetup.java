package com.agent772.createmoregirder.foundation;

import com.agent772.createmoregirder.CMGBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class CMGClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.ANDESITE_GIRDER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.BRASS_GIRDER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.COPPER_GIRDER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.EXPOSED_COPPER_GIRDER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.WEATHERED_COPPER_GIRDER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.OXIDIZED_COPPER_GIRDER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.WAXED_COPPER_GIRDER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.WAXED_EXPOSED_COPPER_GIRDER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.WAXED_WEATHERED_COPPER_GIRDER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.WAXED_OXIDIZED_COPPER_GIRDER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.INDUSTRIAL_IRON_GIRDER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.WEATHERED_IRON_GIRDER.get(), RenderType.cutout());
        });
    }
}
