package com.kisman.cc.mixin.mixins;

import com.kisman.cc.Kisman;
import com.kisman.cc.mixin.mixins.accessor.AccessorEntityPlayerSP;
import com.kisman.cc.mixin.mixins.accessor.AccessorPlayerControllerMP;
import com.kisman.cc.module.exploit.MultiTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    public EntityPlayerSP player;
    @Shadow
    public PlayerControllerMP playerController;

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
        if (MultiTask.instance.isToggled()) {
            mt_isHittingBlock = playerController.getIsHittingBlock();
            ((AccessorPlayerControllerMP) playerController).setIsHittingBlock(false);
        }
    }

    @Inject(method = "rightClickMouse", at = @At("RETURN"))
    public void rightClickMousePostHook(CallbackInfo ci) {
        if (MultiTask.instance.isToggled() && !playerController.getIsHittingBlock())
            ((AccessorPlayerControllerMP) playerController).setIsHittingBlock(mt_isHittingBlock);
    }

    @Inject(method = "sendClickBlockToController", at = @At("HEAD"))
    public void sendClickBlockToControllerHook(boolean leftClick, CallbackInfo ci) {
        if (MultiTask.instance.isToggled()) {
            mt_handActive = player.isHandActive();
            ((AccessorEntityPlayerSP) player).setHandActive(false);
        }
    }

    @Inject(method = "sendClickBlockToController", at = @At("RETURN"))
    public void sendClickBlockToControllerPostHook(boolean leftClick, CallbackInfo ci) {
        if (MultiTask.instance.isToggled() && !player.isHandActive())
            ((AccessorEntityPlayerSP) player).setHandActive(mt_handActive);
    }
}
