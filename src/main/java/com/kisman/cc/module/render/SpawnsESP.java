package com.kisman.cc.module.render;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.mixin.mixins.accessor.AccessorRenderManager;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.Colour;
import com.kisman.cc.util.Rendering;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpawnsESP extends Module {
    private final Setting color = new Setting("Color", this, "Color", new Colour(255, 255, 255, 255));
    private final Setting crystals = new Setting("Crystals", this, true);
    private final Setting players = new Setting("Players", this, false);
    private final Setting mobs = new Setting("Mobs", this, false);
    private final Setting boats = new Setting("Boats", this, false);
    private final Setting duration = new Setting("Duration", this, 1, 0.1f, 5, false);
    private final Setting width = new Setting("Widtht", this, 2.5f, 0.1, 10, false);

    public CopyOnWriteArrayList<VecCircle> circles = new CopyOnWriteArrayList<>();
    public ConcurrentHashMap<BlockPos, Long> blocks = new ConcurrentHashMap<>();

    public SpawnsESP() {
        super("SpawnsESP", Category.RENDER);

        register(color);
        register(crystals);
        register(players);
        register(mobs);
        register(boats);
        register(duration);
        register(width);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        for (VecCircle circle : circles) {
            if ((float)(System.currentTimeMillis() - VecCircle.getTime(circle)) > 1000.0f * duration.getValDouble()) {
                this.circles.remove(circle);
                continue;
            }
            Rendering.setup();

            ArrayList<Vec3d> vertexes = new ArrayList<>();
            AccessorRenderManager accessorRenderManager = (AccessorRenderManager) mc.getRenderManager();
            double deltaX = VecCircle.getVector(circle).x - accessorRenderManager.getRenderPosX();
            double deltaY = VecCircle.getVector(circle).y - accessorRenderManager.getRenderPosY();
            double deltaZ = VecCircle.getVector(circle).z - accessorRenderManager.getRenderPosZ();
            GL11.glLineWidth((float) width.getValDouble());
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glBegin(1);
            for (int i = 0; i <= 360; ++i) {
                Vec3d vec3d = new Vec3d(deltaX + Math.sin((double)i * Math.PI / 180.0) * (double)VecCircle.getPitch(circle), deltaY + (VecCircle.getYaw(circle) * ((float)(System.currentTimeMillis() - VecCircle.getTime(circle)) / (1000.0f * duration.getValDouble()))), deltaZ + Math.cos((double)i * Math.PI / 180.0) * (double)VecCircle.getPitch(circle));
                vertexes.add(vec3d);
            }
            for (int n = 0; n < vertexes.size() - 1; ++n) {
                color.getColour().glColor();
                GL11.glVertex3d(vertexes.get(n).x, vertexes.get(n).y, vertexes.get(n).z);
                GL11.glVertex3d(vertexes.get(n + 1).x, vertexes.get(n + 1).y, vertexes.get(n + 1).z);
            }
            GL11.glEnd();

            Rendering.release();
        }
    }

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Receive> packetReceiveListener = listener(event -> {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            if (((SPacketSpawnObject) event.getPacket()).getType() == 51 && crystals.getValBoolean()) {
                this.circles.add(new VecCircle(new Vec3d(((SPacketSpawnObject) event.getPacket()).getX(), ((SPacketSpawnObject) event.getPacket()).getY(), ((SPacketSpawnObject) event.getPacket()).getZ()), 1.5f, 0.5f));
            } else if (((SPacketSpawnObject) event.getPacket()).getType() == 1 && boats.getValBoolean()) {
                this.circles.add(new VecCircle(new Vec3d(((SPacketSpawnObject) event.getPacket()).getX(), ((SPacketSpawnObject) event.getPacket()).getY(), ((SPacketSpawnObject) event.getPacket()).getZ()), 1.0f, 0.75f));
            }
        } else if (event.getPacket() instanceof SPacketSpawnPlayer && players.getValBoolean()) {
            this.circles.add(new VecCircle(new Vec3d(((SPacketSpawnPlayer) event.getPacket()).getX(), ((SPacketSpawnPlayer) event.getPacket()).getY(), ((SPacketSpawnPlayer) event.getPacket()).getZ()), 1.8f, 0.5f));
        } else if (event.getPacket() instanceof SPacketSpawnMob && mobs.getValBoolean()) {
            this.circles.add(new VecCircle(new Vec3d(((SPacketSpawnMob) event.getPacket()).getX(), ((SPacketSpawnMob) event.getPacket()).getY(), ((SPacketSpawnMob) event.getPacket()).getZ()), 1.8f, 0.5f));
        }
    });

    public static class VecCircle {
        public Vec3d vec3d;
        public float yaw;
        public float pitch;
        public long time;

        public static Vec3d getVector(VecCircle circle) {
            return circle.vec3d;
        }

        public VecCircle(Vec3d vec3d, float yas, float pitch) {
            this.vec3d = vec3d;
            this.yaw = yas;
            this.pitch = pitch;
            this.time = System.currentTimeMillis();
        }

        public static float getPitch(VecCircle circle) {
            return circle.pitch;
        }

        public static float getYaw(VecCircle circle) {
            return circle.yaw;
        }

        public static long getTime(VecCircle circle) {
            return circle.time;
        }
    }
}