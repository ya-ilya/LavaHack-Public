package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.gish.ColorUtil;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomFog extends Module {
    private final Setting red = new Setting("Red", this, 1, 0, 1, false);
    private final Setting green = new Setting("Green", this, 0, 0, 1, false);
    private final Setting blue = new Setting("Blue", this, 0, 0, 1, false);
    private final Setting rainbow = new Setting("Rainbow", this, true);
    private final Setting saturation = new Setting("Saturation", this, 1, 0,1, false);
    private final Setting brightness = new Setting("Brightness", this, 1, 0, 1,  false);
    private final Setting delay = new Setting("Delay", this, 100, 1, 2000, true);

    public CustomFog() {
        super("CustomFog", Category.RENDER);

        register(red);
        register(green);
        register(blue);
        register(rainbow);
        register(saturation);
        register(brightness);
        register(delay);
    }

    @SubscribeEvent
    public void onRenderSky(EntityViewRenderEvent.FogColors event) {
        if (rainbow.getValBoolean()) {
            event.setRed(ColorUtil.rainbowRGB(delay.getValInt(), saturation.getValFloat(), brightness.getValFloat()).getRed() / 255f);
            event.setGreen(ColorUtil.rainbowRGB(delay.getValInt(), saturation.getValFloat(), brightness.getValFloat()).getGreen() / 255f);
            event.setBlue(ColorUtil.rainbowRGB(delay.getValInt(), saturation.getValFloat(), brightness.getValFloat()).getBlue() / 255f);
        } else {
            event.setRed(red.getValFloat());
            event.setGreen(green.getValFloat());
            event.setBlue(blue.getValFloat());
        }
    }
}
