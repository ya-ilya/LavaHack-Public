package com.kisman.cc.mixin.mixins;

import com.kisman.cc.Kisman;
import com.kisman.cc.mixin.mixins.accessor.AccessorEntityPlayerSP;
import com.kisman.cc.mixin.mixins.accessor.AccessorPlayerControllerMP;
import com.kisman.cc.module.exploit.MultiTask;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    private boolean mt_handActive = false;
    private boolean mt_isHittingBlock = false;

    @Inject(method = "init", at = @At("RETURN"))
    private void initHook(CallbackInfo ci) {
        try {
            Kisman.instance.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(method = "rightClickMouse", at = @At("HEAD"))
    public void rightClickMouseHook(CallbackInfo info) {
        Minecraft mc = Minecraft.getMinecraft();

        if (MultiTask.instance.isToggled()) {
            mt_isHittingBlock = mc.playerController.getIsHittingBlock();
            ((AccessorPlayerControllerMP) mc.playerController).setIsHittingBlock(false);
        }
    }

    @Inject(method = "rightClickMouse", at = @At("RETURN"))
    public void rightClickMousePostHook(CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();

        if (MultiTask.instance.isToggled() && !mc.playerController.getIsHittingBlock())
            ((AccessorPlayerControllerMP) mc.playerController).setIsHittingBlock(mt_isHittingBlock);
    }

    @Inject(method = "sendClickBlockToController", at = @At("HEAD"))
    public void sendClickBlockToControllerHook(boolean leftClick, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();

        if (MultiTask.instance.isToggled()) {
            mt_handActive = mc.player.isHandActive();
            ((AccessorEntityPlayerSP) mc.player).setHandActive(false);
        }
    }

    @Inject(method = "sendClickBlockToController", at = @At("RETURN"))
    public void sendClickBlockToControllerPostHook(boolean leftClick, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();

        if (MultiTask.instance.isToggled() && !mc.player.isHandActive())
            ((AccessorEntityPlayerSP) mc.player).setHandActive(mt_handActive);
    }
}
