package com.kisman.cc.module.player.freecam;

import com.kisman.cc.module.render.Breadcrumbs;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class MovementHelper extends Breadcrumbs.Helper {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public MovementHelper(Vec3d vec) {
        super(vec);
    }

    public static boolean isMoving() {
        return MovementHelper.mc.player.movementInput.moveForward != 0.0f || MovementHelper.mc.player.movementInput.moveStrafe != 0.0f;
    }

    public static boolean isUnderBedrock() {
        if (MovementHelper.mc.player.posY <= 3.0) {
            RayTraceResult trace = MovementHelper.mc.world.rayTraceBlocks(MovementHelper.mc.player.getPositionVector(), new Vec3d(MovementHelper.mc.player.posX, 0.0, MovementHelper.mc.player.posZ), false, false, false);
            return trace == null || trace.typeOfHit != RayTraceResult.Type.BLOCK;
        }
        return false;
    }

    public static float getSpeed() {
        return (float)Math.sqrt(MovementHelper.mc.player.motionX * MovementHelper.mc.player.motionX + MovementHelper.mc.player.motionZ * MovementHelper.mc.player.motionZ);
    }

    public static void setSpeed(float speed) {
        float yaw = MovementHelper.mc.player.rotationYaw;
        float forward = MovementHelper.mc.player.movementInput.moveForward;
        float strafe = MovementHelper.mc.player.movementInput.moveStrafe;
        if (forward != 0.0f) {
            if (strafe > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (strafe < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            strafe = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        MovementHelper.mc.player.motionX = (double)(forward * speed) * Math.cos(Math.toRadians(yaw + 90.0f)) + (double)(strafe * speed) * Math.sin(Math.toRadians(yaw + 90.0f));
        MovementHelper.mc.player.motionZ = (double)(forward * speed) * Math.sin(Math.toRadians(yaw + 90.0f)) - (double)(strafe * speed) * Math.cos(Math.toRadians(yaw + 90.0f));
    }

    public static double[] forward(double speed) {
        float forward = MovementHelper.mc.player.movementInput.moveForward;
        float side = MovementHelper.mc.player.movementInput.moveStrafe;
        float yaw = MovementHelper.mc.player.prevRotationYaw + (MovementHelper.mc.player.rotationYaw - MovementHelper.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }
}


