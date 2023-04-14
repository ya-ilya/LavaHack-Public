package com.kisman.cc.mixin.mixins;

import com.kisman.cc.module.render.Animation;
import com.kisman.cc.module.render.JumpCircle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityLivingBase.class, priority = 10000)
public class MixinEntityLivingBase extends Entity {
    @Shadow public EnumHand swingingHand;
    @Shadow public ItemStack activeItemStack;
    @Shadow public float moveStrafing;
    @Shadow public float moveVertical;
    @Shadow public float moveForward;
    @Shadow protected void jump() {}
    @Shadow public boolean isElytraFlying() {return true;}
    @Shadow public  boolean isPotionActive(Potion potionIn) {return false;}
    @Shadow public  PotionEffect getActivePotionEffect(Potion potionIn) {return null;}
    public MixinEntityLivingBase(World worldIn) {super(worldIn);}

    @Shadow @Override protected void entityInit() {}
    @Shadow @Override public void readEntityFromNBT(NBTTagCompound nbtTagCompound) {}
    @Shadow @Override public void writeEntityToNBT(NBTTagCompound nbtTagCompound) {}

    @Inject(method = "jump", at = @At("HEAD"))
    private void onJump(CallbackInfo ci) {
        JumpCircle.instance.handleEntityJump(this);
    }

    @Inject(method = "getArmSwingAnimationEnd", at = @At("HEAD"), cancellable = true)
    private void yesido(CallbackInfoReturnable<Integer> cir) {
        if(Animation.instance.isToggled()) {
            if(isPotionActive(MobEffects.HASTE)) cir.setReturnValue(Animation.instance.speed.getValInt() - (getActivePotionEffect(MobEffects.HASTE).getAmplifier()));
            else cir.setReturnValue(isPotionActive(MobEffects.MINING_FATIGUE) ? Animation.instance.speed.getValInt() + (getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier() + 1) * 2 : Animation.instance.speed.getValInt());
        }
    }
}
