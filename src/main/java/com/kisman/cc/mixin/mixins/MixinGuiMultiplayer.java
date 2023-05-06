package com.kisman.cc.mixin.mixins;

import com.kisman.cc.gui.alts.AltManagerGUI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiMultiplayer.class, priority = 10000)
public class MixinGuiMultiplayer extends GuiScreen {
    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGuiHook(CallbackInfo ci) {
        buttonList.add(new GuiButton(417, 7, 7, 60, 20, "Alts"));
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    public void actionPerformedHook(GuiButton button, CallbackInfo ci) {
        if(button.id == 417) mc.displayGuiScreen(new AltManagerGUI(this));
    }
}
