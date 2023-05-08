package com.kisman.cc.module.misc;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.TimerUtil;

public class SelfDamage extends Module {
    private final Setting jump;
    private final Setting timer;

    private int jumpCount;

    public SelfDamage() {
        super("SelfDamage", Category.MISC);

        register(this.jump = new Setting("Jumps", this, 3, 3, 50, true));
        register(this.timer = new Setting("JumpTimer", this, 3, 1, 1000, true));
    }

    public void onEnable() {
        this.jumpCount = 0;
    }

    public void onDisable() {
        TimerUtil.setTickLength(1);
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;
        if (jumpCount < jump.getValDouble()) {
            TimerUtil.setTickLength((float) timer.getValDouble());
            mc.player.onGround = false;
        }

        if (mc.player.onGround) {
            if (jumpCount < jump.getValDouble()) {
                mc.player.jump();
                jumpCount++;
            } else {
                TimerUtil.setTickLength(1);
            }
        }
    }
}
