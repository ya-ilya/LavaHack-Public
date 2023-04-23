package com.kisman.cc.gui.hud.hudmodule.movement;

import com.kisman.cc.Kisman;
import com.kisman.cc.gui.hud.hudmodule.HudCategory;
import com.kisman.cc.gui.hud.hudmodule.HudModule;
import com.kisman.cc.module.client.HUD;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Speed extends HudModule {
    private int i;

    public Speed() {
        super("Speed", "Speed", HudCategory.MOVEMENT);
    }

    public void update() {
        if(Kisman.instance.hudModuleManager.getModule("Coords").isToggled()) {
            i = 1;
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        FontRenderer fr = mc.fontRenderer;
        ScaledResolution sr = new ScaledResolution(mc);

        String str1 = HUD.instance.speedMode.getValString().equalsIgnoreCase("b/s") ? "b/s " : "km/h ";
        String str2 = HUD.instance.speedMode.getValString().equalsIgnoreCase("b/s") ? String.valueOf(getSpeedInBPS()) : String.valueOf(getSpeedInKMPH());

        fr.drawStringWithShadow(TextFormatting.WHITE + "Speed: " + TextFormatting.GRAY + str1 + str2, 1, sr.getScaledHeight() - 1 - fr.FONT_HEIGHT - (this.i == 1 ? (fr.FONT_HEIGHT * 2 + 4) : 0), -1);
    }


    private int getSpeedInKMPH() {
        float speed = getSpeedInBPS();
        speed = speed * 1000 * 3600;

        return (int) speed;
    }

    private static float getSpeedInBPS() {
        return (float)Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
    }
}
