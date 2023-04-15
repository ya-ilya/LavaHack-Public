package com.kisman.cc.mixin.mixins;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.CrystalAttackEvent;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityEnderCrystal.class)
public class MixinEntityEnderCrystal {
    @Inject(method = "attackEntityFrom", at = @At("RETURN"), cancellable = true)
    public void attackEntityFrom(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getTrueSource() != null) {
            CrystalAttackEvent event = new CrystalAttackEvent(source.getTrueSource().entityId);
            Kisman.EVENT_BUS.post(event);
            if (event.isCancelled()) cir.cancel();
        }
    }
}