package com.kisman.cc.module.movement;

import com.kisman.cc.gui.csgo.components.Slider;
import com.kisman.cc.manager.Managers;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.BlockUtil;
import com.kisman.cc.util.EntityUtil;
import com.kisman.cc.util.PlayerUtil;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Anchor extends Module {
    private final Setting mode = new Setting("Mode", this, Mode.Motion);
    private final Setting movementStop = new Setting("Movement Stop", this, false).setVisible(!mode.checkValString(Mode.Teleport.name()));
    private final Setting timer = new Setting("Timer", this, false);
    private final Setting timerValue = new Setting("Timer Value", this, 5, 0.1f, 20, false).setVisible(timer::getValBoolean);
    private final Setting disableAfterComplete = new Setting("Disable After Complete", this, false);
    private final Setting fastFall = new Setting("Fast Fall", this, false);
    private final Setting fastFallMotion = new Setting("Fast Fall Motion", this, 10, 1, 10, false).setVisible(fastFall::getValBoolean);
    private final Setting useLagTime = new Setting("Use Fast Fall Lag Time", this, false);
    private final Setting lagTime = new Setting("Fast Fall Lag Time", this, 500, 0, 1000, Slider.NumberType.TIME);
    private final Setting pitch = new Setting("Pitch", this, 60, 0, 90, false);

    private boolean using = false;
    private final double[] oneblockPositions = new double[] { 0.42, 0.75 };
    private int packets;
    private boolean jumped = false;

    public Anchor() {
        super("Anchor", Category.MOVEMENT);
        super.setDisplayInfo(() -> "[" + mode.getValString() + "]");

        register(mode);
        register(movementStop);
        register(timer);
        register(timerValue);
        register(disableAfterComplete);
        register(fastFall);
        register(fastFallMotion);
        register(pitch);
    }

    private Vec3d getCenter(double posX, double posY, double posZ) {
        double x = Math.floor(posX) + 0.5D;
        double y = Math.floor(posY);
        double z = Math.floor(posZ) + 0.5D ;

        return new Vec3d(x, y, z);
    }

    public void onDisable() {
        using = false;
    }

    public void update() {
        if (mc.world == null && mc.player == null) return;
        super.setDisplayInfo("[" + mode.getValString() + "]");
        if (mc.player.posY < 0) return;

        if (mc.player.rotationPitch >= pitch.getValDouble()) {
            if (BlockUtil.isBlockHole(PlayerUtil.getPlayerPos().down(1)) || BlockUtil.isBlockHole(PlayerUtil.getPlayerPos().down(2)) || BlockUtil.isBlockHole(PlayerUtil.getPlayerPos().down(3)) || BlockUtil.isBlockHole(PlayerUtil.getPlayerPos().down(4))) {
                if (mode.getValString().equals(Mode.Motion.name())) {
                    Vec3d center = getCenter(mc.player.posX, mc.player.posY, mc.player.posZ);

                    double xDiff = Math.abs(center.x - mc.player.posX);
                    double zDiff = Math.abs(center.z - mc.player.posZ);

                    if (xDiff <= 0.1 && zDiff <= 0.1) center = Vec3d.ZERO;
                    else {
                        double motionX = center.x - mc.player.posX;
                        double motionZ = center.z - mc.player.posZ;

                        mc.player.motionX = motionX / 2;
                        mc.player.motionZ = motionZ / 2;
                    }
                    if (fastFall.getValBoolean() && !lagTimeCheck()) mc.player.motionY = -fastFallMotion.getValDouble();
                    using = true;
                } else if (mode.getValString().equals(Mode.Teleport.name())) {
                    if (!mc.player.onGround) this.jumped = mc.gameSettings.keyBindJump.isKeyDown();
        
                    if (!this.jumped && mc.player.fallDistance < 0.5 && BlockUtil.isInHole() && mc.player.posY - BlockUtil.getNearestBlockBelow() <= 1.125 && mc.player.posY - BlockUtil.getNearestBlockBelow() <= 0.95 && !EntityUtil.isOnLiquid() && !EntityUtil.isInLiquid()) {
                        if (!mc.player.onGround) ++this.packets;
                        if (!mc.player.onGround && !mc.player.isInsideOfMaterial(Material.WATER) && !mc.player.isInsideOfMaterial(Material.LAVA) && !mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.isOnLadder() && this.packets > 0) {
                            BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                            for (double position : oneblockPositions) mc.player.connection.sendPacket(new CPacketPlayer.Position((blockPos.getX() + 0.5f), mc.player.posY - position, (blockPos.getZ() + 0.5f), true));
                            mc.player.setPosition((blockPos.getX() + 0.5f), BlockUtil.getNearestBlockBelow() + 0.1, (blockPos.getZ() + 0.5f));
                            this.packets = 0;
                        }
                    }

                    if (fastFall.getValBoolean() && !lagTimeCheck()) {
                        mc.player.motionY = -fastFallMotion.getValDouble();
                    }
                }
            } else using = false;
        }

        if (BlockUtil.isBlockHole(PlayerUtil.getPlayerPos())) using = false;

        if (using && timer.getValBoolean()) EntityUtil.setTimer(timerValue.getValFloat());
        else EntityUtil.resetTimer();

        if (BlockUtil.isBlockHole(PlayerUtil.getPlayerPos())) {
            if (disableAfterComplete.getValBoolean()) super.setToggled(false);
            if (using) using = false;
        }
    }

    private boolean lagTimeCheck() {
        return useLagTime.getValBoolean() && Managers.instance.passed(lagTime.getValInt());
    }

    public enum Mode {MovementStop, Motion, Teleport}
}
