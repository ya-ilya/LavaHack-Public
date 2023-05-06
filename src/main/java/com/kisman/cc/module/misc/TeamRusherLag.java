package com.kisman.cc.module.misc;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class TeamRusherLag extends Module {
    private final Setting time = new Setting("Lag Time (in ms)", this, 3000, 1000, 10000, true);
    private final Setting text = new Setting("Message", this, "> #TeamRusher Lag: ON", "> #TeamRusher Lag: ON", true);

    private boolean canSend = false;
    private long lastPacket = 0L;

    public TeamRusherLag() {
        super("TeamRusherLag", Category.MISC);

        register(time);
        register(text);
    }

    public boolean isBeta() {return true;}

    public void onEnable() {
        lastPacket = 0L;
    }

    public void onDisable() {
        lastPacket = 0L;
    }

    public void update() {
        if(lastPacket != 0L && System.currentTimeMillis() > lastPacket + time.getValDouble()) {
            if(canSend) {
                canSend = false;
                mc.player.sendChatMessage(text.getValString());
            }
        }
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        lastPacket = 0L;
    }

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Receive> packetReceiveListener = listener(event -> lastPacket = System.currentTimeMillis());
}
