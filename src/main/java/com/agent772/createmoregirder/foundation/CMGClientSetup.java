package com.agent772.createmoregirder.foundation;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.CreateMoreGirder;
import com.agent772.createmoregirder.content.strut.GirderStrutCostOverlay;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

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
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.COPYCAT_GIRDER.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.COPYCAT_GIRDER_ENCASED_SHAFT.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.COPYCAT_GIRDER_STRUT.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.COPYCAT_METAL_GIRDER.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.COPYCAT_METAL_GIRDER_ENCASED_SHAFT.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(CMGBlocks.COPYCAT_METAL_GIRDER_STRUT.get(), RenderType.cutoutMipped());
        });
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.HOTBAR,
                CreateMoreGirder.asResource("strut_cost"),
                GirderStrutCostOverlay.OVERLAY);
    }
}
