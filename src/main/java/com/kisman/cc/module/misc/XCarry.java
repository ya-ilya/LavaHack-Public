package com.kisman.cc.module.misc;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketCloseWindow;

public class XCarry extends Module {
    public XCarry() {
        super("XCarry", "XCarry", Category.MISC);
    }

    public void onEnable() {
        Kisman.EVENT_BUS.subscribe(packetSendListener);
    }

    public void onDisable() {
        Kisman.EVENT_BUS.unsubscribe(packetSendListener);
    }

    @EventHandler
    private final Listener<PacketEvent.Send> packetSendListener = new Listener<>(event -> {
        if(event.getPacket() instanceof CPacketCloseWindow) {
            CPacketCloseWindow packet = (CPacketCloseWindow) event.getPacket();
            if(packet.windowId == mc.player.inventoryContainer.windowId) event.cancel();
        }
    });
}
