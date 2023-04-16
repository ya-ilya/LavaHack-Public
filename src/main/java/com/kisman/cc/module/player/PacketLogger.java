package com.kisman.cc.module.player;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.hud.hudmodule.render.PacketChat;
import com.kisman.cc.hud.hudmodule.render.packetchat.Message;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import i.gishreloaded.gishcode.utils.visual.ChatUtils;
import me.zero.alpine.listener.EventHandler;
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

    public boolean isBeta() {return true;}

    public void onEnable() {
        Kisman.EVENT_BUS.subscribe(listener);
        Kisman.EVENT_BUS.subscribe(listener1);
    }

    public void onDisable() {
        Kisman.EVENT_BUS.unsubscribe(listener);
        Kisman.EVENT_BUS.unsubscribe(listener1);
    }

    @EventHandler
    private final Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if(!client.getValBoolean()) return;

        StringBuilder message = new StringBuilder("Client -> " + event.getPacket().getClass().getName());

        if(values.getValBoolean()) for(Field field : event.getPacket().getClass().getDeclaredFields()) message.append(" ").append(field.getName()).append("[").append(field).append("]");
        
        ChatUtils.simpleMessage(message.toString());
        if(PacketChat.Instance.logs.ActiveMessages.size()+1>10)
        {
            if(PacketChat.Instance.logs.PassiveMessages.size()+1>10)
                PacketChat.Instance.logs.PassiveMessages.remove(0);

            PacketChat.Instance.up();
            PacketChat.Instance.logs.ActiveMessages.add(new Message(message.toString()));

        }else PacketChat.Instance.logs.ActiveMessages.add(new Message(message.toString()));
    });

    @EventHandler
    private final Listener<PacketEvent.Receive> listener1 = new Listener<>(event -> {
        if(!server.getValBoolean()) return;

        StringBuilder message = new StringBuilder("Server -> " + event.getPacket().getClass().getName());

        if(values.getValBoolean()) for(Field field : event.getPacket().getClass().getDeclaredFields()) message.append(" ").append(field.getName()).append("[").append(field).append("]");
        
        ChatUtils.simpleMessage(message.toString());
    });
}
