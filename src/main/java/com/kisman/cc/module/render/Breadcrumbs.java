package com.kisman.cc.module.render;

import com.kisman.cc.mixin.mixins.accessor.AccessorRenderManager;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.PlayerUtil;
import com.kisman.cc.util.TimerUtil;
import com.kisman.cc.util.gish.ColorUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class Breadcrumbs extends Module {
    private final Setting lineWidth = new Setting("Line Width", this, 1, 1, 6, true);
    private final Setting removeTicks =  new Setting("Remove Ticks", this, 10, 0, 50, true);
    private final Setting alpha = new Setting("Alpha", this, 100, 0, 255, true);

    private final ArrayList<Helper> positions = new ArrayList<>();

    public Breadcrumbs() {
        super("Breadcrumbs", Category.RENDER);

        register(lineWidth);
        register(removeTicks);
        register(alpha);
    }

    public void onEnable() {positions.clear();}
    public void onDisable() {positions.clear();}

    public void update() {
        if (mc.player == null || mc.world == null) return;

        if (PlayerUtil.isMoving(mc.player)) positions.add(new Helper(mc.player.getPositionVector()));
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        mc.entityRenderer.disableLightmap();

        GL11.glLineWidth(lineWidth.getValFloat());
        GlStateManager.resetColor();
        double lastPosX = 114514.0;
        double lastPosY = 114514.0;
        double lastPosZ = 114514.0;
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        for (int i = 0; i < positions.size(); i++) {
            Helper bc = positions.get(i);
            ColorUtil.glColor(ColorUtil.astolfoColors(100, 100), alpha.getValInt());

            if (bc.timer.hasReached(removeTicks.getValInt())) positions.remove(bc);

            AccessorRenderManager accessorRenderManager = (AccessorRenderManager) mc.getRenderManager();
            double renderPosX = accessorRenderManager.getRenderPosX();
            double renderPosY = accessorRenderManager.getRenderPosY();
            double renderPosZ = accessorRenderManager.getRenderPosZ();

            if (!(lastPosX == 114514.0 && lastPosY == 114514.0 && lastPosZ == 114514.0)) {
                GL11.glVertex3d(bc.getVector().x - renderPosX, bc.getVector().y - renderPosY, bc.getVector().z - renderPosZ);
                GL11.glVertex3d(lastPosX, lastPosY, lastPosZ);
                GL11.glVertex3d(lastPosX, lastPosY + mc.player.height, lastPosZ);
                GL11.glVertex3d(bc.getVector().z - renderPosX, bc.getVector().y - renderPosY + mc.player.height, bc.getVector().z - renderPosZ);
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

    public static class Helper {
        public TimerUtil timer;
        public Vec3d vec;

        public Helper(Vec3d vec) {
            this.timer = new TimerUtil();
            this.vec = vec;
        }

        public Vec3d getVector() {
            return vec;
        }
    }
}
