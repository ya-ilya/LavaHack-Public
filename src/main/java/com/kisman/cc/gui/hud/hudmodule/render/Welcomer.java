package com.kisman.cc.gui.hud.hudmodule.render;

import com.kisman.cc.Kisman;
import com.kisman.cc.gui.hud.hudmodule.HudCategory;
import com.kisman.cc.gui.hud.hudmodule.HudModule;
import com.kisman.cc.module.client.HUD;
import com.kisman.cc.util.customfont.CustomFontUtil;
import com.kisman.cc.util.gish.ColorUtil;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Welcomer extends HudModule {
    public Welcomer() {
        super("Welcomer", HudCategory.RENDER, true);
    }

    public void onEnable() {
        setX(100);
        setY(2);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        int color = HUD.instance.astolfoColor.getValBoolean() ? ColorUtil.astolfoColors(100, 100) : HUD.instance.welColor.getColour().getRGB();

        setW(CustomFontUtil.getStringWidth("Welcome to " + Kisman.getName() + ", " + mc.player.getName() + "!"));
        setH(CustomFontUtil.getFontHeight());

        CustomFontUtil.drawCenteredStringWithShadow("Welcome to " + Kisman.getName() + ", " + mc.player.getName() + "!", getX(), getY(), color);
    }
}
