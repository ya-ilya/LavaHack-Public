package com.kisman.cc.module.player;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.util.MovementUtil;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayer;

public class FastLadder extends Module {
    public FastLadder() {
        super("FastLadder", Category.PLAYER);
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;
        if (mc.player.isOnLadder()) mc.player.jump();
    }

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
        if (event.getPacket() instanceof CPacketPlayer && mc.player.isOnLadder() && MovementUtil.isMoving()) ((CPacketPlayer) event.getPacket()).onGround = true;
    });
}
