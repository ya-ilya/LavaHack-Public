package com.kisman.cc.gui.hud.hudmodule.player;

import com.kisman.cc.Kisman;
import com.kisman.cc.gui.hud.hudmodule.HudCategory;
import com.kisman.cc.gui.hud.hudmodule.HudModule;
import com.kisman.cc.module.client.HUD;
import com.kisman.cc.util.customfont.CustomFontUtil;
import com.kisman.cc.util.gish.ColorUtil;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Ping extends HudModule {
    public Ping() {
        super("Ping", "", HudCategory.PLAYER, true);
        setX(1);
        setY(1);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        String str = "Ping: " + TextFormatting.GRAY + (mc.isSingleplayer() ? 0 : Kisman.instance.serverManager.getPing());
        setW(CustomFontUtil.getStringWidth(str));
        setH(CustomFontUtil.getFontHeight());
        CustomFontUtil.drawStringWithShadow(str, getX(), getY(), HUD.instance.astolfoColor.getValBoolean() ? ColorUtil.astolfoColors(100, 100) : -1);
    }
}
