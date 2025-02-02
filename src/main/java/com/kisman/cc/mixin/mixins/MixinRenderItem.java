package com.kisman.cc.mixin.mixins;

import com.kisman.cc.module.render.NoRender;
import com.kisman.cc.module.render.ViewModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.awt.*;

@Mixin(RenderItem.class)
public abstract class MixinRenderItem {
    @Shadow
    protected abstract void renderModel(IBakedModel model, int color, ItemStack stack);

    @Shadow
    protected abstract void renderEffect(IBakedModel model);

    @ModifyArg(method = "renderEffect", at = @At(value="INVOKE", target="net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"), index=1)
    private int renderEffect(int oldValue) {
        return ViewModel.instance.isToggled() && ViewModel.instance.useAlpha.getValBoolean() ? new Color(255, 255, 255, ViewModel.instance.alpha.getValInt()).getRGB() : oldValue;
    }

    /**
     * @author _kisman_
     * @reason NoRender -> Enchant Glint
     */
    @Overwrite
    public void renderItem(ItemStack stack, IBakedModel model) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            if (model.isBuiltInRenderer()) {
                if (ViewModel.instance.isToggled() && ViewModel.instance.useAlpha.getValBoolean()) GlStateManager.color(1, 1, 1, ViewModel.instance.alpha.getValFloat() / 255f);
                else GlStateManager.color(1, 1, 1, 1);
                GlStateManager.enableRescaleNormal();
                stack.getItem().getTileEntityItemStackRenderer().renderByItem(stack);
            } else {
                renderModel(model, ViewModel.instance.isToggled() && ViewModel.instance.useAlpha.getValBoolean() ? new Color(255, 255, 255, ViewModel.instance.alpha.getValInt()).getRGB() : -1, stack);
                if (stack.hasEffect()) {
                    if (!NoRender.instance.isToggled() || !NoRender.instance.enchantGlint.getValBoolean()) {
                        renderEffect(model);
                    }
                }
            }

            GlStateManager.popMatrix();
        }
    }
}
