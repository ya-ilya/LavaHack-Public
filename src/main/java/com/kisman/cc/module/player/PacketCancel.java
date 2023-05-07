package com.kisman.cc.module.player;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.*;

public class PacketCancel extends Module {
    private final Setting input = new Setting("CPacketInput", this, false);
    private final Setting player = new Setting("CPacketPlayer", this, false);
    private final Setting entityAction = new Setting("CPacketEntityAction", this, false);
    private final Setting useEntity = new Setting("CPacketUseEntity", this, false);
    private final Setting vehicleMove = new Setting("CPacketVehicleMove", this, false);

    public PacketCancel() {
        super("PacketCancel", Category.PLAYER);

        register(new Setting("Packets", this, "Packets"));
        register(input);
        register(player);
        register(entityAction);
        register(useEntity);
        register(vehicleMove);
    }

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
       if (
               (event.getPacket() instanceof CPacketInput && input.getValBoolean()) ||
                       (event.getPacket() instanceof CPacketPlayer && player.getValBoolean()) ||
                       (event.getPacket() instanceof CPacketEntityAction && entityAction.getValBoolean()) ||
                       (event.getPacket() instanceof CPacketUseEntity && useEntity.getValBoolean()) ||
                       (event.getPacket() instanceof CPacketVehicleMove && vehicleMove.getValBoolean())
       ) {
            event.cancel();
           System.out.println("test");
       }
    });
}
