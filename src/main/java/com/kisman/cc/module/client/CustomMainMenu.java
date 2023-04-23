package com.kisman.cc.module.client;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;

public class CustomMainMenu extends Module {
    public final Setting watermark = new Setting("WaterMark", this, true);
    public final Setting customSplashText = new Setting("Custom Splash Text", this, true);
    public final Setting customSplashFont = new Setting("Custom Splash Font", this, true).setVisible(customSplashText::getValBoolean);
    public final Setting particles = new Setting("Particles", this, true);

    public static CustomMainMenu instance;

    public CustomMainMenu() {
        super("CustomMainMenu", Category.CLIENT);

        instance = this;

        register(watermark);
        register(customSplashText);
        register(customSplashFont);
        register(particles);
    }
}
