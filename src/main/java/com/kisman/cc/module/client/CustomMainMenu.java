package com.kisman.cc.module.client;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;

public class CustomMainMenu extends Module {
    public final Setting watermark = new Setting("WaterMark", this, true);
    public final Setting customSplashText = new Setting("Custom Splash Text", this, true);
    public final Setting customSplashFont = new Setting("Custom Splash Font", this, true).setVisible(() -> customSplashText.getValBoolean());
    public final Setting particles = new Setting("Particles", this, true);

    public static CustomMainMenu instance;

    public CustomMainMenu() {
        super("CustomMainMenu", Category.CLIENT);

        instance = this;

        settingManager.register(watermark);
        settingManager.register(customSplashText);
        settingManager.register(customSplashFont);
        settingManager.register(particles);
    }

    public void update() {
        com.kisman.cc.util.modules.CustomMainMenu.WATERMARK = watermark.getValBoolean();
        com.kisman.cc.util.modules.CustomMainMenu.CUSTOM_SPLASH_TEXT = customSplashText.getValBoolean();
        com.kisman.cc.util.modules.CustomMainMenu.CUSTOM_SPLASH_FONT = customSplashFont.getValBoolean();
        com.kisman.cc.util.modules.CustomMainMenu.PARTICLES = particles.getValBoolean();
    }

    public void onDisable() {
        com.kisman.cc.util.modules.CustomMainMenu.WATERMARK = false;
        com.kisman.cc.util.modules.CustomMainMenu.CUSTOM_SPLASH_TEXT = false;
        com.kisman.cc.util.modules.CustomMainMenu.CUSTOM_SPLASH_FONT = false;
        com.kisman.cc.util.modules.CustomMainMenu.PARTICLES = false;
    }
}
