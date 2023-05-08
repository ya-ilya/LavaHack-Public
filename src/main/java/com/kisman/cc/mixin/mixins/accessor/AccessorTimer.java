package com.kisman.cc.mixin.mixins.accessor;

import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Timer.class)
public interface AccessorTimer {
    @Accessor("tickLength")
    void setTickLength(float tickLength);

    @Accessor("elapsedTicks")
    void setElapsedTicks(int elapsedTicks);
}
