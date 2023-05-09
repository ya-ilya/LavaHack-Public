package com.kisman.cc.mixin.mixins.accessor;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityLiving.class)
public interface AccessorEntityLiving {
    @Accessor("lookHelper")
    EntityLookHelper getLookHelper();

    @Accessor("lookHelper")
    void setLookHelper(EntityLookHelper lookHelper);
}
