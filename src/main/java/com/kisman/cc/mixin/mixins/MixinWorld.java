package com.kisman.cc.mixin.mixins;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.SpawnEntityEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = World.class, priority = 10000)
public class MixinWorld {
    @Inject(method = "onEntityAdded", at = @At("HEAD"))
    public void onEntityAddedHook(Entity entityIn, CallbackInfo ci) {
        Kisman.EVENT_BUS.post(new SpawnEntityEvent(entityIn));
    }
}
