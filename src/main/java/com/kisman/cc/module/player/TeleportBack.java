package com.kisman.cc.module.player;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.module.movement.Sprint;
import com.kisman.cc.util.ChatUtil;
import net.minecraft.network.play.client.CPacketPlayer;

public class TeleportBack extends Module {
    private double x;
    private double y;
    private double z;

    public TeleportBack() {
        super("TeleportBack", Category.PLAYER);
    }

    public void onEnable() {
        if (mc.player != null && mc.world != null) {
            savePosition();
            ChatUtil.complete("Position saved!");
        }
    }

    public void onDisable() {
        if (mc.player != null && mc.world != null) {
            loadPosition();
            ChatUtil.complete("Teleported!");
        }
    }

    public void update() {
        if (mc.player != null && mc.world != null) {
            x = mc.player.posX;
            y = mc.player.posY;
            z = mc.player.posZ;

            if (Sprint.instance.isToggled()) {
                Sprint.instance.setToggled(false);
            }

            if (mc.player.isSprinting()) {
                mc.player.setSprinting(false);
            }

            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                loadPosition();
            }

            mc.player.onGround = false;
        }
    }

    private void savePosition() {
        x = mc.player.posX;
        y = mc.player.posY;
        z = mc.player.posZ;
    }


    private void loadPosition() {
        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, false));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));

        mc.player.setPositionAndUpdate(x, y, z);
        mc.player.setPosition(x, y, z);
    }
}
