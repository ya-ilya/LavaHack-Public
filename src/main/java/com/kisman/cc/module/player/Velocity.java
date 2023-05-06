package com.kisman.cc.module.player;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.event.events.PlayerApplyCollisionEvent;
import com.kisman.cc.event.events.PlayerPushOutOfBlocksEvent;
import com.kisman.cc.event.events.PlayerPushedByWaterEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;

public class Velocity extends Module{
    private final Setting mode = new Setting("Mode", this, "None", new ArrayList<>(Arrays.asList("None", "Matrix", "Matrix 6.4", "Vanilla")));

    private final Setting exp = new Setting("Explosion", this, true);
    private final Setting bobbers = new Setting("Bobbers", this, true);
    private final Setting noPush = new Setting("NoPush", this, true);

    private final Setting horizontal = new Setting("Horizontal", this, 90, 0, 100, true);
    private final Setting vertical = new Setting("Vertical", this, 100, 0, 100, true);

    public Velocity() {
        super("Velocity", Category.PLAYER);

        register(mode);

        register(exp);
        register(bobbers);
        register(noPush);

        register(horizontal);
        register(vertical);
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;
        super.setDisplayInfo("[" + mode.getValString() + "]");

        switch (mode.getValString()) {
            case "Matrix":
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() == Block.getBlockById(0)) {
                    if (mc.player.hurtTime > 0) mc.player.motionY = -0.2;
                }
                break;
            case "Matrix 6.4":
                mc.player.onGround = true;
                break;
            case "Vanilla":
                mc.player.motionX *= (double) horizontal.getValInt() / 100;
                mc.player.motionY *= (double) vertical.getValInt() / 100;
                mc.player.motionZ *= (double) horizontal.getValInt() / 100;
                break;
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<PlayerApplyCollisionEvent> applyCollisionListener = listener(event -> {
        if (noPush.getValBoolean()) event.cancel();
    });

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<PlayerPushedByWaterEvent> pushedByWaterListener = listener(event -> {
        if (noPush.getValBoolean()) event.cancel();
    });

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<PlayerPushOutOfBlocksEvent> pushOutOfBlocksListener = listener(event -> {
        if (noPush.getValBoolean()) event.cancel();
    });

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Receive> packetReceiveListener = listener(event -> {
        if (event.getPacket() instanceof SPacketEntityVelocity) if (((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId()) event.cancel();
        if (event.getPacket() instanceof SPacketExplosion && exp.getValBoolean()) event.cancel();
        if (event.getPacket() instanceof SPacketEntityStatus && bobbers.getValBoolean()) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 31) {
                Entity entity = packet.getEntity(mc.world);
                if (entity instanceof EntityFishHook) if (((EntityFishHook) entity).caughtEntity == mc.player) event.cancel();
            }
        }
    });
}
