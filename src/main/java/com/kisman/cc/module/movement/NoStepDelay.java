package com.kisman.cc.module.movement;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;

public class NoStepDelay extends Module {
    public NoStepDelay() {
        super("NoStepDelay", Category.MOVEMENT);
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;
        mc.playerController.stepSoundTickCounter = 0;
    }
}
