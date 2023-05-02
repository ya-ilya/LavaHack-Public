package com.kisman.cc.module.player;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import io.netty.buffer.Unpooled;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public class ForgeBypass extends Module {
    public static ForgeBypass instance;

    public ForgeBypass() {
        super("ForgeBypass", Category.PLAYER);

        instance = this;
    }

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
        if (!mc.isIntegratedServerRunning()) {
            if (event.getPacket().getClass().getName().equals("net.minecraftforge.fml.common.network.internal.FMLProxyPacket")) event.cancel();
            else if (event.getPacket() instanceof CPacketCustomPayload) if (((CPacketCustomPayload) event.getPacket()).getChannelName().equalsIgnoreCase("MC|Brand")) ((CPacketCustomPayload) event.getPacket()).data = (new PacketBuffer(Unpooled.buffer()).writeString("vanilla"));
        }
    });
}
