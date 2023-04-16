package com.kisman.cc.module.combat;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

public class Criticals extends Module {
    private final Setting strict = new Setting("Strict", this, false);
    private final Setting onlyKillaura = new Setting("OnlyKillAura", this, false);

    public Criticals() {
        super("Criticals", "", Category.COMBAT);

        register(strict);
        register(onlyKillaura);
    }

    public void onEnable() {
        Kisman.EVENT_BUS.subscribe(packetSendListener);
    }

    public void onDisable() {
        Kisman.EVENT_BUS.unsubscribe(packetSendListener);
    }

    @EventHandler
    private final Listener<PacketEvent.Send> packetSendListener = new Listener<>(event -> {
        if(event.getPacket() instanceof CPacketUseEntity) {
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if(packet.action.equals(CPacketUseEntity.Action.ATTACK) && mc.player.onGround && !mc.player.isInLava() && !mc.player.isInWater() && !mc.player.isInWeb) {
                if(onlyKillaura.getValBoolean() && !KillAura.instance.isToggled()) return;

                Entity entity = packet.getEntityFromWorld(mc.world);

                if(entity instanceof EntityLivingBase) {
                    double x = mc.player.posX, y = mc.player.posY, z = mc.player.posZ;

                    if(strict.getValBoolean()) {
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 0.07, z, false));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 0.08, z, false));
                        mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, false));
                    }

                    mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 0.05, z, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y + 0.012, z, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, false));
                    mc.player.onCriticalHit(entity);
                }
            }
        }
    });
}
