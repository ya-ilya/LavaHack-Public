package com.kisman.cc.module.movement;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.module.combat.KillAura;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.EntityUtil;
import com.kisman.cc.util.RotationUtils;
import com.kisman.cc.util.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;

import java.util.Objects;

public class TargetStrafe extends Module {
    private final Setting radius = new Setting("Radius", this, 3.6f, 0.1f, 7, false);
    private final Setting speed = new Setting("Speed", this, 3.19, 0.15f, 50, false);
    private final Setting autoJump = new Setting("Auto Jump", this, false);
    private Entity target;
    private final TimerUtil timer = new TimerUtil();
    private int direction;

    public TargetStrafe() {
        super("TargetStrafe", Category.MOVEMENT);

        register(radius);
        register(speed);
        register(autoJump);
    }

    public double getMovementSpeed() {
        double speed = 0.2873;
        if (mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(1)))) {
            final int n = Objects.requireNonNull(mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier();
            speed *= 1.0 + 0.2 * (n + 1);
        }
        return speed;
    }

    public void update() {
        if (mc.player == null || mc.world == null || !KillAura.instance.isToggled()) return;

        target = EntityUtil.getTarget(6);

        if (target == null) {
            super.setDisplayInfo("[Radius: " + radius.getValInt() + " | Speed: " + speed.getValInt() + "]");
            return;
        } else super.setDisplayInfo("[" + target.getName() + " | Radius: " + radius.getValInt() + " | Speed: " + speed.getValInt() + "]");

        if (mc.player.collidedHorizontally) {
            timer.reset();
            invertStrafe();
        }

        if (autoJump.getValBoolean() && mc.player.onGround) mc.player.jump();
        if (mc.gameSettings.keyBindLeft.isKeyDown()) direction = 1;
        if (mc.gameSettings.keyBindRight.isKeyDown()) direction = -1;

        mc.player.movementInput.moveForward = 0;
        doTargetStrafe(getMovementSpeed());
    }

    private void doTargetStrafe(double speed) {
        if (mc.player.getDistance(target) <= radius.getValDouble()) setSpeed(speed - (0.2 - this.speed.getValDouble() / 100.0), RotationUtils.getNeededRotations(target)[0], this.direction, 0.0);
        else setSpeed(speed - (0.2 - this.speed.getValDouble() / 100.0), RotationUtils.getNeededRotations(target)[0], this.direction, 1.0);
    }

    private void invertStrafe() {
        this.direction = -this.direction;
    }

    private void setSpeed(final double d, final float f, final double d2, final double d3) {
        double d4 = d3;
        double d5 = d2;
        float f2 = f;
        if (d4 == 0.0 && d5 == 0.0) {
            mc.player.motionZ = 0.0;
            mc.player.motionX = 0.0;
        } else {
            if (d4 != 0.0) {
                if (d5 > 0.0) f2 += ((d4 > 0.0) ? -45 : 45);
                else if (d5 < 0.0) f2 += ((d4 > 0.0) ? 45 : -45);
                d5 = 0.0;
                if (d4 > 0.0) d4 = 1.0;
                else if (d4 < 0.0) d4 = -1.0;
            }
            final double d6 = Math.cos(Math.toRadians(f2 + 90.0f));
            final double d7 = Math.sin(Math.toRadians(f2 + 90.0f));
            mc.player.motionX = d4 * d * d6 + d5 * d * d7;
            mc.player.motionZ = d4 * d * d7 - d5 * d * d6;
        }
    }
}
