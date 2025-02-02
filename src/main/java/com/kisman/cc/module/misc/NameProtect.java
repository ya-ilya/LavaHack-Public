package com.kisman.cc.module.misc;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.ChatUtil;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;

public class NameProtect extends Module {
    private final Setting name = new Setting("Name", this, "Kisman", "Kisman", true);

    public NameProtect() {
        super("NameProtect", Category.MISC);

        register(name);
    }

    public boolean isBeta() {
        return true;
    }

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Receive> packetReceiveListener = listener(event -> {
        SPacketChat packet;
        if (event.getPacket() instanceof SPacketChat && (packet = (SPacketChat)event.getPacket()).getType() != ChatType.GAME_INFO && getChatNames(packet.getChatComponent().getFormattedText())) event.cancel();
    });

    private boolean getChatNames(String message) {
        if (mc.player == null) return false;
        String out = message;
        out = out.replace(mc.player.getName(), name.getValString());
        ChatUtil.simpleMessage(out);
        return true;
    }
}
