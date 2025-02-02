package com.kisman.cc.mixin.mixins;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.Event;
import com.kisman.cc.event.events.DamageBlockEvent;
import com.kisman.cc.event.events.DestroyBlockEvent;
import com.kisman.cc.module.player.Reach;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {
    @Inject(method = "getBlockReachDistance", at = @At("HEAD"), cancellable = true)
    public void getBlockReachDistanceHook(CallbackInfoReturnable<Float> callback) {
        if (Reach.instance.isToggled()) {
            callback.setReturnValue(Reach.instance.distance.getValFloat());
        }
    }

    @Inject(method = "clickBlock", at = @At("HEAD"), cancellable = true)
    private void clickBlockHook(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir) {
        DamageBlockEvent event = new DamageBlockEvent(posBlock, directionFacing);
        Kisman.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            cir.setReturnValue(false);
        }
    }

    @Dynamic
    @Inject(
        method = "onPlayerDestroyBlock",
        at = @At(
            value = "INVOKE",
            target = "net/minecraft/block/Block.removedByPlayer" +
                    "(Lnet/minecraft/block/state/IBlockState;" +
                     "Lnet/minecraft/world/World;" +
                     "Lnet/minecraft/util/math/BlockPos;" +
                     "Lnet/minecraft/entity/player/EntityPlayer;Z)Z",
            remap = false),
        cancellable = true)
    private void onPlayerDestroyBlockHook(BlockPos pos,
                                          CallbackInfoReturnable<Boolean> info)
    {
        DestroyBlockEvent event = new DestroyBlockEvent(Event.Era.PRE, pos);
        Kisman.EVENT_BUS.post(event);
        if (event.isCancelled())
        {
            info.setReturnValue(false);
        }
    }
}
