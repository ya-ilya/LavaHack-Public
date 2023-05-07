package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.util.RenderUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class TrajectoriesRewrite extends Module {
    public TrajectoriesRewrite() {
        super("TrajectoriesRewrite", Category.RENDER);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        EntityPlayerSP player = mc.player;
        if (player.inventory.getCurrentItem() != null) {
            if (this.isThrowable(player.inventory.getCurrentItem().getItem())) {
                double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) mc.timer.renderPartialTicks - (double) (MathHelper.cos((float) Math.toRadians(mc.player.rotationYaw)) * 0.16F);
                double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double) mc.timer.renderPartialTicks + (double) mc.player.getEyeHeight() - 0.100149011612D;
                double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double) mc.timer.renderPartialTicks - (double) (MathHelper.sin((float) Math.toRadians(mc.player.rotationYaw)) * 0.16F);
                float con = 1.0F;
                if (!(mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow)) con = 0.4F;

                double motionX = (-MathHelper.sin((float) Math.toRadians(mc.player.rotationYaw)) * MathHelper.cos((float) Math.toRadians(mc.player.rotationPitch)) * con);
                double motionZ = (MathHelper.cos((float) Math.toRadians(mc.player.rotationYaw)) * MathHelper.cos((float) Math.toRadians(mc.player.rotationPitch)) * con);
                double motionY = (-MathHelper.sin((float) Math.toRadians(mc.player.rotationPitch)) * con);
                double ssum = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);

                motionX /= ssum;
                motionY /= ssum;
                motionZ /= ssum;

                if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow) {
                    float pow = (float) (72000 - mc.player.getItemInUseCount()) / 20.0F;
                    pow = (pow * pow + pow * 2.0F) / 3.0F;

                    if (pow > 1.0F) pow = 1.0F;

                    if (pow <= 0.1F) pow = 1.0F;

                    pow *= 2.0F;
                    pow *= 1.5F;
                    motionX *= pow;
                    motionY *= pow;
                    motionZ *= pow;
                } else {
                    motionX *= 1.5D;
                    motionY *= 1.5D;
                    motionZ *= 1.5D;
                }

                Vec3d playerVector = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
                GL11.glPushMatrix();
                enableDefaults();
                GL11.glLineWidth(1.8F);
                double r = 0;
                double g = 1;
                double b = 0;
                GL11.glColor3d(r, g, b);
                GL11.glBegin(GL11.GL_LINE_STRIP);
                double gravity = this.getGravity(mc.player.inventory.getCurrentItem().getItem());

                for (int q = 0; q < 1000; ++q) {
                    double rx = x - mc.renderManager.renderPosX;
                    double ry = y - mc.renderManager.renderPosY;
                    double rz = z - mc.renderManager.renderPosZ;
                    GL11.glVertex3d(rx, ry, rz);

                    x += motionX;
                    y += motionY;
                    z += motionZ;
                    motionX *= 0.99D;
                    motionY *= 0.99D;
                    motionZ *= 0.99D;
                    motionY -= gravity;

                    if (mc.world.rayTraceBlocks(playerVector, new Vec3d(x, y, z)) != null) break;
                }

                GL11.glEnd();

                GL11.glTranslated(x - mc.renderManager.renderPosX, y - mc.renderManager.renderPosY, z - mc.renderManager.renderPosZ);
                GL11.glRotatef(mc.player.rotationYaw, 0.0F, (float) (y - mc.renderManager.renderPosY), 0.0F);
                GL11.glTranslated(-(x - mc.renderManager.renderPosX), -(y - mc.renderManager.renderPosY), -(z - mc.renderManager.renderPosZ));
                RenderUtil.drawESP(x - 0.35 - mc.renderManager.renderPosX, y - 0.5 - mc.renderManager.renderPosY, z - 0.5 - mc.renderManager.renderPosZ, r, b, g);
                disableDefaults();
                GL11.glPopMatrix();
            }
        }
    }

    public void enableDefaults() {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glEnable(GL13.GL_MULTISAMPLE);
        GL11.glDepthMask(false);
    }

    public void disableDefaults() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(GL13.GL_MULTISAMPLE);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private double getGravity(Item item) {return item instanceof ItemBow ? 0.05D : 0.03D;}
    private boolean isThrowable(Item item) {return item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemEnderPearl;}

    public void drawLine3D(float var1, float var2, float var3, float var4, float var5, float var6) {
        GL11.glVertex3d(var1, var2, var3);
        GL11.glVertex3d(var4, var5, var6);
    }
}