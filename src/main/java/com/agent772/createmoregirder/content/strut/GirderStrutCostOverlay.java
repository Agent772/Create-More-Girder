package com.agent772.createmoregirder.content.strut;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class GirderStrutCostOverlay {

    public static final IGuiOverlay OVERLAY = GirderStrutCostOverlay::renderOverlay;

    private static final int SLOT_WIDTH = 21;
    private static final int SLOT_ICON_OFFSET = 3;
    private static final int VERTICAL_OFFSET_FROM_BOTTOM = 100;
    private static final int GROUP_GAP = 6;

    private static boolean active;
    private static ItemStack displayItem = ItemStack.EMPTY;
    private static int displayCount;
    private static boolean fulfilled;

    private static ItemStack textureDisplayItem = ItemStack.EMPTY;
    private static int textureDisplayCount;
    private static boolean textureFulfilled;

    public static void display(ItemStack item, int count, boolean hasSufficient) {
        active = true;
        displayItem = item;
        displayCount = count;
        fulfilled = hasSufficient;
        textureDisplayItem = ItemStack.EMPTY;
        textureDisplayCount = 0;
        textureFulfilled = true;
    }

    public static void displayWithTexture(ItemStack item, int count, boolean hasSufficient,
                                           ItemStack textureItem, int textureCount, boolean textureHasSufficient) {
        active = true;
        displayItem = item;
        displayCount = count;
        fulfilled = hasSufficient;
        textureDisplayItem = textureItem;
        textureDisplayCount = textureCount;
        textureFulfilled = textureHasSufficient;
    }

    public static void reset() {
        active = false;
    }

    private static void renderOverlay(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.screen != null)
            return;
        if (!active || displayItem.isEmpty() || displayCount <= 0)
            return;

        boolean hasTexture = !textureDisplayItem.isEmpty() && textureDisplayCount > 0;

        int strutSlots = slotCount(displayCount);
        int textureSlots = hasTexture ? slotCount(textureDisplayCount) : 0;

        int totalWidth = SLOT_WIDTH * strutSlots;
        if (hasTexture) {
            totalWidth += GROUP_GAP + SLOT_WIDTH * textureSlots;
        }

        int startX = (screenWidth - totalWidth) / 2;
        int y = screenHeight - VERTICAL_OFFSET_FROM_BOTTOM;

        // Render strut cost slots
        renderSlots(graphics, mc, displayItem, displayCount, fulfilled, startX, y);

        // Render texture cost slots side by side
        if (hasTexture) {
            int textureX = startX + SLOT_WIDTH * strutSlots + GROUP_GAP;
            renderSlots(graphics, mc, textureDisplayItem, textureDisplayCount, textureFulfilled, textureX, y);
        }

        RenderSystem.disableBlend();
    }

    private static int slotCount(int count) {
        int slots = 0;
        int remaining = count;
        while (remaining > 0) {
            slots++;
            remaining -= 64;
        }
        return slots;
    }

    private static void renderSlots(GuiGraphics graphics, Minecraft mc, ItemStack item, int count, boolean isFulfilled, int x, int y) {
        int remaining = count;
        int slots = slotCount(count);
        for (int i = 0; i < slots; i++) {
            int slotCount = Math.min(64, remaining);
            remaining -= slotCount;

            RenderSystem.enableBlend();
            (isFulfilled ? AllGuiTextures.HOTSLOT_ACTIVE : AllGuiTextures.HOTSLOT).render(graphics, x, y);

            ItemStack renderStack = item.copyWithCount(slotCount);
            String countStr = isFulfilled ? null : ChatFormatting.GOLD.toString() + slotCount;

            GuiGameElement.of(renderStack)
                    .at(x + SLOT_ICON_OFFSET, y + SLOT_ICON_OFFSET)
                    .render(graphics);
            graphics.renderItemDecorations(mc.font, renderStack, x + SLOT_ICON_OFFSET, y + SLOT_ICON_OFFSET, countStr);
            x += SLOT_WIDTH;
        }
    }
}
