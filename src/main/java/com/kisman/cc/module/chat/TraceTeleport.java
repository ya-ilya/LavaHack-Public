package com.kisman.cc.module.chat;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.ChatUtil;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityTeleport;

public class TraceTeleport extends Module {
    private final Setting onlyPlayers = new Setting("Only Players", this, true);
    
    public TraceTeleport() {
        super("TraceTeleport", Category.CHAT);

        register(onlyPlayers);
    }

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Receive> packetReceiveListener = listener(event -> {
        if (event.getPacket() instanceof SPacketEntityTeleport) {
            SPacketEntityTeleport packet = (SPacketEntityTeleport) event.getPacket();

            if (onlyPlayers.getValBoolean() && !(mc.world.getEntityByID(packet.getEntityId()) instanceof EntityPlayer)) return;
            if (Math.abs(mc.player.posX - packet.getX()) > 500d || Math.abs(mc.player.posZ - packet.getZ()) > 500d) {
                String name = "Unknown";
                Entity entity = mc.world.getEntityByID(packet.getEntityId());
                if (entity != null) name = entity.getClass().getSimpleName();

                double distance = Math.sqrt(Math.pow(mc.player.posX - packet.getX(), 2d) + Math.pow(mc.player.posZ - packet.getZ(), 2d));

                String warn = String.format("Entity [%s] teleported to [%.2f, %.2f, %.2f], %.2f blocks away", name, packet.getX(), packet.getY(), packet.getZ(), distance);

                ChatUtil.warning(warn);

                Kisman.LOGGER.warn("[TraceTeleport]: " + warn);
            }
        }
    });
}
