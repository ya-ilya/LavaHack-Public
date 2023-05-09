package com.kisman.cc.mixin.mixins.accessor;

import net.minecraft.entity.ai.EntityLookHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityLookHelper.class)
public interface AccessorEntityLookHelper {
    @Invoker("updateRotation")
    float invokeUpdateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_);

    @Accessor("isLooking")
    void setIsLooking(boolean isLooking);

    @Accessor("posX")
    double getPosX();

    @Accessor("posY")
    double getPosY();

    @Accessor("posZ")
    double getPosZ();

    @Accessor("deltaLookYaw")
    float getDeltaLookYaw();

    @Accessor("deltaLookPitch")
    float getDeltaLookPitch();
}
