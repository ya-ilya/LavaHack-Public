package com.kisman.cc.module.chat;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AutoEZ extends Module {
    public static List<String> AutoGgMessages = new ArrayList<>(Arrays.asList("{name} owned by {player_name} with " + Kisman.getName(), "gg, {name}!", Kisman.getName() + " owning {name}"));
    private ConcurrentHashMap<String, Integer> targetedPlayers = null;
    private int index = -1;

    private final Setting random = new Setting("Random message", this, true);

    public AutoEZ() {
        super("AutoEZ", Category.CHAT);

        register(random);
    }

    public void onEnable() {
        targetedPlayers = new ConcurrentHashMap<>();
        
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;
        if (this.targetedPlayers == null) this.targetedPlayers = new ConcurrentHashMap<>();

        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.getHealth() < 0) {
                    String name = player.getName();
                    if (shouldAnnounce(name)) {
                        doAnnounce(name);
                        break;
                    }
                }
            }
        }

        targetedPlayers.forEach((name, timeout) -> {
            if (timeout < 0) targetedPlayers.remove(name);
            else targetedPlayers.put(name, timeout - 1);
        });
    }

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
        if (mc.player != null) {
            if (targetedPlayers == null) targetedPlayers = new ConcurrentHashMap<>();

            if (event.getPacket() instanceof CPacketUseEntity) {
                CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
                if (packet.getAction().equals(CPacketUseEntity.Action.ATTACK)) {
                    Entity entity = packet.getEntityFromWorld(mc.world);
                    if (entity instanceof EntityPlayer) addTargetedPlayer(entity.getName());
                }
            }
        }
    });

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (mc.player != null) {
            if (targetedPlayers == null) targetedPlayers = new ConcurrentHashMap<>();

            EntityLivingBase entity = event.getEntityLiving();
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.getHealth() < 0) {
                    String name = player.getName();
                    if (shouldAnnounce(name)) doAnnounce(name);
                }
            }
        }
    }

    private boolean shouldAnnounce(String name) {
        return this.targetedPlayers.containsKey(name);
    }

    private void doAnnounce(String name) {
        targetedPlayers.remove(name);
        if (index >= (AutoGgMessages.size() - 1)) index = -1;
        index++;
        String message;
        if (AutoGgMessages.size() > 0 && random.getValBoolean()) message = AutoGgMessages.get(index);
        else message = "{name} ezzzz " + Kisman.getName() + " " + Kisman.getVersion() + " on top!";
        String messageSanitized = message.replaceAll("ยง", "").replace("{name}", name).replace("{player_name}", mc.player.getName());
        if (messageSanitized.length() > 255) messageSanitized = messageSanitized.substring(0, 255);
        mc.player.sendChatMessage(messageSanitized);
    }

    public void addTargetedPlayer(String name) {
        if (!Objects.equals(name, mc.player.getName())) {
            if (targetedPlayers == null) targetedPlayers = new ConcurrentHashMap<>();
            targetedPlayers.put(name, 20);
        }
    }

    public void onDisable() {
        
        targetedPlayers = null;
    }
}
