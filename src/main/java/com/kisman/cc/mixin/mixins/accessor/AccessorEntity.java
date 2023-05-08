package com.kisman.cc.mixin.mixins.accessor;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface AccessorEntity {
    @Accessor("isInWeb")
    boolean isInWeb();

    @Accessor("isInWeb")
    void setIsInWeb(boolean isInWeb);

    @Accessor("nextStepDistance")
    void setNextStepDistance(int nextStepDistance);
}
