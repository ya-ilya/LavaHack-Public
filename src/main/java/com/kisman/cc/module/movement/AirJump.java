package com.kisman.cc.module.movement;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;

import java.util.Arrays;

public class AirJump extends Module {
    private final Setting mode = new Setting("Mode", this, "Vanilla", Arrays.asList("Vanilla", "NCP", "Matrix"));

    public AirJump() {
        super("AirJump", "Category", Category.MOVEMENT);

        setmgr.rSetting(mode);
    }

    public void update() {
        if(mc.player == null || mc.world == null) return;
        if (mode.getValString().equalsIgnoreCase("Vanilla")) if (mc.gameSettings.keyBindJump.isPressed()) mc.player.motionY = 0.7;
        else if (mode.getValString().equalsIgnoreCase("NCP")) {
            mc.player.onGround = true;
            mc.player.isAirBorne = false;
        } else if(mode.getValString().equalsIgnoreCase("Matrix") && mc.gameSettings.keyBindJump.pressed) {
            mc.player.jump();
            mc.player.motionY -= 0.25f;
            if(mc.gameSettings.keyBindForward.pressed) {
                mc.timer.elapsedTicks = (int) 1.05f;
                mc.player.motionX *= 1.1f;
                mc.player.motionZ *= 1.1f;
                mc.player.onGround = false;
            }
        }
    }
}