package com.kisman.cc.mixin.mixins;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.EventRenderToolTip;
import com.kisman.cc.module.render.NoRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class MixinGuiScreen extends Gui implements GuiYesNoCallback {
    @Shadow public Minecraft mc;
    @Inject(method = "drawDefaultBackground", at = @At("HEAD"), cancellable = true)
    public void drof(CallbackInfo ci) {if(NoRender.instance.guiOverlay.getValBoolean() && mc.world != null && mc.player != null) ci.cancel();}
    @Override public void confirmClicked(boolean b, int i) {}
    @Inject(method = "renderToolTip", at = @At("HEAD"), cancellable = true)
    private void toolTipHook(ItemStack itemStack, int x, int y, CallbackInfo ci) {
        EventRenderToolTip event = new EventRenderToolTip(itemStack, x, y);
        Kisman.EVENT_BUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }
}
