package com.kisman.cc.module.chat;

import com.kisman.cc.event.Event;
import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.mixin.mixins.accessor.AccessorSPacketChat;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;

public class AntiSpam extends Module {
    public static AntiSpam instance;

    public ArrayList<String> illegalWords = new ArrayList<>();

    public AntiSpam() {
        super("AntiSpam", Category.CHAT);
        instance = this;
    }

    public boolean isBeta() {
        return true;
    }

    @SuppressWarnings("unused")
    private final Listener<PacketEvent> packetListener = listener(event -> {
        if (event.getEra().equals(Event.Era.PRE) && event.getPacket() instanceof SPacketChat) {
            if (!((SPacketChat) event.getPacket()).isSystem()) return;
            String message = ((SPacketChat) event.getPacket()).getChatComponent().getFormattedText();
            for (String str : illegalWords) message = message.replaceAll(str, "");
            ((AccessorSPacketChat) event.getPacket()).setChatComponent(new TextComponentString(message));
        }
    });
}
