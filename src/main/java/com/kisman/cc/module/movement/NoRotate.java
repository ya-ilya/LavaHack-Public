package com.kisman.cc.module.movement;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import i.gishreloaded.gishcode.utils.TimerUtils;
import i.gishreloaded.gishcode.utils.visual.ChatUtils;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class NoRotate extends Module {
    private final Setting waitDelay = new Setting("Delay", this, 2500, 0, 10000, true);

    private final TimerUtils timer = new TimerUtils();
    private boolean cancelPackets = true;
    private boolean timerReset = false;

    public NoRotate() {
        super("NoRotate", "NoRotate", Category.MOVEMENT);

        register(waitDelay);
    }

    public void onEnable() {
        Kisman.EVENT_BUS.subscribe(packetSendListener);

        ChatUtils.message(TextFormatting.GOLD + "[NoRotate] " + TextFormatting.GRAY + "This module might desync you!");
    }

    public void onDisable() {
        Kisman.EVENT_BUS.unsubscribe(packetSendListener);
    }

    public void update() {
        if(timerReset && !cancelPackets && timer.passedMillis(waitDelay.getValInt())) {
            ChatUtils.message(TextFormatting.GOLD + "[NoRotate] " + TextFormatting.GRAY + "This module might desync you!");
            cancelPackets = true;
            timerReset = false;
        }
    }

    @SubscribeEvent
    public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        timer.reset();
        timerReset = true;
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        cancelPackets = false;
    }

    @EventHandler
    private final Listener<PacketEvent.Send> packetSendListener = new Listener<>(event -> {
        if(cancelPackets && event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
        }
    });
}
