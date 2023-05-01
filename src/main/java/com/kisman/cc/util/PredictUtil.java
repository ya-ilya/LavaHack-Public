package com.kisman.cc.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class PredictUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static EntityPlayer predictPlayer(EntityPlayer entity, PredictSettings settings) {
        // Position of the player
        double[] posVec = new double[] {entity.posX, entity.posY, entity.posZ};
        // This is likely a temp variable that is going to replace posVec
        double[] newPosVec = posVec.clone();
        // entity motions
        double motionX = entity.posX - entity.prevPosX;
        double motionZ = entity.posZ - entity.prevPosZ;

        for(int i = 0; i < settings.tick; i++) {
            RayTraceResult result;

            newPosVec = posVec.clone();
            newPosVec[0] += motionX;
            newPosVec[2] += motionZ;
            result = mc.world.rayTraceBlocks(new Vec3d(posVec[0], posVec[1], posVec[2]), new Vec3d(newPosVec[0], posVec[1], newPosVec[2]));
            if (result == null || result.typeOfHit == RayTraceResult.Type.ENTITY) {
                posVec = newPosVec.clone();
            }
        }

        EntityOtherPlayerMP clonedPlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("fdee323e-7f0c-4c15-8d1c-0f277442342a"), entity.getName()));
        clonedPlayer.setPosition(posVec[0], posVec[1], posVec[2]);
        clonedPlayer.inventory.copyInventory(entity.inventory);
        clonedPlayer.setHealth(entity.getHealth());
        clonedPlayer.prevPosX = entity.prevPosX;
        clonedPlayer.prevPosY = entity.prevPosY;
        clonedPlayer.prevPosZ = entity.prevPosZ;
        for(PotionEffect effect : entity.getActivePotionEffects()) clonedPlayer.addPotionEffect(effect);
        return clonedPlayer;
    }

    public static class PredictSettings {
        final int tick;
        final int startDecrease;
        final int exponentStartDecrease;
        final int decreaseY;
        final int exponentDecreaseY;
        final int increaseY;
        final int exponentIncreaseY;
        final int width;
        final int nStairs;
        final double speedActivationStairs;
        
        public PredictSettings(int tick, int startDecrease, int exponentStartDecrease, int decreaseY, int exponentDecreaseY,
                               int increaseY, int exponentIncreaseY, int width, int nStairs, double speedActivationStairs) {
            this.tick = tick;
            this.startDecrease = startDecrease;
            this.exponentStartDecrease = exponentStartDecrease;
            this.decreaseY = decreaseY;
            this.exponentDecreaseY = exponentDecreaseY;
            this.increaseY = increaseY;
            this.exponentIncreaseY = exponentIncreaseY;
            this.width = width;
            this.nStairs = nStairs;
            this.speedActivationStairs = speedActivationStairs;
        }
    }
}
