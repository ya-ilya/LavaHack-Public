package com.kisman.cc.mixin.mixins.accessor;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author moneymod
 */

@Mixin(EntityPlayerSP.class)
public interface IEntityPlayerSP {
    @Accessor( "handActive" )
    void setHandActive(boolean value);
}
