package com.kisman.cc.module.player;

import com.kisman.cc.Kisman;
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

        setmgr.rSetting(new Setting("Packets", this, "Packets"));

        setmgr.rSetting(new Setting("CPacketInput", this, false));
        setmgr.rSetting(new Setting("CPacketPlayer", this, false));
        setmgr.rSetting(new Setting("CPacketEntityAction", this, false));
        setmgr.rSetting(new Setting("CPacketUseEntity", this, false));
        setmgr.rSetting(new Setting("CPacketVehicleMove", this, false));
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
        Kisman.EVENT_BUS.subscribe(this.sendListener);
        this.input = setmgr.getSettingByName(this, "CPacketInput").getValBoolean();
        this.player = setmgr.getSettingByName(this, "CPacketPlayer").getValBoolean();
        this.entityAction = setmgr.getSettingByName(this, "CPacketEntityAction").getValBoolean();
        this.useEntity = setmgr.getSettingByName(this, "CPacketUseEntity").getValBoolean();
        this.vehicleMove = setmgr.getSettingByName(this, "CPacketVehicleMove").getValBoolean();
    }

    public void onDisable() {
        Kisman.EVENT_BUS.unsubscribe(this.sendListener);
    }

    @EventHandler
    private final Listener<PacketEvent.Send> sendListener = new Listener<>(event -> {
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
