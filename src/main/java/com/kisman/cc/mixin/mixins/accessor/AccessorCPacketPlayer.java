package com.kisman.cc.mixin.mixins.accessor;

import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketPlayer.class)
public interface AccessorCPacketPlayer {
    @Accessor("rotating")
    boolean isRotating();

    @Accessor("onGround")
    void setIsOnGround(boolean onGround);

    @Accessor("y")
    void setY(double y);

    @Accessor("yaw")
    float getYaw();

    @Accessor("yaw")
    void setYaw(float yaw);

    @Accessor("pitch")
    float getPitch();

    @Accessor("pitch")
    void setPitch(float pitch);
}
