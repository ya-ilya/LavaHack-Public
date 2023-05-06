package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.Colour;
import com.kisman.cc.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class EyeFinder extends Module {
    private final Setting color = new Setting("Color", this, "Color", new Colour(Color.CYAN));
    private final Setting range = new Setting("Range", this, 50, 20, 50, true);

    public EyeFinder() {
        super("EyeFinder", Category.RENDER);

        register(color);
        register(range);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        mc.world.loadedEntityList.stream().filter(entity -> mc.player != entity && !entity.isDead && entity instanceof EntityPlayer && mc.player.getDistance(entity) <= range.getValInt()).forEach(this::drawLine);
    }

    private void drawLine(final Entity e) {
        RayTraceResult result = e.rayTrace(6.0, mc.getRenderPartialTicks());
        if (result == null) return;
        Vec3d eyes = e.getPositionEyes(mc.getRenderPartialTicks());
        GL11.glPushMatrix();
        GlStateManager.enableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        double posX = eyes.x - mc.getRenderManager().renderPosX;
        double posY = eyes.y - mc.getRenderManager().renderPosY;
        double posZ = eyes.z - mc.getRenderManager().renderPosZ;
        double posX2 = result.hitVec.x - mc.getRenderManager().renderPosX;
        double posY2 = result.hitVec.y - mc.getRenderManager().renderPosY;
        double posZ2 = result.hitVec.z - mc.getRenderManager().renderPosZ;
        color.getColour().glColor();
        GlStateManager.glLineWidth(1.5f);
        GL11.glBegin(1);
        GL11.glVertex3d(posX, posY, posZ);
        GL11.glVertex3d(posX2, posY2, posZ2);
        GL11.glVertex3d(posX2, posY2, posZ2);
        GL11.glVertex3d(posX2, posY2, posZ2);
        GL11.glEnd();
        if (result.typeOfHit == RayTraceResult.Type.BLOCK) RenderUtil.drawBlockESP(result.getBlockPos(), color.getColour().r1, color.getColour().g1, color.getColour().b1);
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GL11.glPopMatrix();
    }
}
