package com.kisman.cc.module.combat;

import com.kisman.cc.event.Event;
import com.kisman.cc.event.events.PlayerMotionUpdateEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.*;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class AntiTrap extends Module {
    public static AntiTrap instance;

    private final Setting mode = new Setting("Mode", this, "MotionTick", new ArrayList<>(Arrays.asList("MotionTick", "ClientTick")));
    private final Setting delay = new Setting("Delay", this, 400, 0, 1000, true);
    private final Setting switchMode = new Setting("SwitchMode", this, SwitchModes.None);
    private final Setting rotate = new Setting("Rotate", this, Rotate.NONE);
    private final Setting sortY = new Setting("SortY", this, true);
    private final Setting onlyInHole = new Setting("OnlyInHole", this, true);

    private final TimerUtil timer = new TimerUtil();

    public Set<BlockPos> placedPos = new HashSet<>();
    private final Vec3d[] surroundTargets = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, 1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(-1.0, 1.0, 1.0) };

    private boolean offhand = false;

    public AntiTrap() {
        super("AntiTrap", Category.COMBAT);

        instance = this;

        register(mode);
        register(delay);
        register(switchMode);
        register(rotate);
        register(sortY);
        register(onlyInHole);
    }

    public void onEnable() {
        if (mc.player == null || mc.world == null) super.setToggled(false);
    }

    public void onDisable() {
        if ((mc.player == null || mc.world == null)) return;

        switchItem();
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;
        super.setDisplayInfo("[" + mode.getValString() + "]");
        if (mode.getValString().equalsIgnoreCase("ClientTick")) doAntiTrap();
    }

    @SuppressWarnings("unused")
    private final Listener<PlayerMotionUpdateEvent> motionUpdateListener = listener(event -> {
        if (event.getEra() == Event.Era.PRE && mode.getValString().equalsIgnoreCase("MotionTick")) {
            doAntiTrap();
        }
    });

    private void doAntiTrap() {
        if (timer.passedMillis(delay.getValInt())) timer.reset();
        else return;

        if (onlyInHole.getValBoolean() && !BlockUtil.isBlockHole(mc.player.getPosition())) return;

        this.offhand = mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        if (!this.offhand && InventoryUtil.findItem(Items.END_CRYSTAL, 0, 9) == -1) return;
        ArrayList<Vec3d> targets = new ArrayList<>();
        Collections.addAll(targets, BlockUtil.convertVec3ds(mc.player.getPositionVector(), this.surroundTargets));
        EntityPlayer closestPlayer = EntityUtil.getTarget(5);
        if (closestPlayer != null) {
            targets.sort((vec3d, vec3d2) -> Double.compare(mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
            if (sortY.getValBoolean()) targets.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
        }
        for (Vec3d vec3d3 : targets) {
            BlockPos pos = new BlockPos(vec3d3);
            if (!CrystalUtils.canPlaceCrystal(pos)) continue;

            placeCrystal(pos);
            break;
        }
    }

    private void placeCrystal(final BlockPos pos) {
        boolean mainhand = (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL);
        if (!mainhand && !this.offhand && !this.switchItem()) return;

        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5));
        EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
        float[] angle = AngleUtil.calculateAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((pos.getX() + 0.5f), (pos.getY() - 0.5f), (pos.getZ() + 0.5f)));
        switch (this.rotate.getValString()) {
            case "NORMAL": {
                RotationUtils.setPlayerRotations(angle[0], angle[1]);
                break;
            }
            case "PACKET": {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(angle[0], (float) MathHelper.normalizeAngle((int)angle[1], 360), AntiTrap.mc.player.onGround));
                break;
            }
        }
        placedPos.add(pos);
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        mc.player.swingArm(EnumHand.MAIN_HAND);
        this.timer.reset();
    }

    private boolean switchItem() {
        if (offhand) return true;

        InventoryUtil.switchToSlot(InventoryUtil.findItem(Items.END_CRYSTAL, 0, 9), this.switchMode.getValString().equals("Silent"));
        return true;
    }

    public enum Rotate {
        NONE,
        NORMAL,
        PACKET
    }

    public enum SwitchModes {
        None,
        Normal,
        Silent
    }
}
