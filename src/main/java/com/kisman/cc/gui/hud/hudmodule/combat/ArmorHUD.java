package com.kisman.cc.gui.hud.hudmodule.combat;

import com.kisman.cc.gui.hud.hudmodule.HudCategory;
import com.kisman.cc.gui.hud.hudmodule.HudModule;
import com.kisman.cc.module.client.HUD;
import com.kisman.cc.util.customfont.CustomFontUtil;
import com.kisman.cc.util.gish.ColorUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ArmorHUD extends HudModule {
    private int armourCompress;
    private int armourSpacing;

    public ArmorHUD() {
        super("ArmorHud", "ArmorHUD", HudCategory.COMBAT);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if(mc.player == null && mc.world == null) return;

        ScaledResolution rs = event.getResolution();
        RenderItem itemRender = mc.getRenderItem();
        int i = rs.getScaledWidth() / 2;
        int iteration = 0;
        int y = rs.getScaledHeight() - 55 - (mc.player.isInWater() ? 10 : 0);

        for (ItemStack is : mc.player.inventory.armorInventory) {
            iteration++;
            if (is.isEmpty()) continue;
            int x = i - 90 + (9 - iteration) * armourSpacing + armourCompress;
            GlStateManager.enableDepth();

            itemRender.zLevel = 200F;
            itemRender.renderItemAndEffectIntoGUI(is, x, y);
            itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
            itemRender.zLevel = 0F;

            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            String s = is.getCount() > 1 ? String.valueOf(is.getCount()) : "";
            int color = HUD.instance.astolfoColor.getValBoolean() ? ColorUtil.astolfoColors(100, 100) : -1;
            CustomFontUtil.drawStringWithShadow(s, x + 19 - 2 - CustomFontUtil.getStringWidth(s), y + 9, color);

            if (HUD.instance.armDmg.getValBoolean()) {
                float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
                float red = 1 - green;
                int dmg = 100 - (int) (red * 100);
                CustomFontUtil.drawStringWithShadow(String.valueOf(dmg), x + 8 - CustomFontUtil.getStringWidth(String.valueOf(dmg)) / 2.0, y - 11, color);
            }

            if (HUD.instance.armExtra.getValBoolean()) {
                final ItemStack itemStack = mc.player.getHeldItemOffhand();
                Item helfInOffHand = mc.player.getHeldItemOffhand().getItem();
                int offHandHeldItemCount = getItemsOffHand(helfInOffHand);
                GlStateManager.pushMatrix();
                GlStateManager.disableAlpha();
                GlStateManager.clear(256);
                GlStateManager.enableBlend();
                GlStateManager.pushAttrib();
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.disableDepth();

                mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, 572, y);
                itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, 572, y, String.valueOf(offHandHeldItemCount));
                GlStateManager.enableDepth();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popAttrib();
                GlStateManager.disableBlend();

                GlStateManager.disableDepth();
                GlStateManager.disableLighting();
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
                GlStateManager.popMatrix();
            }
            if (HUD.instance.armExtra.getValBoolean()) {
                Item currentHeldItem = mc.player.inventory.getCurrentItem().getItem();
                int currentHeldItemCount = mc.player.inventory.getCurrentItem().getCount();

                ItemStack stackHeld = new ItemStack(currentHeldItem, 1);
                GlStateManager.pushMatrix();
                GlStateManager.disableAlpha();
                GlStateManager.clear(256);
                GlStateManager.enableBlend();
                GlStateManager.pushAttrib();
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.disableDepth();
                mc.getRenderItem().renderItemAndEffectIntoGUI(stackHeld, 556, y);

                itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, stackHeld, 556, y, String.valueOf(currentHeldItemCount));

                GlStateManager.enableDepth();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popAttrib();
                GlStateManager.disableBlend();

                GlStateManager.disableDepth();
                GlStateManager.disableLighting();
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
                GlStateManager.popMatrix();
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();

            if (HUD.instance.armExtra.getValBoolean()) {
                armourCompress = 14;
                armourSpacing = 17;
            } else {
                armourCompress = 2;
                armourSpacing = 20;
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    private int getItemsOffHand(Item i) {
        return mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == i).mapToInt(ItemStack::getCount).sum();
    }
}
