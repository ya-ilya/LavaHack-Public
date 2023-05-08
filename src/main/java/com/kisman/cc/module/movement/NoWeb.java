package com.kisman.cc.module.movement;

import com.kisman.cc.mixin.mixins.accessor.AccessorEntity;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import org.lwjgl.input.Keyboard;

public class NoWeb extends Module {
//    public final Setting disableBB = new Setting("Add BB", this, true);
    public final Setting onGround = new Setting("OnGround", this, true);
//    public final Setting bbOffset = new Setting("BB Offset", this, 0, -2, 2, false);
    public final Setting motionX = new Setting("MotionX", this, 0.84, -1, 5, false);
    public final Setting motionY = new Setting("MotionY", this, 1, 0, 20, false);

    public NoWeb() {
        super("NoWeb", Category.MOVEMENT);

//      register(disableBB);
        register(onGround);
//      register(bbOffset);
        register(motionX);
        register(motionY);
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;

        if (((AccessorEntity) mc.player).isInWeb() && !Step.instance.isToggled()) {
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                ((AccessorEntity) mc.player).setIsInWeb(true);
                mc.player.motionY *= motionY.getValDouble();
            } else if (onGround.getValBoolean()) mc.player.onGround = false;

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) || Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()) || Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())
                    || Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
                ((AccessorEntity) mc.player).setIsInWeb(false);
                mc.player.motionX *= motionX.getValDouble();
                mc.player.motionZ *= motionX.getValDouble();
            }
        }
    }
}
