package com.kisman.cc.mixin.mixins;

import com.kisman.cc.module.render.HotbarModifier;
import com.kisman.cc.module.render.NoRender;
import com.kisman.cc.util.Render2DUtil;
import com.kisman.cc.util.gish.ColorUtil;
import com.kisman.cc.util.render.objects.AbstractGradient;
import com.kisman.cc.util.render.objects.Vec4d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame extends Gui {
    @Shadow
    @Final
    protected static ResourceLocation WIDGETS_TEX_PATH;
    @Shadow
    @Final
    protected Minecraft mc;

    @Shadow
    protected abstract void renderHotbarItem(int x, int y, float partialTicks, EntityPlayer player, ItemStack stack);

    @Inject(method = "renderPortal", at = @At("HEAD"), cancellable = true)
    protected void renderPortalHook(float timeInPortal, ScaledResolution scaledRes, CallbackInfo callbackInfo) {
        if (NoRender.instance.isToggled() && NoRender.instance.portal.getValBoolean()) callbackInfo.cancel();
    }

    @Inject(method = "renderPumpkinOverlay", at = @At("HEAD"), cancellable = true)
    protected void renderPumpkinOverlayHook(ScaledResolution scaledRes, CallbackInfo callbackInfo) {
        if (NoRender.instance.isToggled() && NoRender.instance.overlay.getValBoolean()) callbackInfo.cancel();
    }

    @Inject(method = "renderPotionEffects", at = @At("HEAD"), cancellable = true)
    protected void renderPotionEffectsHook(ScaledResolution scaledRes, CallbackInfo callbackInfo) {
        if (NoRender.instance.isToggled() && NoRender.instance.overlay.getValBoolean()) callbackInfo.cancel();
    }

    /**
     * @author _kisman_
     * @reason .
     * Credits: wild
     */
    @Overwrite
    protected void renderHotbar(ScaledResolution sr, float partialTicks) {
        if (HotbarModifier.instance.isToggled()) {
            Color backgroundColor = new Color(31, 31, 31, 152);
            if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.mc.getTextureManager().bindTexture(WIDGETS_TEX_PATH);
                EntityPlayer entityplayer = (EntityPlayer) this.mc.getRenderViewEntity();
                ItemStack itemstack = entityplayer.getHeldItemOffhand();
                EnumHandSide enumhandside = entityplayer.getPrimaryHand().opposite();
                int i = sr.getScaledWidth() / 2;
                float f = this.zLevel;
                this.zLevel = -90.0F;
                Render2DUtil.drawRectWH(i - 91, sr.getScaledHeight() - 22, 182, 22, backgroundColor.getRGB());
                double[] selectedCoords = new double[]{i - 91 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22, 22, 22};
                Render2DUtil.drawAbstract(new AbstractGradient(new Vec4d(new double[]{selectedCoords[0], selectedCoords[1]}, new double[]{selectedCoords[0] + selectedCoords[2], selectedCoords[1]}, new double[]{selectedCoords[0] + selectedCoords[2], selectedCoords[1] + selectedCoords[3]}, new double[]{selectedCoords[0], selectedCoords[1] + selectedCoords[3]}), ColorUtil.injectAlpha(backgroundColor, 1), HotbarModifier.getPrimaryColor(), true));
                if (!itemstack.isEmpty()) {
                    if (enumhandside == EnumHandSide.LEFT) {
                        if (!HotbarModifier.instance.offhand.getValBoolean())
                            this.drawTexturedModalRect(i - 91 - 29, sr.getScaledHeight() - 23, 24, 22, 29, 24);
                        else {
                            Render2DUtil.drawRectWH(i - 91 - 29, sr.getScaledHeight() - 22, 22, 22, backgroundColor.getRGB());
                            double[] selectedCoordsOffhand = new double[]{i - 91 - 29, sr.getScaledHeight() - 22, 22, 22};
                            if (HotbarModifier.instance.offhandGradient.getValBoolean())
                                Render2DUtil.drawAbstract(new AbstractGradient(new Vec4d(new double[]{selectedCoordsOffhand[0], selectedCoordsOffhand[1]}, new double[]{selectedCoordsOffhand[0] + selectedCoordsOffhand[2], selectedCoordsOffhand[1]}, new double[]{selectedCoordsOffhand[0] + selectedCoordsOffhand[2], selectedCoordsOffhand[1] + selectedCoordsOffhand[3]}, new double[]{selectedCoordsOffhand[0], selectedCoordsOffhand[1] + selectedCoordsOffhand[3]}), ColorUtil.injectAlpha(backgroundColor, 1), HotbarModifier.getPrimaryColor(), true));
                        }
                    } else {
                        if (!HotbarModifier.instance.offhand.getValBoolean())
                            this.drawTexturedModalRect(i + 91, sr.getScaledHeight() - 23, 53, 22, 29, 24);
                        else {
                            Render2DUtil.drawRectWH(i + 91 + 7, sr.getScaledHeight() - 22, 22, 22, backgroundColor.getRGB());
                            double[] selectedCoordsOffhand = new double[]{i + 91 + 7, sr.getScaledHeight() - 22, 22, 22};
                            if (HotbarModifier.instance.offhandGradient.getValBoolean())
                                Render2DUtil.drawAbstract(new AbstractGradient(new Vec4d(new double[]{selectedCoordsOffhand[0], selectedCoordsOffhand[1]}, new double[]{selectedCoordsOffhand[0] + selectedCoordsOffhand[2], selectedCoordsOffhand[1]}, new double[]{selectedCoordsOffhand[0] + selectedCoordsOffhand[2], selectedCoordsOffhand[1] + selectedCoordsOffhand[3]}, new double[]{selectedCoordsOffhand[0], selectedCoordsOffhand[1] + selectedCoordsOffhand[3]}), ColorUtil.injectAlpha(backgroundColor, 1), new Color(255, 255, 255, 152), true));
                        }
                    }
                }

                this.zLevel = f;
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                RenderHelper.enableGUIStandardItemLighting();

                int l1;
                int i2;
                int j2;
                for (l1 = 0; l1 < 9; ++l1) {
                    i2 = i - 90 + l1 * 20 + 2;
                    j2 = sr.getScaledHeight() - 16 - 3;
                    this.renderHotbarItem(i2, j2, partialTicks, entityplayer, entityplayer.inventory.mainInventory.get(l1));
                }

                if (!itemstack.isEmpty()) {
                    l1 = sr.getScaledHeight() - 16 - 3;
                    if (enumhandside == EnumHandSide.LEFT)
                        this.renderHotbarItem(i - 91 - 26, l1, partialTicks, entityplayer, itemstack);
                    else this.renderHotbarItem(i + 91 + 10, l1, partialTicks, entityplayer, itemstack);
                }

                if (this.mc.gameSettings.attackIndicator == 2) {
                    float f1 = this.mc.player.getCooledAttackStrength(0.0F);
                    if (f1 < 1.0F) {
                        i2 = sr.getScaledHeight() - 20;
                        j2 = i + 91 + 6;
                        if (enumhandside == EnumHandSide.RIGHT) j2 = i - 91 - 22;
                        this.mc.getTextureManager().bindTexture(Gui.ICONS);
                        int k1 = (int) (f1 * 19.0F);
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                        this.drawTexturedModalRect(j2, i2, 0, 94, 18, 18);
                        this.drawTexturedModalRect(j2, i2 + 18 - k1, 18, 112 - k1, 18, k1);
                    }
                }

                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
            }
        } else {
            if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.mc.getTextureManager().bindTexture(WIDGETS_TEX_PATH);
                EntityPlayer entityplayer = (EntityPlayer) this.mc.getRenderViewEntity();
                ItemStack itemstack = entityplayer.getHeldItemOffhand();
                EnumHandSide enumhandside = entityplayer.getPrimaryHand().opposite();
                int i = sr.getScaledWidth() / 2;
                float f = this.zLevel;
                this.zLevel = -90.0F;
                this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
                this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
                if (!itemstack.isEmpty()) {
                    if (enumhandside == EnumHandSide.LEFT)
                        this.drawTexturedModalRect(i - 91 - 29, sr.getScaledHeight() - 23, 24, 22, 29, 24);
                    else this.drawTexturedModalRect(i + 91, sr.getScaledHeight() - 23, 53, 22, 29, 24);
                }

                this.zLevel = f;
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                RenderHelper.enableGUIStandardItemLighting();

                int l1;
                int i2;
                int j2;
                for (l1 = 0; l1 < 9; ++l1) {
                    i2 = i - 90 + l1 * 20 + 2;
                    j2 = sr.getScaledHeight() - 16 - 3;
                    this.renderHotbarItem(i2, j2, partialTicks, entityplayer, entityplayer.inventory.mainInventory.get(l1));
                }

                if (!itemstack.isEmpty()) {
                    l1 = sr.getScaledHeight() - 16 - 3;
                    if (enumhandside == EnumHandSide.LEFT)
                        this.renderHotbarItem(i - 91 - 26, l1, partialTicks, entityplayer, itemstack);
                    else this.renderHotbarItem(i + 91 + 10, l1, partialTicks, entityplayer, itemstack);
                }

                if (this.mc.gameSettings.attackIndicator == 2) {
                    float f1 = this.mc.player.getCooledAttackStrength(0.0F);
                    if (f1 < 1.0F) {
                        i2 = sr.getScaledHeight() - 20;
                        j2 = i + 91 + 6;
                        if (enumhandside == EnumHandSide.RIGHT) j2 = i - 91 - 22;
                        this.mc.getTextureManager().bindTexture(Gui.ICONS);
                        int k1 = (int) (f1 * 19.0F);
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                        this.drawTexturedModalRect(j2, i2, 0, 94, 18, 18);
                        this.drawTexturedModalRect(j2, i2 + 18 - k1, 18, 112 - k1, 18, k1);
                    }
                }

                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
            }
        }
    }
}
