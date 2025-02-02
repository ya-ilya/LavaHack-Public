package com.kisman.cc.module.render;

import com.kisman.cc.event.events.TotemPopEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class PopCharms extends Module {
    public final Setting selfPop = new Setting("SelfPop", this, false);
    private final Setting angle = new Setting("Angle", this, false);
    private final Setting angleSpeed = new Setting("AngleSpeed", this, 150, 0, 500, true);
    private final Setting fadeSpeed = new Setting("FadeSpeed", this, 200, 0, 500, true);
    private final Setting width = new Setting("Width", this, Double.longBitsToDouble(Double.doubleToLongBits(0.10667784123174527) ^ 0x7FB34F3D2F4C588FL), Double.longBitsToDouble(Double.doubleToLongBits(2.8356779810862056) ^ 0x7FE6AF77EFF6053EL), Double.longBitsToDouble(Double.doubleToLongBits(0.14239240361793695) ^ 0x7FD639EA0E5E7291L), false);
    private final Setting colorR = new Setting("ColorR", this, 1, 0, 1, false);
    private final Setting colorG = new Setting("ColorG", this, 1, 0, 1, false);
    private final Setting colorB = new Setting("ColorB", this, 1, 0, 1, false);
    private final Setting colorA = new Setting("ColorA", this, 1, 0, 1, false);

    private EntityOtherPlayerMP player;
    private EntityPlayer entity;
    public long startTime;
    public float opacity;
    public long time;
    public long duration;
    public float startAlpha;

    public PopCharms() {
        super("PopCharms", Category.RENDER);

        register(selfPop);
        register(angle);
        register(angleSpeed);
        register(fadeSpeed);
        register(width);
        register(colorR);
        register(colorG);
        register(colorB);
        register(colorA);
    }

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if (!selfPop.getValBoolean() && event.getEntity() == mc.player) return;
        if (!(event.getEntity() instanceof EntityPlayer)) return;

        entity = (EntityPlayer) event.getEntity();

        GameProfile profile = new GameProfile(entity.getUniqueID(), "");
        player = new EntityOtherPlayerMP(mc.world, profile);
        player.copyLocationAndAnglesFrom(entity);
        player.rotationYaw = entity.rotationYaw;
        player.rotationYawHead = entity.rotationYawHead;
        player.rotationPitch = entity.rotationPitch;
        player.prevRotationPitch = entity.prevRotationPitch;
        player.prevRotationYaw = entity.prevRotationYaw;
        player.renderYawOffset = entity.renderYawOffset;
        this.startTime = System.currentTimeMillis();
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (player == null || entity == null) return;
        Color color = new Color(255, 255, 255, 255);
            opacity = Float.intBitsToFloat(Float.floatToIntBits(1.6358529E38f) ^ 0x7EF622C3);
            time = System.currentTimeMillis();
            duration = time - this.startTime;
            startAlpha = (float) color.getAlpha() / 255;
            if (duration < (fadeSpeed.getValInt() * 10L)) opacity = startAlpha - (float)duration / (float)(fadeSpeed.getValInt() * 10);
            if (duration < (fadeSpeed.getValInt() * 10L)) {
            GL11.glPushMatrix();
            if (angle.getValBoolean()) GlStateManager.translate(Float.intBitsToFloat(Float.floatToIntBits(1.240196E38f) ^ 0x7EBA9A9D), ((float)duration / (float)(angleSpeed.getValInt() * 10)), Float.intBitsToFloat(Float.floatToIntBits(3.0414126E38f) ^ 0x7F64CF7A));
            mc.getRenderManager().renderEntityStatic(player, Float.intBitsToFloat(Float.floatToIntBits(6.159893f) ^ 0x7F451DD8), false);
            GlStateManager.translate(Float.intBitsToFloat(Float.floatToIntBits(3.0715237E38f) ^ 0x7F671365), Float.intBitsToFloat(Float.floatToIntBits(1.9152719E37f) ^ 0x7D668ADF), Float.intBitsToFloat(Float.floatToIntBits(1.9703683E38f) ^ 0x7F143BEA));
            GL11.glPopMatrix();
        }
    }
}
