package com.kisman.cc.module.player;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.*;
import net.minecraftforge.common.MinecraftForge;

public class PacketCancel extends Module {
    private boolean input;
    private boolean player;
    private boolean entityAction;
    private boolean useEntity;
    private boolean vehicleMove;

    public PacketCancel() {
        super("PacketCancel", "PacketCancel", Category.PLAYER);

        register(new Setting("Packets", this, "Packets"));

        register(new Setting("CPacketInput", this, false));
        register(new Setting("CPacketPlayer", this, false));
        register(new Setting("CPacketEntityAction", this, false));
        register(new Setting("CPacketUseEntity", this, false));
        register(new Setting("CPacketVehicleMove", this, false));
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);

        this.input = settingManager.getSettingByName(this, "CPacketInput").getValBoolean();
        this.player = settingManager.getSettingByName(this, "CPacketPlayer").getValBoolean();
        this.entityAction = settingManager.getSettingByName(this, "CPacketEntityAction").getValBoolean();
        this.useEntity = settingManager.getSettingByName(this, "CPacketUseEntity").getValBoolean();
        this.vehicleMove = settingManager.getSettingByName(this, "CPacketVehicleMove").getValBoolean();
    }

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
       if(
               (event.getPacket() instanceof CPacketInput && this.input) ||
                       (event.getPacket() instanceof CPacketPlayer && this.player) ||
                       (event.getPacket() instanceof CPacketEntityAction && this.entityAction) ||
                       (event.getPacket() instanceof CPacketUseEntity && this.useEntity) ||
                       (event.getPacket() instanceof CPacketVehicleMove && this.vehicleMove)
       ) {
            event.cancel();
           System.out.println("test");
       }
    });
}
