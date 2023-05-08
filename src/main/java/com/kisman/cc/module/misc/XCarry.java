package com.kisman.cc.module.misc;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.mixin.mixins.accessor.AccessorCPacketCloseWindow;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketCloseWindow;

public class XCarry extends Module {
    public XCarry() {
        super("XCarry", Category.MISC);
    }

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
        if (event.getPacket() instanceof CPacketCloseWindow) {
            CPacketCloseWindow packet = (CPacketCloseWindow) event.getPacket();
            if (((AccessorCPacketCloseWindow) packet).getWindowId() == mc.player.inventoryContainer.windowId) {
                event.cancel();
            }
        }
    });
}
