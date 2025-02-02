package com.kisman.cc.module.movement;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.mixin.mixins.accessor.AccessorSPacketPlayerPosLook;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.ChatUtil;
import com.kisman.cc.util.TimerUtil;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class NoRotate extends Module {
    private final Setting waitDelay = new Setting("Delay", this, 2500, 0, 10000, true);

    private final TimerUtil timer = new TimerUtil();
    private boolean cancelPackets = true;
    private boolean timerReset = false;

    public NoRotate() {
        super("NoRotate", Category.MOVEMENT);

        register(waitDelay);
    }

    public void onEnable() {
        ChatUtil.message(TextFormatting.GOLD + "[NoRotate] " + TextFormatting.GRAY + "This module might desync you!");
    }

    public void update() {
        if (timerReset && !cancelPackets && timer.passedMillis(waitDelay.getValInt())) {
            ChatUtil.message(TextFormatting.GOLD + "[NoRotate] " + TextFormatting.GRAY + "This module might desync you!");
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

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
        if (cancelPackets && event.getPacket() instanceof SPacketPlayerPosLook) {
            AccessorSPacketPlayerPosLook accessorSPacketPlayerPosLook = (AccessorSPacketPlayerPosLook) event.getPacket();
            accessorSPacketPlayerPosLook.setYaw(mc.player.rotationYaw);
            accessorSPacketPlayerPosLook.setPitch(mc.player.rotationPitch);
        }
    });
}
