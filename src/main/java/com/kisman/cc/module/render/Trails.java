package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.TimerUtil;
import com.kisman.cc.util.gish.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class Trails extends Module {
    private final Setting removeTicks = new Setting("RemoveTicks", this, 1, 1, 50, true);

    private final ArrayList<TrailUtil> bcs = new ArrayList<>();

    public Trails() {
        super("Trails", Category.RENDER);

        register(removeTicks);
    }

    public void onEnable() {
        bcs.clear();
    }

    public boolean isBeta() {return true;}

    public void update() {
        if(mc.player == null || mc.world == null) return;

        bcs.add(new TrailUtil(mc.player.getPositionVector()));
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        int time = removeTicks.getValInt() * 500;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        mc.entityRenderer.disableLightmap();

        GL11.glLineWidth(3);
        GlStateManager.resetColor();
        double lastPosX = 114514.0;
        double lastPosY = 114514.0;
        double lastPosZ = 114514.0;
        GL11.glBegin(GL11.GL_QUADS);
        for (int i = 0; i < bcs.size(); i++) {
            TrailUtil bc = bcs.get(i);
            ColorUtil.glColor(ColorUtil.astolfoColors(100, 100), 100);

            if (bc.getTimer().hasReached(time)) {
                bcs.remove(bc);
            }

            double renderPosX = mc.getRenderManager().renderPosX;
            double renderPosY = mc.getRenderManager().renderPosY;
            double renderPosZ = mc.getRenderManager().renderPosZ;

            if (!(lastPosX == 114514.0 && lastPosY == 114514.0 && lastPosZ == 114514.0)) {
                GL11.glVertex3d(bc.getVector().x - renderPosX, bc.getVector().y - renderPosY, bc.getVector().z - renderPosZ);
                GL11.glVertex3d(lastPosX, lastPosY, lastPosZ);
                GL11.glVertex3d(lastPosX, lastPosY + mc.player.height, lastPosZ);
                GL11.glVertex3d(bc.getVector().x - renderPosX, bc.getVector().y - renderPosY + mc.player.height, bc.getVector().z - renderPosZ);
            }
            lastPosX = bc.getVector().x  - renderPosX;
            lastPosY = bc.getVector().y- renderPosY;
            lastPosZ = bc.getVector().z - renderPosZ;
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static class TrailUtil {
        private final Vec3d vector;
        private final TimerUtil timer;

        public TrailUtil (Vec3d vector) {
            timer = new TimerUtil();
            this.vector = vector;
        }

        public TimerUtil getTimer() {
            return timer;
        }

        public Vec3d getVector() {
            return vector;
        }
    }
}
