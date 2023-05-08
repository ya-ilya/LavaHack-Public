package com.kisman.cc.mixin.mixins;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.PlayerApplyCollisionEvent;
import com.kisman.cc.event.events.PlayerJumpEvent;
import com.kisman.cc.event.events.PlayerPushedByWaterEvent;
import com.kisman.cc.event.events.PlayerTravelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends Entity {
    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    private void jumpHook(CallbackInfo ci) {
        if (Minecraft.getMinecraft().player.getName().equals(getName())) {
            PlayerJumpEvent event = new PlayerJumpEvent();
            Kisman.EVENT_BUS.post(event);
            if (event.isCancelled()) ci.cancel();
        }
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void travelHook(float strafe, float vertical, float forward, CallbackInfo ci) {
        PlayerTravelEvent event = new PlayerTravelEvent(strafe, vertical, forward);
        Kisman.EVENT_BUS.post(event);

        if (event.isCancelled()) {
            move(MoverType.SELF, motionX, motionY, motionZ);
            ci.cancel();
        }
    }

    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    private void applyEntityCollisionHook(Entity entity, CallbackInfo ci) {
        PlayerApplyCollisionEvent event = new PlayerApplyCollisionEvent(entity);
        Kisman.EVENT_BUS.post(event);

        if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "isPushedByWater()Z", at = @At("HEAD"), cancellable = true)
    private void isPushedByWaterHook(CallbackInfoReturnable<Boolean> cir) {
        PlayerPushedByWaterEvent event = new PlayerPushedByWaterEvent();
        Kisman.EVENT_BUS.post(event);

        if (event.isCancelled()) cir.setReturnValue(false);
    }
}
