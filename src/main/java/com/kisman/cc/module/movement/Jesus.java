package com.kisman.cc.module.movement;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.mixin.mixins.accessor.AccessorCPacketPlayer;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.EntityUtil;
import com.kisman.cc.util.MovementUtil;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;

public class Jesus extends Module {
    private final Setting mode = new Setting("Mode", this, "Matrix", new ArrayList<>(Arrays.asList("Matrix", "Matrix 6.3", "MatrixPixel", "Solid")));
    private final Setting speedMatrix = new Setting("Speed Matrix", this, 0.6f, 0, 1, false);
    private final Setting speedSolid = new Setting("Speed Solid", this, 1, 0, 2, false);
    private final Setting speedPixel = new Setting("Speed Pixel", this, 4, 3, 10, true);

    public Jesus() {
        super("Jesus", Category.MOVEMENT);

        register(mode);

        register(speedMatrix);
        register(speedSolid);
        register(speedPixel);
    }

    public void onDisable() {
        EntityUtil.resetTimer();
        if (mc.player == null || mc.world == null) return;
        mc.player.jumpMovementFactor = 0.02f;
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;

        super.setDisplayInfo("[" + mode.getValString() + TextFormatting.GRAY + "]");

        if (mode.getValString().equalsIgnoreCase("Matrix")) {
            float speed = speedMatrix.getValFloat();

            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 0.37f, mc.player.posZ)).getBlock() == Blocks.WATER) {
                mc.player.jump();
                mc.player.jumpMovementFactor = 0;

                mc.player.motionX *= speed;
                mc.player.motionZ *= speed;
                mc.player.onGround = false;

                if (mc.player.isInWater() || mc.player.isInLava()) mc.player.onGround = false;
            }
        } else if (mode.getValString().equalsIgnoreCase("Solid")) {
            float speed = speedSolid.getValFloat();

            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock() == Block.getBlockById(9)) mc.player.motionY = 0.18f;
            else if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 0.0000001, mc.player.posZ)).getBlock() == Block.getBlockById(9)) {
                mc.player.fallDistance = 0.0f;
                mc.player.motionX = 0.0;
                mc.player.jumpMovementFactor = speed;
                mc.player.motionY = 0;
            }
        } else if (mode.getValString().equalsIgnoreCase("Matrix 6.3")) {
            if (mc.player.isInWater())
                if (mc.player.collidedHorizontally || mc.gameSettings.keyBindJump.isPressed()) {
                    mc.player.motionY = 0.09;
                    return;
                }
            if (EntityUtil.isFluid(0.3)) mc.player.motionY = 0.1;
            else if (EntityUtil.isFluid(0.2)) {
                EntityUtil.resetTimer();
                mc.player.motionY = 0.2;
            } else if (EntityUtil.isFluid(0)) {
                EntityUtil.setTimer(0.8f);
                MovementUtil.hClip(1.2);
                mc.player.motionX = 0;
                mc.player.motionZ = 0;
            }
        } else {
            if (EntityUtil.isFluid(-0.1)) MovementUtil.strafe(speedPixel.getValInt());
            if (EntityUtil.isFluid(0.0000001)) {
                mc.player.fallDistance = 0.0f;
                mc.player.motionX = 0.0;
                mc.player.motionZ = 0.0;
                mc.player.motionY = 0.06f;
                mc.player.jumpMovementFactor = 0.01f;
            }
        }
    }

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
        if ((mode.getValString().equalsIgnoreCase("Matrix 6.3") || mode.getValString().equalsIgnoreCase("MatrixPixel")) && event.getPacket() instanceof CPacketPlayer && EntityUtil.isFluid(0.3)) {
            ((AccessorCPacketPlayer) event.getPacket()).setIsOnGround(false);
        }
    });
}
