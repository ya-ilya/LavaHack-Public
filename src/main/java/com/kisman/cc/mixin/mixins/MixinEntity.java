package com.kisman.cc.mixin.mixins;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.PlayerTurnEvent;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity.class, priority = 10000)
public abstract class MixinEntity {
    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    public void turnHook(float yaw, float pitch, CallbackInfo ci) {
        PlayerTurnEvent event = new PlayerTurnEvent(yaw, pitch);
        Kisman.EVENT_BUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }
}
