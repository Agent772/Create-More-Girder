package com.agent772.createmoregirder.foundation;

import com.agent772.createmoregirder.content.strut.GirderStrutPlacementEffects;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Client-side event handlers
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
public class CMGClientEvents {

    @SubscribeEvent
    public static void onClientTickPre(final TickEvent.ClientTickEvent event) {
        final Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.player != null) {
            GirderStrutPlacementEffects.tick(mc.player);
        }
    }
}
