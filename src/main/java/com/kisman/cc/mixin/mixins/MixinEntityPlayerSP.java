package com.kisman.cc.mixin.mixins;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.Event;
import com.kisman.cc.event.events.PlayerMotionUpdateEvent;
import com.kisman.cc.event.events.PlayerMoveEvent;
import com.kisman.cc.event.events.PlayerPushOutOfBlocksEvent;
import com.kisman.cc.event.events.PlayerUpdateEvent;
import com.kisman.cc.module.movement.NoSlowSneak;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends MixinAbstractClientPlayer {
    public MixinEntityPlayerSP(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    @Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
    private void isSneakingHook(CallbackInfoReturnable<Boolean> cir) {
        if (NoSlowSneak.instance.isToggled() && NoSlowSneak.instance.mode.checkValString(NoSlowSneak.Mode.Cancel.name())) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void moveHook(MoverType type, double x, double y, double z, CallbackInfo ci) {
        PlayerMoveEvent event = new PlayerMoveEvent(type, x, y, z);
        Kisman.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            move(type, event.getX(), event.getY(), event.getX());
            ci.cancel();
        }
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void onPreUpdateWalkingPlayerHook(CallbackInfo ci) {
        PlayerMotionUpdateEvent event = new PlayerMotionUpdateEvent(Event.Era.PRE, rotationYaw, rotationPitch, this.posX, this.getEntityBoundingBox().minY, this.posZ, this.onGround);
        Kisman.EVENT_BUS.post(event);
        this.rotationYaw = event.getYaw();
        this.rotationPitch = event.getPitch();
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"), cancellable = true)
    public void onPostUpdateWalkingPlayerHook(CallbackInfo ci) {
        PlayerMotionUpdateEvent event = new PlayerMotionUpdateEvent(Event.Era.POST, rotationYaw, rotationPitch, this.posX, this.getEntityBoundingBox().minY, this.posZ, this.onGround);
        Kisman.EVENT_BUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void onUpdateHook(CallbackInfo ci) {
        PlayerUpdateEvent event = new PlayerUpdateEvent();
        Kisman.EVENT_BUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "pushOutOfBlocks(DDD)Z", at = @At("HEAD"), cancellable = true)
    public void pushOutOfBlocksHook(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        PlayerPushOutOfBlocksEvent event = new PlayerPushOutOfBlocksEvent(x, y, z);
        Kisman.EVENT_BUS.post(event);
        if (event.isCancelled()) cir.setReturnValue(false);
    }
}
