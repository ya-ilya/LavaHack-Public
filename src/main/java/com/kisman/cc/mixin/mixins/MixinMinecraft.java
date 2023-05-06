package com.kisman.cc.mixin.mixins;

import com.kisman.cc.Kisman;
import com.kisman.cc.mixin.mixins.accessor.AccessorPlayerControllerMP;
import com.kisman.cc.mixin.mixins.accessor.IEntityPlayerSP;
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

@Mixin(value = Minecraft.class, priority = 10000)
public abstract class MixinMinecraft {
    @Shadow
    public GameSettings gameSettings;
    @Shadow
    public EntityPlayerSP player;
    @Shadow
    public PlayerControllerMP playerController;
    private boolean mt_handActive = false;
    private boolean mt_isHittingBlock = false;

    @Shadow
    protected abstract void clickMouse();

    @Inject(method = "init", at = @At("RETURN"))
    private void initHook(CallbackInfo ci) {
        try {
            Kisman.instance.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(method = "processKeyBinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z", shift = At.Shift.BEFORE, ordinal = 2))
    public void processKeyBindsHook(CallbackInfo info) {
        if (MultiTask.instance.isToggled()) {
            while (gameSettings.keyBindAttack.isPressed()) {
                clickMouse();
            }
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
            ((IEntityPlayerSP) player).setHandActive(false);
        }
    }

    @Inject(method = "sendClickBlockToController", at = @At("RETURN"))
    public void sendClickBlockToControllerPostHook(boolean leftClick, CallbackInfo ci) {
        if (MultiTask.instance.isToggled() && !player.isHandActive())
            ((IEntityPlayerSP) player).setHandActive(mt_handActive);
    }
}
