package com.kisman.cc.module.combat;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.manager.managers.FriendManager;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

public class NoFriendDamage extends Module {
    public NoFriendDamage() {
        super("NoFriendDamage", Category.COMBAT);
    }

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
        if (event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            Entity target = packet.getEntityFromWorld(mc.world);
            if (target instanceof EntityPlayer && FriendManager.instance.isFriend(target.getName())) event.cancel();
        }
    });
}
