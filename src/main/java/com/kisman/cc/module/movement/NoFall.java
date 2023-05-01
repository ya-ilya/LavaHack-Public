package com.kisman.cc.module.movement;

import com.kisman.cc.event.Event;
import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.event.events.PlayerMotionUpdateEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.EntityUtil;
import com.kisman.cc.util.InventoryUtil;
import com.kisman.cc.util.TimerUtil;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class NoFall extends Module {
    private final Setting mode = new Setting("Mode", this, Mode.Packet);

    private final TimerUtil timer = new TimerUtil();

    public NoFall() {
        super("NoFall", Category.MOVEMENT);

        register(mode);
    }

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
        if(event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            switch(mode.getValString()) {
                case "Packet":
                    if (mc.player.fallDistance > 3.0F) {
                        packet.onGround = true;
                        return;
                    }
                    break;
                case "Anti":
                    if (mc.player.fallDistance > 3.0F) {
                        packet.y = mc.player.posY + 0.10000000149011612;
                        return;
                    }
                    break;
                case "AAC":
                    if (mc.player.fallDistance > 3.0F) {
                        mc.player.onGround = true;
                        mc.player.capabilities.isFlying = true;
                        mc.player.capabilities.allowFlying = true;
                        packet.onGround = true;
                        mc.player.velocityChanged = true;
                        mc.player.capabilities.isFlying = false;
                        mc.player.jump();
                    }
                    break;
            }
        }
    });

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<PlayerMotionUpdateEvent> motionUpdateListener = listener(event -> {
        if(mode.getValString().equalsIgnoreCase(Mode.Bucket.name())) {
            int bucketSlot = InventoryUtil.findItem(Items.WATER_BUCKET, 0, 9);
            int oldSlot = mc.player.inventory.currentItem;

            if(bucketSlot != -1) {
                Vec3d positionVector = mc.player.getPositionVector();
                RayTraceResult rayTraceBlocks = mc.world.rayTraceBlocks(positionVector, new Vec3d(positionVector.x, positionVector.y - 3.0, positionVector.z), true);

                if (mc.player.fallDistance < 5.0f || rayTraceBlocks == null || rayTraceBlocks.typeOfHit != RayTraceResult.Type.BLOCK || mc.world.getBlockState(rayTraceBlocks.getBlockPos()).getBlock() instanceof BlockLiquid || EntityUtil.isInLiquid() || EntityUtil.isInLiquid(true)) return;
                if (event.getEra() == Event.Era.PRE) event.setPitch(90.0f);
                else {
                    RayTraceResult rayTraceBlocks2 = mc.world.rayTraceBlocks(positionVector, new Vec3d(positionVector.x, positionVector.y - 5.0, positionVector.z), true);
                    if (rayTraceBlocks2 != null && rayTraceBlocks2.typeOfHit == RayTraceResult.Type.BLOCK && !(mc.world.getBlockState(rayTraceBlocks2.getBlockPos()).getBlock() instanceof BlockLiquid) && timer.passedMillis(1000)) {
                        InventoryUtil.switchToSlot(bucketSlot, true);
                        mc.playerController.processRightClick(mc.player, mc.world, bucketSlot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                        InventoryUtil.switchToSlot(oldSlot, true);
                        timer.reset();
                    }
                }
            }
        }
    });

    public enum Mode {Packet, AAC, Anti, Bucket}
}
