package com.kisman.cc.module.movement;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import net.minecraft.network.play.client.CPacketEntityAction;

import java.util.Arrays;

public class Fly extends Module {
    private final Setting flySpeed = new Setting("FlySpeed", this, 0.1f, 0.1f, 100.0f, false);
    private final Setting mode = new Setting("Mode", this, "Vanilla", Arrays.asList("Vanilla", "WellMore", "ReallyWorld"));

    public Fly() {
        super("Fly", Category.MOVEMENT);

        register(flySpeed);
        register(mode);
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;

        super.setDisplayInfo("[" + mode.getValString() + "]");

        if (mode.getValString().equalsIgnoreCase("Vanilla")) {
            mc.player.capabilities.isFlying = true;
            mc.player.capabilities.setFlySpeed(flySpeed.getValFloat());
        } else if (mode.getValString().equalsIgnoreCase("WellMore")) {
            if (mc.player.onGround) mc.player.motionY = 1.0;
            else {
                mc.player.capabilities.isFlying = true;
                mc.player.capabilities.setFlySpeed(1.3f);
                mc.player.motionX = 0.0;
                mc.player.motionY = -0.02;
                mc.player.motionZ = 0.0;
            }
        } else if (mode.getValString().equalsIgnoreCase("ReallyWorld")) {
            if (mc.gameSettings.keyBindJump.isPressed()) {
                if (mc.player.ticksExisted % 3 == 0) mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                mc.player.jump();
            }
        }
    }

    public void onDisable() {
        if (mc.player == null || mc.world == null) return;

        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.setFlySpeed(0.1f);
    }
}
