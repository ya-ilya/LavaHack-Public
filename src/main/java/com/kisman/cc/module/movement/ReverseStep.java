package com.kisman.cc.module.movement;

import com.kisman.cc.gui.csgo.components.Slider;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.TimerUtil;

import java.util.Locale;

public class ReverseStep extends Module {
    public static ReverseStep instance;

    public final Setting height = new Setting("Height", this, 1.0, 0.5, 4, false);

    private final Setting lagTime = new Setting("Lag Time", this, false);
    private final Setting lagTimeValue = new Setting("Lag Time Value", this, 500, 0, 2000, Slider.NumberType.TIME);

    private final TimerUtil lagTimer = new TimerUtil();

    public ReverseStep() {
        super("ReverseStep", Category.MOVEMENT);

        instance = this;

        register(height);
        register(lagTime);
        register(lagTimeValue);
    }

    public void onEnable() {
        lagTimer.reset();
    }

    public void update() {
        if (mc.world == null || mc.player == null || mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder() || mc.gameSettings.keyBindJump.isKeyDown()) return;
        super.setDisplayInfo("[" + String.format(Locale.ENGLISH, "%.4f", height.getValDouble()) + "]");
        if (mc.player.onGround && !mc.player.isInWater() && !mc.player.isOnLadder()) {
            if (lagTimer.passedMillis(lagTime.getValBoolean() ? lagTimeValue.getValLong() : 500L)) {
                lagTimer.reset();
                if (lagTime.getValBoolean()) return;
            }
            for (double y = 0.0; y < height.getValDouble() + 0.5; y += 0.01) {
                if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                    mc.player.motionY = -10.0;
                    break;
                }
            }
        }
    }
}
