package com.kisman.cc.module.movement;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class HoleSnap extends Module {
    private final Setting speedValue = new Setting("Speed", this, 0, 0, 2, false);
    private final Setting range = new Setting("Range", this, 4, 0, 10, true);
    private final Setting disableIfNoHole = new Setting("Disable If No Hole", this, true);

    private BlockPos hole;

    public HoleSnap() {
        super("HoleSnap", Category.MOVEMENT);

        register(speedValue);
        register(range);
        register(disableIfNoHole);
    }

    public void onEnable() {
        hole = null;
        hole = findHoles();
        if (hole == null && disableIfNoHole.getValBoolean()) super.setToggled(false);
    }

    public void onDisable() {
        hole = null;
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;
        super.setDisplayInfo("[" + speedValue.getValInt() + "]");
        hole = findHoles();
        if (hole == null) return;

        if (mc.gameSettings.keyBindSneak.isKeyDown() || HoleUtil.isInHole(mc.player, true, false)) {
            PlayerUtil.centerPlayer(mc.player.getPositionVector());
            if (disableIfNoHole.getValBoolean()) {
                super.setToggled(false);
                return;
            }
        }

        double yawRad = RotationUtils.getRotationTo(mc.player.getPositionVector().add(new Vec3d(-0.5, 0, -0.5)), new Vec3d(hole)).x * Math.PI / 180;
        double dist = mc.player.getPositionVector().distanceTo(new Vec3d(hole.getX(), hole.getY(), hole.getZ()));

        double speed;
        if (mc.player.onGround) speed = Math.min(MovementUtil.getBaseMoveSpeed(), Math.abs(dist) / 2); // divide by 2 because motion
        else speed = Math.min((Math.abs(mc.player.motionX) + Math.abs(mc.player.motionZ)), Math.abs(dist) / 2);

        speed *= speedValue.getValDouble();
        mc.player.motionX = -Math.sin(yawRad) * speed;
        mc.player.motionZ = Math.cos(yawRad) * speed;
    }

    private BlockPos findHoles() {
        NonNullList<BlockPos> holes = NonNullList.create();

        //from old HoleFill module, really good way to do this
        List<BlockPos> blockPosList = CrystalUtils.getSphere(mc.player, range.getValFloat(), false, true);

        blockPosList.forEach(pos -> {
            HoleUtil.HoleInfo holeInfo = HoleUtil.isHole(pos, true, false);
            HoleUtil.HoleType holeType = holeInfo.getType();
            if (holeType != HoleUtil.HoleType.NONE) {
                AxisAlignedBB centreBlocks = holeInfo.getCentre();

                if (centreBlocks == null) return;
                if (holeType == HoleUtil.HoleType.SINGLE && mc.world.isAirBlock(pos) && mc.world.isAirBlock(pos.add(0, 1, 0)) && mc.world.isAirBlock(pos.add(0, 2, 0)) && pos.getY() <= mc.player.posY) {
                    holes.add(pos);
                }
            }
        });

        BlockPos distPos = new BlockPos(Double.POSITIVE_INFINITY, 69, 429);
        double lastDist = (int) Double.POSITIVE_INFINITY;

        for (BlockPos blockPos : holes) {
            if (mc.player.getDistanceSq(blockPos) < lastDist) {
                distPos = blockPos;
                lastDist = mc.player.getDistanceSq(blockPos);
            }
        }

        if (!distPos.equals(new BlockPos(Double.POSITIVE_INFINITY, 69, 429))) return distPos;
        else return null;
    }
}
