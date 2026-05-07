package com.agent772.createmoregirder.content.strut;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.item.ItemStack;

public class GirderStrutCostOverlay {

    public static final LayeredDraw.Layer OVERLAY = GirderStrutCostOverlay::renderOverlay;

    private static final int SLOT_WIDTH = 21;
    private static final int SLOT_ICON_OFFSET = 3;
    private static final int VERTICAL_OFFSET_FROM_BOTTOM = 100;

    private static boolean active;
    private static ItemStack displayItem = ItemStack.EMPTY;
    private static int displayCount;
    private static boolean fulfilled;

    public static void display(ItemStack item, int count, boolean hasSufficient) {
        active = true;
        displayItem = item;
        displayCount = count;
        fulfilled = hasSufficient;
    }

    public static void reset() {
        active = false;
    }

    private static void renderOverlay(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.screen != null)
            return;
        if (!active || displayItem.isEmpty() || displayCount <= 0)
            return;

        int slots = 0;
        int remaining = displayCount;
        while (remaining > 0) {
            slots++;
            remaining -= 64;
        }

        int w = SLOT_WIDTH * slots;
        int x = (graphics.guiWidth() - w) / 2;
        int y = graphics.guiHeight() - VERTICAL_OFFSET_FROM_BOTTOM;

        remaining = displayCount;
        for (int i = 0; i < slots; i++) {
            int slotCount = Math.min(64, remaining);
            remaining -= slotCount;

            RenderSystem.enableBlend();
            (fulfilled ? AllGuiTextures.HOTSLOT_ACTIVE : AllGuiTextures.HOTSLOT).render(graphics, x, y);

            ItemStack renderStack = displayItem.copyWithCount(slotCount);
            String countStr = fulfilled ? null : ChatFormatting.GOLD.toString() + slotCount;

            GuiGameElement.of(renderStack)
                    .at(x + SLOT_ICON_OFFSET, y + SLOT_ICON_OFFSET)
                    .render(graphics);
            graphics.renderItemDecorations(mc.font, renderStack, x + SLOT_ICON_OFFSET, y + SLOT_ICON_OFFSET, countStr);
            x += SLOT_WIDTH;
        }

        RenderSystem.disableBlend();
    }
}
