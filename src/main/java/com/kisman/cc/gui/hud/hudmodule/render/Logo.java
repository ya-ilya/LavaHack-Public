package com.kisman.cc.gui.hud.hudmodule.render;

import com.kisman.cc.Kisman;
import com.kisman.cc.gui.hud.hudmodule.HudCategory;
import com.kisman.cc.gui.hud.hudmodule.HudModule;
import com.kisman.cc.module.client.CustomFont;
import com.kisman.cc.module.client.HUD;
import com.kisman.cc.util.Colour;
import com.kisman.cc.util.Render2DUtil;
import com.kisman.cc.util.customfont.CustomFontUtil;
import com.kisman.cc.util.render.objects.Icons;
import com.kisman.cc.util.gish.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Logo extends HudModule {
    public Logo() {
        super("Logo", "lava-hack on top", HudCategory.RENDER);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        String name = Kisman.getName();
        String version = Kisman.getVersion();

        if(HUD.instance.logoMode.getValString().equals("Simple")) {
            if(event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
                int color = HUD.instance.astolfoColor.getValBoolean() ? ColorUtil.astolfoColors(100, 100) : -1;

                if(HUD.instance.logoGlow.getValBoolean()) {
                    int glowOffset = HUD.instance.glowOffset.getValInt();
                    Render2DUtil.drawGlow(1 - glowOffset, 1 - glowOffset, 1 + CustomFontUtil.getStringWidth(name + version) + glowOffset, 1 + CustomFontUtil.getFontHeight() + glowOffset, color);
                }

                CustomFontUtil.drawStringWithShadow((HUD.instance.logoBold.getValBoolean() ? TextFormatting.BOLD : "") + name + " " + TextFormatting.GRAY + version, 1, 1, color);
            }
        } else if(HUD.instance.logoMode.getValString().equals("CSGO")) {
            String text = ((CustomFont.instance.mode.getValString().equals("Verdana") ? TextFormatting.BOLD : "") + name) + TextFormatting.GRAY + " | " + TextFormatting.RESET + mc.player.getName() + TextFormatting.GRAY + " | " + TextFormatting.RESET + (mc.isSingleplayer() ? 0 : Kisman.instance.serverManager.getPing()) + " ms" + TextFormatting.GRAY + " | " + TextFormatting.RESET + "FPS " + Minecraft.getDebugFPS();
            int x = 3;
            int y = 8;
            int width = 4 + CustomFontUtil.getStringWidth(text);
            int height = 4 + CustomFontUtil.getFontHeight();

            Gui.drawRect(x + 3, y + 3, x + width + 3, y + height - 3, (ColorUtil.getColor(33, 33, 42)));
            Gui.drawRect(x + 3, y, x + width + 3, y + height, (ColorUtil.getColor(33, 33, 42)));
            Gui.drawRect(x + 2, y + 2, x + width + 2, y + height - 2, (ColorUtil.getColor(45, 45, 55)));
            Gui.drawRect(x + 2, y, x + width + 2, y + height, (ColorUtil.getColor(45, 45, 55)));
            Gui.drawRect(x + 1, y + 1, x + width + 1, y + height - 1, (ColorUtil.getColor(60, 60, 70)));
            Gui.drawRect(x + 1, y, x + width + 1, y + height, (ColorUtil.getColor(60, 60, 70)));
            Gui.drawRect(x - 3, y - 8, x + width + 3, y + height - 3, (ColorUtil.getColor(33, 33, 42)));
            Gui.drawRect(x - 3, y, x + width + 3, y + height, (ColorUtil.getColor(33, 33, 42)));
            Gui.drawRect(x - 2, y - 7, x + width + 2, y + height - 2, (ColorUtil.getColor(45, 45, 55)));
            Gui.drawRect(x - 2, y, x + width + 2, y + height, (ColorUtil.getColor(45, 45, 55)));
            Gui.drawRect(x - 1, y - 6, x + width + 1, y + height - 1, (ColorUtil.getColor(60, 60, 70)));
            Gui.drawRect(x - 1, y, x + width + 1, y + height, (ColorUtil.getColor(60, 60, 70)));
            Gui.drawRect(x, y - 5, x + width, y + height, (ColorUtil.astolfoColors(100, 100)));
            Gui.drawRect(x - 3, y - 1, x + width + 3, y + height + 3, (ColorUtil.getColor(33, 33, 42)));
            Gui.drawRect(x - 2, y - 2, x + width + 2, y + height + 2, (ColorUtil.getColor(45, 45, 55)));
            Gui.drawRect(x - 1, y - 3, x + width + 1, y + height + 1, (ColorUtil.getColor(60, 60, 70)));
            Gui.drawRect(x, y - 4, x + width, y + height, (ColorUtil.getColor(34, 34, 40)));

            CustomFontUtil.drawStringWithShadow((HUD.instance.logoBold.getValBoolean() ? TextFormatting.BOLD : "") + text, x + 2, y + 2, ColorUtil.astolfoColors(100, 100));
        } else if(HUD.instance.logoMode.checkValString("GishCode")) {
            int color = HUD.instance.astolfoColor.getValBoolean() ? ColorUtil.astolfoColors(100, 100) : -1;

            GL11.glPushMatrix();

            GL11.glScaled(1.5, 1.5, 1.5);
            mc.fontRenderer.drawStringWithShadow("LavaHack", 4, 4, color);
            GL11.glScaled(0.6, 0.6, 0.6);
            mc.fontRenderer.drawStringWithShadow(TextFormatting.GRAY + Kisman.getVersion(), 84, 4, -1);
            mc.fontRenderer.drawStringWithShadow(TextFormatting.GRAY + "1.12.2", 84, 14, -1);

            GL11.glPopMatrix();
        } else {
            if(HUD.instance.logoImage.checkValString("Old")) Icons.LOGO.render(0, 0, 50, 50);
            else if(HUD.instance.logoImage.checkValString("New")) Icons.LOGO_NEW.render(0, 0, 80, 80 , new Colour(ColorUtil.astolfoColors(100, 100)));
        }
    }
}
