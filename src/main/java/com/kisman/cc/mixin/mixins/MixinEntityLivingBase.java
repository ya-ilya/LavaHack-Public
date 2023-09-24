package com.kisman.cc.mixin.mixins;

import com.kisman.cc.module.render.Animation;
import com.kisman.cc.module.render.JumpCircle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {
    @Shadow public abstract boolean isPotionActive(Potion potionIn);
    @Shadow public abstract PotionEffect getActivePotionEffect(Potion potionIn);

    public MixinEntityLivingBase(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "jump", at = @At("HEAD"))
    private void jumpHook(CallbackInfo ci) {
        JumpCircle.instance.handleEntityJump(this);
    }

    @Inject(method = "getArmSwingAnimationEnd", at = @At("HEAD"), cancellable = true)
    private void getArmSwingAnimationEndHook(CallbackInfoReturnable<Integer> cir) {
        if (Animation.instance.isToggled()) {
            if (isPotionActive(MobEffects.HASTE)) cir.setReturnValue(Animation.instance.speed.getValInt() - (getActivePotionEffect(MobEffects.HASTE).getAmplifier()));
            else cir.setReturnValue(isPotionActive(MobEffects.MINING_FATIGUE) ? Animation.instance.speed.getValInt() + (getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier() + 1) * 2 : Animation.instance.speed.getValInt());
        }
    }
}
