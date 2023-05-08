package com.kisman.cc.mixin.mixins.accessor;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayerSP.class)
public interface AccessorEntityPlayerSP {
    @Accessor("handActive")
    void setHandActive(boolean handActive);

    @Accessor("sprintToggleTimer")
    void setSprintToggleTimer(int timer);
    
    @Accessor("serverSprintState")
    boolean getServerSprintState();
    
    @Accessor("serverSneakState")
    boolean getServerSneakState();
    
    @Accessor("lastReportedPosX")
    double getLastReportedPosX();

    @Accessor("lastReportedPosY")
    double getLastReportedPosY();

    @Accessor("lastReportedPosZ")
    double getLastReportedPosZ();

    @Accessor("lastReportedYaw")
    float getLastReportedYaw();

    @Accessor("lastReportedPitch")
    float getLastReportedPitch();

    @Accessor("lastReportedPosX")
    void setLastReportedPosX(double x);

    @Accessor("lastReportedPosY")
    void setLastReportedPosY(double y);

    @Accessor("lastReportedPosZ")
    void setLastReportedPosZ(double z);

    @Accessor("lastReportedYaw")
    void setLastReportedYaw(float yaw);

    @Accessor("lastReportedPitch")
    void setLastReportedPitch(float pitch);
    
    @Accessor("positionUpdateTicks")
    int getPositionUpdateTicks();
    
    @Accessor("positionUpdateTicks")
    void setPositionUpdateTicks(int ticks);
    
    @Accessor("prevOnGround")
    boolean getPrevOnGround();

    @Accessor("prevOnGround")
    void setPrevOnGround(boolean prevOnGround);
    
    @Accessor("autoJumpEnabled")
    void setAutoJumpEnabled(boolean enabled);
}
