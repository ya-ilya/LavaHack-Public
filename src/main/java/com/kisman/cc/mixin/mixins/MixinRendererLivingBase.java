package com.kisman.cc.mixin.mixins;

import com.kisman.cc.module.combat.AutoRer;
import com.kisman.cc.module.combat.KillAura;
import com.kisman.cc.module.combat.autocrystal.AutoCrystal;
import com.kisman.cc.module.misc.Optimizer;
import com.kisman.cc.module.render.Charms;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

@Mixin(value = RenderLivingBase.class, priority = 10000)
public abstract class MixinRendererLivingBase<T extends EntityLivingBase> extends Render<T> {
    @Shadow protected ModelBase mainModel;

    protected MixinRendererLivingBase(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At("HEAD"), cancellable = true)
    private void doRenderHook(T f3, double flag1, double flag, double f, float f1, float f2, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world != null && mc.player != null && Optimizer.instance.isToggled() && Optimizer.instance.customEntityRenderRange.getValBoolean() && mc.player.getDistance(f3) > Optimizer.instance.entityRenderRange.getValInt()) ci.cancel();
    }

    /**
     * @author kshk
     * @reason charms
     */
    @Overwrite
    protected void renderModel(T p_renderModel_1_, float p_renderModel_2_, float p_renderModel_3_, float p_renderModel_4_, float p_renderModel_5_, float p_renderModel_6_, float p_renderModel_7_) {
        boolean flag = this.isVisible(p_renderModel_1_);
        boolean flag1 = !flag && !p_renderModel_1_.isInvisibleToPlayer(Minecraft.getMinecraft().player);

        if (flag || flag1) {
            if (!this.bindEntityTexture(p_renderModel_1_)) return;
            if (flag1) GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            if (Charms.instance.isToggled() && p_renderModel_1_ instanceof EntityPlayer) {
                glPushMatrix();

                glEnable(GL_POLYGON_OFFSET_LINE);

                if (Charms.instance.polygonOffset.getValBoolean()) glPolygonOffset(1.0f, 1000000);

                glDisable(GL_TEXTURE_2D);
                glDisable(GL_LIGHTING);

                if (Charms.instance.customColor.getValBoolean()) {
                    Setting color = Charms.instance.color;
                    if (Charms.instance.targetRender.getValBoolean()) {
                        if (AutoRer.currentTarget == p_renderModel_1_ || KillAura.instance.target == p_renderModel_1_ || AutoCrystal.instance.target == p_renderModel_1_) glColor4f(0.6f, 0, 1, color.getColour().a1);
                        else color.getColour().glColor();
                    } else color.getColour().glColor();
                }

                glDisable(GL_DEPTH_TEST);
                this.mainModel.render(p_renderModel_1_, p_renderModel_2_, p_renderModel_3_, p_renderModel_4_, p_renderModel_5_, p_renderModel_6_, p_renderModel_7_);
                glEnable(GL_DEPTH_TEST);
                this.mainModel.render(p_renderModel_1_, p_renderModel_2_, p_renderModel_3_, p_renderModel_4_, p_renderModel_5_, p_renderModel_6_, p_renderModel_7_);

                if (Charms.instance.customColor.getValBoolean()) RenderUtil.setupColor(new Color(0xFFFFFFF).hashCode());

                glEnable(GL_TEXTURE_2D);
                glEnable(GL_LIGHTING);
                glDisable(GL_POLYGON_OFFSET_LINE);
                glPopMatrix();

            } else this.mainModel.render(p_renderModel_1_, p_renderModel_2_, p_renderModel_3_, p_renderModel_4_, p_renderModel_5_, p_renderModel_6_, p_renderModel_7_);

            if (flag1) GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
        }
    }

    protected boolean isVisible(T p_isVisible_1_) {
        return !p_isVisible_1_.isInvisible() || this.renderOutlines;
    }

    @Nullable @Override protected ResourceLocation getEntityTexture(T t) {return null;}
}
