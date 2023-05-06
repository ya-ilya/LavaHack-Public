package com.kisman.cc.module.movement;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;

public class AutoWalk extends Module{
    public AutoWalk() {
        super("AutoWalk", Category.MOVEMENT);
    }

    public void onDisable() {
        if (mc.player == null || mc.world == null) return;

        mc.gameSettings.keyBindForward.pressed = false;
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;

        mc.gameSettings.keyBindForward.pressed = true;
    }
}
