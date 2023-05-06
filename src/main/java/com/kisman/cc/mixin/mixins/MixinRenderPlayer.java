package com.kisman.cc.mixin.mixins;

import com.kisman.cc.module.render.NameTags;
import com.kisman.cc.module.render.Reverse;
import com.kisman.cc.module.render.Spin;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {
    @Inject(method = "preRenderCallback*", at = @At("HEAD"))
    public void preRenderCallbackHook(AbstractClientPlayer entitylivingbaseIn, float partialTickTime, CallbackInfo ci) {
        if (Spin.instance.isToggled()) {
            float f = 0.9357f;
            float hue = (float) (System.currentTimeMillis() % 22600L) / 5.0f;

            GlStateManager.scale(f, f, f);

            GlStateManager.rotate(hue, 1, 0, hue);
        } else if (Reverse.instance.isToggled() && !Spin.instance.isToggled()) GlStateManager.rotate(180, 1, 0, 0);
    }

    @Inject(method = "renderEntityName(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDLjava/lang/String;D)V", at = @At("HEAD"), cancellable = true)
    private void renderEntityNameHook(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo ci) {
        if (NameTags.instance.isToggled() && entityIn instanceof EntityPlayer) ci.cancel();
    }
}
