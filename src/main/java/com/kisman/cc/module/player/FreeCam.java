package com.kisman.cc.module.player;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.Event;
import com.kisman.cc.event.events.PlayerMotionUpdateEvent;
import com.kisman.cc.event.events.PlayerPushOutOfBlocksEvent;
import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.module.player.freecam.MovementHelper;
import com.kisman.cc.settings.Setting;
import me.zero.alpine.event.type.Cancellable;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FreeCam
        extends Module {
    public final Setting speed = new Setting("Flying Speed",this, 1.0f, 0.1f, 5.0f, false);
    public final Setting disableOnDamage = new Setting("Disable on damage",this,false);
    public final Setting clipOnDisable = new Setting("Clip on disable", this,false);
    public final Setting autoTeleportDisable = new Setting("Auto teleport disable",this,false);
    public final Setting reallyWorld = new Setting("Really World",this,false);
    private double oldX;
    private double oldY;
    private double oldZ;

    public FreeCam() {
        super("FreeCam", "Allows you to fly in a FreeCam", Category.PLAYER);
        setmgr.rSetting(speed);
        setmgr.rSetting(reallyWorld);
        setmgr.rSetting(autoTeleportDisable);
        setmgr.rSetting(clipOnDisable);
        setmgr.rSetting(disableOnDamage);
    }

    @Override
    public void onEnable() {
        this.oldX = mc.player.posX;
        this.oldY = mc.player.posY;
        this.oldZ = mc.player.posZ;
        mc.player.noClip = true;
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        fakePlayer.copyLocationAndAnglesFrom(mc.player);
        fakePlayer.posY -= 0.0;
        fakePlayer.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-69, fakePlayer);
        Kisman.EVENT_BUS.subscribe(packetListener);
        Kisman.EVENT_BUS.subscribe(pushOutOfBlocksListener);
        Kisman.EVENT_BUS.subscribe(motionUpdateListener);
        super.onEnable();
        if (mc.player == null || mc.world == null || mc.player.ticksExisted < 1) {
            if (this.autoTeleportDisable.getValBoolean()) {
                this.toggle();
            }
        }
    }

    @Override
    public void onDisable() {
        Kisman.EVENT_BUS.unsubscribe(packetListener);
        Kisman.EVENT_BUS.unsubscribe(pushOutOfBlocksListener);
        Kisman.EVENT_BUS.unsubscribe(motionUpdateListener);
            
        if (this.clipOnDisable.getValBoolean()) {
            this.oldX = mc.player.posX;
            this.oldY = mc.player.posY;
            this.oldZ = mc.player.posZ;
        }
        mc.player.capabilities.isFlying = false;
        mc.world.removeEntityFromWorld(-69);
        mc.player.motionZ = 0.0;
        mc.player.motionX = 0.0;
        mc.player.noClip = true;
        mc.player.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, mc.player.rotationYaw, mc.player.rotationPitch);
        super.onDisable();
        if (mc.player == null || mc.world == null || mc.player.ticksExisted < 1) {
            if (this.autoTeleportDisable.getValBoolean()) {
                this.toggle();
            }
        }
    }

    @EventHandler
    private final Listener<PacketEvent> packetListener = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketPlayerPosLook && reallyWorld.getValBoolean()) {
            event.cancel();
        }

        if (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketEntityAction) {
            event.cancel();
        }
    });

    @EventHandler
    private final Listener<PlayerPushOutOfBlocksEvent> pushOutOfBlocksListener = new Listener<>(Cancellable::cancel);

    @EventHandler
    private final Listener<PlayerMotionUpdateEvent> motionUpdateListener = new Listener<>(event -> {
        if (event.getEra() == Event.Era.PRE) {
            event.cancel();
        }
    });

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (this.disableOnDamage.getValBoolean()) {
            if (mc.player.hurtTime <= 8) {
                mc.player.noClip = true;
                mc.player.capabilities.isFlying = true;
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.motionY = this.speed.getValFloat() / 1.5f;
                }
                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.motionY = -this.speed.getValFloat() / 1.5f;
                }
                MovementHelper.setSpeed(this.speed.getValFloat());
            } else if (!MovementHelper.isUnderBedrock()) {
                mc.player.capabilities.isFlying = false;
                mc.renderGlobal.loadRenderers();
                mc.player.noClip = false;
                mc.player.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, mc.player.rotationYaw, mc.player.rotationPitch);
                mc.world.removeEntityFromWorld(-69);
                mc.player.motionZ = 0.0;
                mc.player.motionX = 0.0;
                this.toggle();
            }
        } else {
            mc.player.noClip = true;
            mc.player.onGround = false;
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY = this.speed.getValFloat() / 1.5f;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = -this.speed.getValFloat() / 1.5f;
            }
            MovementHelper.setSpeed(this.speed.getValFloat());
            mc.player.capabilities.isFlying = true;
        }
        if (this.clipOnDisable.getValBoolean()) {
            this.oldX = mc.player.posX;
            this.oldY = mc.player.posY;
            this.oldZ = mc.player.posZ;
        }
    }
}

