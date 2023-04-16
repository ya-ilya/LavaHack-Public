package com.kisman.cc.module.client;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;

import java.util.Arrays;

public class Cape extends Module {
    public static Cape instance;

    public final Setting mode = new Setting("Cape Mode", this, "Gif", Arrays.asList("Gif", "Xulu+", "GentleManMC", "Kuro", "Putin", "Gradient"));

    public Cape() {
        super("Cape", "Custom cape", Category.CLIENT);

        instance = this;

        settingManager.register(mode);
    }

    public void update() {
        super.setDisplayInfo("[" + mode.getValString() + "]");
    }
}
