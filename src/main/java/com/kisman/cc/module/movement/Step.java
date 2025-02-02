package com.kisman.cc.module.movement;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;

import java.util.Locale;

public class Step extends Module {
    public static Step instance;

    public final Setting height = new Setting("Height", this, 0.5f, 0.5f, 4, false);

    public Step() {
        super("Step", Category.MOVEMENT);

        instance = this;

        register(height);
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;
        super.setDisplayInfo("[" + String.format(Locale.ENGLISH, "%.4f", height.getValDouble()) + "]");
        mc.player.stepHeight = height.getValFloat();
    }

    public void onDisable() {
        if (mc.player != null && mc.world != null) mc.player.stepHeight = 0.5f;
    }
}
