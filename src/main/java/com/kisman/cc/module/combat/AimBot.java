package com.kisman.cc.module.combat;

import com.kisman.cc.event.Event;
import com.kisman.cc.event.events.PlayerMotionUpdateEvent;
import com.kisman.cc.mixin.mixins.accessor.AccessorEntityPlayerSP;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.PlayerUtil;
import com.kisman.cc.util.RotationSpoof;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.Arrays;

public class AimBot extends Module {
    private final Setting mode = new Setting("Mode", this, "Packet", new ArrayList<>(Arrays.asList("Packet", "Client")));

    public static AimBot instance;

    public RotationSpoof rotationSpoof = null;

    public AimBot() {
        super("AimBot", Category.COMBAT);

        instance = this;

        register(mode);
    }

    @SuppressWarnings("unused")
    private final Listener<PlayerMotionUpdateEvent> motionUpdateListener = listener(event -> {
        if (event.getEra() != Event.Era.PRE) return;

        if (rotationSpoof == null) return;

        event.cancel();

        boolean sprint = mc.player.isSprinting();
        AccessorEntityPlayerSP player = (AccessorEntityPlayerSP) mc.player;

        if (sprint != player.getServerSprintState()) {
            if (sprint) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
            } else {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
            }
        }

        boolean sneak = mc.player.isSneaking();

        if (sneak != player.getServerSneakState()) {
            if (sneak) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            } else {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
        }

        if (PlayerUtil.isCurrentViewEntity()) {
            float yaw = mc.player.cameraYaw;
            float pitch = mc.player.cameraPitch;

            if (rotationSpoof != null) {
                yaw = rotationSpoof.yaw;
                pitch = rotationSpoof.pitch;

                switch (mode.getValString()) {
                    case "Client":
                        mc.player.rotationYaw = yaw;
                        mc.player.rotationPitch = pitch;
                        break;
                    case "Packet":
                    default:
                        mc.player.rotationYawHead = yaw;
                }
            }

            AxisAlignedBB axisalignedbb = mc.player.getEntityBoundingBox();
            double posXDifference = mc.player.posX - player.getLastReportedPosX();
            double posYDifference = axisalignedbb.minY - player.getLastReportedPosY();
            double posZDifference = mc.player.posZ - player.getLastReportedPosZ();
            double yawDifference = (yaw - player.getLastReportedYaw());
            double rotationDifference = (pitch - player.getLastReportedPitch());
            player.setPositionUpdateTicks(player.getPositionUpdateTicks() + 1);
            boolean movedXYZ = posXDifference * posXDifference + posYDifference * posYDifference + posZDifference * posZDifference > 9.0E-4D || player.getPositionUpdateTicks() >= 20;
            boolean movedRotation = yawDifference != 0.0D || rotationDifference != 0.0D;

            if (mc.player.isRiding()) {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.motionX, -999.0D, mc.player.motionZ, yaw, pitch, mc.player.onGround));
                movedXYZ = false;
            } else if (movedXYZ && movedRotation) {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, axisalignedbb.minY, mc.player.posZ, yaw, pitch, mc.player.onGround));
            } else if (movedXYZ) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, axisalignedbb.minY, mc.player.posZ, mc.player.onGround));
            } else if (movedRotation) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, mc.player.onGround));
            } else if (player.getPrevOnGround() != mc.player.onGround) {
                mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
            }

            if (movedXYZ) {
                player.setLastReportedPosX(mc.player.posX);
                player.setLastReportedPosY(axisalignedbb.minY);
                player.setLastReportedPosZ(mc.player.posZ);
                player.setPositionUpdateTicks(0);
            }

            if (movedRotation) {
                player.setLastReportedYaw(yaw);
                player.setLastReportedPitch(pitch);
            }

            player.setPrevOnGround(mc.player.onGround);
            player.setAutoJumpEnabled(mc.gameSettings.autoJump);
        }
    });
}
