package com.kisman.cc.module.player;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.gui.hud.hudmodule.render.PacketChat;
import com.kisman.cc.gui.hud.hudmodule.render.packetchat.Message;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.ChatUtil;
import me.zero.alpine.listener.Listener;

import java.lang.reflect.Field;

public class PacketLogger extends Module{
    private final Setting client = new Setting("Client", this, true);
    private final Setting server = new Setting("Server", this, true);
    private final Setting values = new Setting("Values", this, false);

    public PacketLogger() {
        super("PacketLogger", Category.PLAYER);

        register(client);
        register(server);
        register(values);
    }

    public boolean isBeta() {
        return true;
    }

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
        if (!client.getValBoolean()) return;

        StringBuilder message = new StringBuilder("Client -> " + event.getPacket().getClass().getName());

        if (values.getValBoolean()) for (Field field : event.getPacket().getClass().getDeclaredFields()) message.append(" ").append(field.getName()).append("[").append(field).append("]");
        
        ChatUtil.simpleMessage(message.toString());
        if (PacketChat.Instance.logs.activeMessages.size()+1>10)
        {
            if (PacketChat.Instance.logs.passiveMessages.size()+1>10)
                PacketChat.Instance.logs.passiveMessages.remove(0);

            PacketChat.Instance.up();
            PacketChat.Instance.logs.activeMessages.add(new Message(message.toString()));

        }else PacketChat.Instance.logs.activeMessages.add(new Message(message.toString()));
    });

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Receive> packetReceiveListener = listener(event -> {
        if (!server.getValBoolean()) return;

        StringBuilder message = new StringBuilder("Server -> " + event.getPacket().getClass().getName());

        if (values.getValBoolean()) for (Field field : event.getPacket().getClass().getDeclaredFields()) message.append(" ").append(field.getName()).append("[").append(field).append("]");
        
        ChatUtil.simpleMessage(message.toString());
    });
}
