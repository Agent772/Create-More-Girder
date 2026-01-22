package com.agent772.createmoregirder.foundation;

import com.agent772.createmoregirder.content.strut.GirderStrutPlacementEffects;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

/**
 * Client-side event handlers
 */
@EventBusSubscriber(Dist.CLIENT)
public class CMGClientEvents {

    @SubscribeEvent
    public static void onClientTickPre(final ClientTickEvent.Pre event) {
        final Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.player != null) {
            GirderStrutPlacementEffects.tick(mc.player);
        }
    }
}
