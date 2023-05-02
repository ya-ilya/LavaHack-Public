package com.kisman.cc.util;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PlayerUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void swingArm(Hand hand) {
        switch (hand) {
            case MAINHAND:
                mc.player.swingArm(EnumHand.MAIN_HAND);
                break;
            case OFFHAND:
                mc.player.swingArm(EnumHand.OFF_HAND);
                break;
            case PACKET:
                mc.player.connection.sendPacket(new CPacketAnimation(mc.player.getHeldItemMainhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND));
                break;
            case NONE:
                break;
        }
    }

    public static void fakeJump(int packets) {
        if (packets > 0 && packets != 5) mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
        if (packets > 1) mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + .419999986887, mc.player.posZ, true));
        if (packets > 2) mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + .7531999805212, mc.player.posZ, true));
        if (packets > 3) mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.0013359791121, mc.player.posZ, true));
        if (packets > 4) mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.1661092609382, mc.player.posZ, true));
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static BlockPos getPlayerPos(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    // TechAle: Return the health of the player
    public static float getHealth() {
        return mc.player.getHealth() + mc.player.getAbsorptionAmount();
    }

    public static void centerPlayer(Vec3d centeredBlock) {
        double xDeviation = Math.abs(centeredBlock.x - mc.player.posX);
        double zDeviation = Math.abs(centeredBlock.z - mc.player.posZ);

        if (xDeviation <= 0.1 && zDeviation <= 0.1) centeredBlock = Vec3d.ZERO;
        else {
            double newX = -2;
            double newZ = -2;
            int xRel = (mc.player.posX < 0 ? -1 : 1);
            int zRel = (mc.player.posZ < 0 ? -1 : 1);
            if (BlockUtil.getBlock(mc.player.posX, mc.player.posY - 1, mc.player.posZ) instanceof BlockAir) {
                if (Math.abs((mc.player.posX % 1)) * 1E2 <= 30) newX = Math.round(mc.player.posX - (0.3 * xRel)) + 0.5 * -xRel;
                else if (Math.abs((mc.player.posX % 1)) * 1E2 >= 70) newX = Math.round(mc.player.posX + (0.3 * xRel)) - 0.5 * -xRel;
                if (Math.abs((mc.player.posZ % 1)) * 1E2 <= 30) newZ = Math.round(mc.player.posZ - (0.3 * zRel)) + 0.5 * -zRel;
                else if (Math.abs((mc.player.posZ % 1)) * 1E2 >= 70) newZ = Math.round(mc.player.posZ + (0.3 * zRel)) - 0.5 * -zRel;
            }

            if (newX == -2)
                if (mc.player.posX > Math.round(mc.player.posX)) {
                    newX = Math.round(mc.player.posX) + 0.5;
                }
                // (mc.player.posX % 1)*1E2 < 30
                else if (mc.player.posX < Math.round(mc.player.posX)) {
                    newX = Math.round(mc.player.posX) - 0.5;
                } else {
                    newX = mc.player.posX;
                }

            if (newZ == -2)
                if (mc.player.posZ > Math.round(mc.player.posZ)) {
                    newZ = Math.round(mc.player.posZ) + 0.5;
                } else if (mc.player.posZ < Math.round(mc.player.posZ)) {
                    newZ = Math.round(mc.player.posZ) - 0.5;
                } else {
                    newZ = mc.player.posZ;
                }

            mc.player.connection.sendPacket(new CPacketPlayer.Position(newX, mc.player.posY, newZ, true));
            mc.player.setPosition(newX, mc.player.posY, newZ);
        }
    }

    public static double[] forward(final double speed) {
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.player != null && mc.player.isPotionActive(Potion.getPotionById(1))) {
            final int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public static boolean isMoving(EntityLivingBase entity) {
        return entity.moveForward != 0 || entity.moveStrafing != 0;
    }

    public static void setSpeed(final EntityLivingBase entity, final double speed) {
        double[] dir = forward(speed);
        entity.motionX = dir[0];
        entity.motionZ = dir[1];
    }

    public static boolean IsEating() {
        return mc.player != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemFood && mc.player.isHandActive() && mc.player.getActiveHand().equals(EnumHand.MAIN_HAND);
    }

    public static boolean isEatingOffhand() {
        return mc.player != null && mc.player.getHeldItemOffhand().getItem() instanceof ItemFood && mc.player.isHandActive() && mc.player.getActiveHand().equals(EnumHand.OFF_HAND);
    }

    public static boolean isCurrentViewEntity() {
        return mc.getRenderViewEntity() == mc.player;
    }

    public static BlockPos GetLocalPlayerPosFloored() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static int GetItemSlot(Item input) {
        if (mc.player == null) return 0;

        for (int i = 0; i < mc.player.inventoryContainer.getInventory().size(); ++i) {
            if (i == 0 || i == 5 || i == 6 || i == 7 || i == 8) continue;

            ItemStack s = mc.player.inventoryContainer.getInventory().get(i);

            if (s.isEmpty()) continue;
            if (s.getItem() == input) return i;
        }
        return -1;
    }

    public static int GetRecursiveItemSlot(Item input) {
        if (mc.player == null) return 0;

        for (int i = mc.player.inventoryContainer.getInventory().size() - 1; i > 0; --i) {
            if (i == 5 || i == 6 || i == 7 || i == 8) continue;

            ItemStack s = mc.player.inventoryContainer.getInventory().get(i);

            if (s.isEmpty()) continue;
            if (s.getItem() == input) return i;
        }
        return -1;
    }

    public enum Hand {
        MAINHAND, OFFHAND, PACKET, NONE
    }
}
