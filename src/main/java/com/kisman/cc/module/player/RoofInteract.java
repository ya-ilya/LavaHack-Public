package com.kisman.cc.module.player;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;

public class RoofInteract extends Module {
    public RoofInteract() {
        super("RoofInteract", Category.PLAYER);
    }

    public void onEnable() {
        Kisman.EVENT_BUS.subscribe(packetSendListener);
    }

    public void onDisable() {
        Kisman.EVENT_BUS.unsubscribe(packetSendListener);
    }

    @EventHandler
    private final Listener<PacketEvent.Send> packetSendListener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
            if (packet.getPos().getY() >= 255 && packet.getDirection() == EnumFacing.UP) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(packet.getPos(), EnumFacing.DOWN, packet.getHand(), packet.getFacingX(), packet.getFacingY(), packet.getFacingZ()));
                event.cancel();
            }
        }
    });
}
