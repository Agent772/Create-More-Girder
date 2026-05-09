package com.agent772.createmoregirder.foundation;

import com.agent772.createmoregirder.CMGBlocks;
import com.agent772.createmoregirder.CreateMoreGirder;
import com.agent772.createmoregirder.content.strut.GirderStrutCostOverlay;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CreateMoreGirder.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
        });
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "strut_cost", GirderStrutCostOverlay.OVERLAY);
    }
}
