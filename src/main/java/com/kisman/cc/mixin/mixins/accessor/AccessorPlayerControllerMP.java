package com.kisman.cc.mixin.mixins.accessor;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerControllerMP.class)
public interface AccessorPlayerControllerMP {
    @Accessor("stepSoundTickCounter")
    void setStepSoundTickCounter(float counter);

    @Accessor("isHittingBlock")
    void setIsHittingBlock(boolean value);

    @Accessor("blockHitDelay")
    void setBlockHitDelay(int value);

    @Accessor("curBlockDamageMP")
    float getCurBlockDamageMP();
}
