package com.kisman.cc.module.movement;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.event.events.PlayerUpdateEvent;
import com.kisman.cc.gui.csgo.components.Slider;
import com.kisman.cc.manager.Managers;
import com.kisman.cc.mixin.mixins.accessor.AccessorEntityPlayer;
import com.kisman.cc.mixin.mixins.accessor.AccessorKeyBinding;
import com.kisman.cc.mixin.mixins.accessor.AccessorMinecraft;
import com.kisman.cc.mixin.mixins.accessor.AccessorTimer;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.EntityUtil;
import com.kisman.cc.util.MovementUtil;
import com.kisman.cc.util.PlayerUtil;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Speed extends Module {
    public static Speed instance;

    public final Setting speedMode = new Setting("SpeedMode", this, "Strafe", new ArrayList<>(Arrays.asList("Strafe", "Strafe New", "YPort", "Sti", "Matrix 6.4", "Matrix Bhop", "Sunrise Strafe", "Bhop", "Strafe2", "Matrix")));

    private final Setting useTimer = new Setting("Use Timer", this, false).setVisible(() -> speedMode.checkValString("Bhop") || speedMode.checkValString("Strafe New"));

    private final Setting motionXmodifier = new Setting("Motion X Modifier", this, 0, 0, 0.5, false).setVisible(() -> speedMode.checkValString("Strafe2"));
    private final Setting motionZmodifier = new Setting("Motion Z Modifier", this, 0, 0, 0.5, false).setVisible(() -> speedMode.checkValString("Strafe2"));

    private final Setting strafeNewLine = new Setting("StrafeNewLine", this, "Strafe New").setVisible(() -> speedMode.checkValString("Strafe New"));
    private final Setting strafeSpeed = new Setting("Strafe Speed", this, 0.2873f, 0.1f, 1, false).setVisible(() -> speedMode.checkValString("Strafe New"));
    private final Setting slow = new Setting("Slow", this, false).setVisible(() -> speedMode.checkValString("Strafe New"));
    private final Setting cap = new Setting("Cap", this, 10, 0, 10, false).setVisible(() -> speedMode.checkValString("Strafe New"));
    private final Setting scaleCap = new Setting("Scale Cap", this, false).setVisible(() -> speedMode.checkValString("Strafe New"));
    private final Setting lagTime = new Setting("Lag Time", this, 500, 0, 1000, Slider.NumberType.TIME).setVisible(() -> speedMode.checkValString("Strafe New"));

    private final Setting yPortLine = new Setting("YPortLine", this, "YPort").setVisible(() -> speedMode.checkValString("YPort"));
    private final Setting yPortSpeed = new Setting("YPortSpeed", this, 0.06f, 0.01f, 0.15f, false).setVisible(() -> speedMode.checkValString("YPort"));
    private final Setting yWater = new Setting("Water", this, false).setVisible(() -> speedMode.checkValString("YPort"));
    private final Setting yLava = new Setting("Lava", this, false).setVisible(() -> speedMode.checkValString("YPort"));

    private final Setting stiLine = new Setting("StiLine", this, "Sti").setVisible(() -> speedMode.checkValString("Sti"));
    private final Setting stiSpeed = new Setting("StiSpeed", this, 4, 0.1, 10, true).setVisible(() -> speedMode.checkValString("Sti"));

    private final Setting bhopLine = new Setting("BhopLine", this, "Bhop").setVisible(() -> speedMode.checkValString("Bhop"));
    private final Setting useMotion = new Setting("Use Motion", this, false).setVisible(() -> speedMode.checkValString("Bhop"));
    private final Setting useMotionInAir = new Setting("Use Motion In Air", this, false).setVisible(() -> speedMode.checkValString("Bhop"));
    private final Setting jumpMovementFactorSpeed = new Setting("Jump Movement Factor Speed", this, 0.265f, 0.01f, 10, false).setVisible(() -> speedMode.checkValString("Bhop"));
    private final Setting jumpMovementFactor = new Setting("Jump Movement Factor", this, false).setVisible(() -> speedMode.checkValString("Bhop"));
    private final Setting boostSpeed = new Setting("Boost Speed", this, 0.265f, 0.01f, 10, false).setVisible(() -> speedMode.checkValString("Bhop"));
    private final Setting boostFactor = new Setting("Boost Factor", this, false).setVisible(() -> speedMode.checkValString("Bhop"));

    private int stage;
    private double speed;
    private double dist;
    private boolean boost;

    private BlockPos lastPos;
    private Motion currentMotion;
    private double y = 1;

    public Speed() {
        super("Speed", Category.MOVEMENT);

        instance = this;

        register(speedMode);

        register(useTimer);

        register(motionXmodifier);
        register(motionZmodifier);

        register(strafeNewLine);
        register(strafeSpeed);
        register(slow);
        register(cap);
        register(scaleCap);
        register(lagTime);

        register(yPortLine);
        register(yPortSpeed);
        register(yWater);
        register(yLava);

        register(stiLine);
        register(stiSpeed);

        register(bhopLine);
        register(useMotion);
        register(useMotionInAir);
        register(jumpMovementFactor);
        register(jumpMovementFactorSpeed);
        register(boostSpeed);
        register(boostFactor);
    }

    public void onEnable() {
        if (mc.player == null || mc.world == null) return;
        stage = 4;
        dist = MovementUtil.getDistance2D();
        speed = MovementUtil.getSpeed();
    }

    public void onDisable() {
        EntityUtil.resetTimer();
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;

        super.setDisplayInfo("[" + speedMode.getValString() + TextFormatting.GRAY + "]");

        dist = MovementUtil.getDistance2D();

        switch (speedMode.getValString()) {
            case "Strafe":
                doStrafeSpeed();
                break;
            case "YPort":
                doYPortSpeed();
                break;
            case "Strafe New":
                doStrafeNewSpeed();
                break;
            case "Matrix Bhop":
                doMatrixBhopSpeed();
                break;
            case "Matrix 6.4":
                doMatrixSixFourSpeed();
                break;
            case "Sunrise Strafe":
                doSunriseStrafeSpeed();
                break;
            case "Bhop":
                doBhopSpeed();
                break;
            case "Strafe2":
                doStrafeTwoSpeed();
                break;
            case "Matrix":
                doMatrixSpeed();
                break;
        }
    }

    private void doStrafeSpeed() {
        if (!(mc.player.moveForward > 0) || mc.player.hurtTime >= 5) return;

        if (mc.player.onGround) {
            mc.player.motionY = 0.405;
            float direction = getDirection();

            mc.player.motionX -= (MathHelper.sin(direction) * 0.2F);
            mc.player.motionZ += (MathHelper.cos(direction) * 0.2F);
        } else {
            double currentSpeed = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
            double speed = Math.abs(mc.player.rotationYawHead - mc.player.rotationYaw) < 90 ? 1.0064 : 1.001;
            double direction =getDirection();

            mc.player.motionX = -Math.sin(direction) * speed * currentSpeed;
            mc.player.motionZ = Math.cos(direction) * speed * currentSpeed;
        }
    }

    private void doYPortSpeed() {
        if (!PlayerUtil.isMoving(mc.player) || (mc.player.isInWater() && !yWater.getValBoolean()) && (mc.player.isInLava() && !yLava.getValBoolean()) || mc.player.collidedHorizontally) return;
        if (mc.player.onGround) {
            EntityUtil.setTimer(1.15f);
            mc.player.jump();
            PlayerUtil.setSpeed(mc.player, PlayerUtil.getBaseMoveSpeed() + yPortSpeed.getValDouble());
        } else {
            mc.player.motionY = -1;
            EntityUtil.resetTimer();
        }
    }

    private void doStrafeNewSpeed() {
        if (mc.player.isElytraFlying()) return;
        if (useTimer.getValBoolean() && Managers.instance.passed(250)) EntityUtil.setTimer(1.0888f);
        if (!Managers.instance.passed(lagTime.getValInt())) return;

        switch (stage) {
            case 1:
                if (!PlayerUtil.isMoving(mc.player)) break;
                speed = 1.35 * MovementUtil.getSpeed(slow.getValBoolean(), strafeSpeed.getValDouble()) - 0.01;
                break;
            case 2:
                if (!PlayerUtil.isMoving(mc.player) || mc.player.onGround) break;
                mc.player.motionY = 0.3999 + MovementUtil.getJumpSpeed();
                speed *= boost ? 1.6835 : 1.395;
                break;
            case 3:
                speed = dist  - 0.66 * (dist - MovementUtil.getSpeed(slow.getValBoolean(), strafeSpeed.getValDouble()));
                boost = !boost;
                break;
            default:
                if (stage > 0 && (mc.world.getCollisionBoxes(null, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0)).size() > 0 || mc.player.collidedVertically)) {
                    stage = PlayerUtil.isMoving(mc.player) ? 1 : 0;
                }

                speed = dist - dist / 159;
                break;
        }

        speed = Math.min(speed, getCap());
        speed = Math.max(speed, MovementUtil.getSpeed(slow.getValBoolean(), strafeSpeed.getValDouble()));
        MovementUtil.strafe((float) speed);

        if (PlayerUtil.isMoving(mc.player)) stage++;
    }

    private void doMatrixBhopSpeed() {
        if (!PlayerUtil.isMoving(mc.player)) return;

        ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(false);

        if (mc.player.onGround) {
            mc.player.jump();
            ((AccessorEntityPlayer) mc.player).setSpeedInAir(0.0208f);
            mc.player.jumpMovementFactor = 0.1f;
            EntityUtil.setTimer(0.94f);
        }
        if (mc.player.fallDistance > 0.6 && mc.player.fallDistance < 1.3) {
            ((AccessorEntityPlayer) mc.player).setSpeedInAir(0.0208f);
            EntityUtil.setTimer(1.8f);
        }
    }

    private void doMatrixSixFourSpeed() {
        if (mc.player.ticksExisted % 4 == 0) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }

        if (!PlayerUtil.isMoving(mc.player)) return;

        if (mc.player.onGround) {
            ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(false);
            mc.player.jump();
        } else if (mc.player.fallDistance <= 0.1) {
            ((AccessorEntityPlayer) mc.player).setSpeedInAir(0.0202f);
            mc.player.jumpMovementFactor = 0.027f;
            EntityUtil.setTimer(1.5f);
        } else if (mc.player.fallDistance > 0.1 && mc.player.fallDistance < 1.3) {
            EntityUtil.setTimer(0.7f);
        } else if (mc.player.fallDistance >= 1.3) {
            EntityUtil.resetTimer();
            ((AccessorEntityPlayer) mc.player).setSpeedInAir(0.0202f);
            mc.player.jumpMovementFactor = 0.025f;
        }
    }

    private void doSunriseStrafeSpeed() {
        if (PlayerUtil.isMoving(mc.player)) {
            if (mc.player.onGround) {
                mc.player.jump();
                MovementUtil.strafe(MovementUtil.calcMoveYaw(mc.player.rotationYaw), MovementUtil.getSpeed() * 1.02);
            }
        } else {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
        }
    }

    private void doStrafeTwoSpeed() {
        if (!MovementUtil.isMoving()) return;

        if (mc.player.onGround) {
            mc.player.jump();
        } else {
            double yaw = MovementUtil.getDirection();
            mc.player.motionX = -Math.sin(yaw) * motionXmodifier.getValFloat();
            mc.player.motionZ = Math.cos(yaw) * motionZmodifier.getValFloat();
        }
    }

    private void doMatrixSpeed() {
        if (!MovementUtil.isMoving() || mc.player.ticksExisted % 2 != 0) return;

        if (mc.player.onGround) mc.player.jump();
        else MovementUtil.setMotion(MovementUtil.WALK_SPEED * 1.025);
    }

    public double getCap() {
        double ret = cap.getValDouble();

        if (!scaleCap.getValBoolean()) return ret;
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            ret *= 1 + 0.2 * (amplifier + 1);
        }

        if (slow.getValBoolean() && mc.player.isPotionActive(MobEffects.SLOWNESS)) {
            int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SLOWNESS)).getAmplifier();
            ret /= 1 + 0.2 * (amplifier + 1);
        }

        return ret;
    }

    public static double[] directionSpeed(double speed) {
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

        if (forward != 0.0f) {
            if (side > 0.0f) yaw += ((forward > 0.0f) ? -45 : 45);
            else if (side < 0.0f) yaw += ((forward > 0.0f) ? 45 : -45);
            side = 0.0f;
            if (forward > 0.0f) forward = 1.0f;
            else if (forward < 0.0f) forward = -1.0f;
        }

        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = forward * speed * cos + side * speed * sin;
        double posZ = forward * speed * sin - side * speed * cos;
        return new double[]{posX, posZ};
    }

    private double getBaseMotionSpeed() {
        double baseSpeed = 0.2873D;
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * ((double) amplifier + 1);
        }
        return baseSpeed;
    }

    private void doBhopSpeed() {
        currentMotion = getMotion();
        mc.player.setSprinting(true);

        if (!mc.gameSettings.keyBindForward.isKeyDown()) return;

        if (mc.player.onGround) {
            if (useMotion.getValBoolean()) {
                bhopSpeedDoMotion(false);
            }

            y = 1;
            EntityUtil.resetTimer();

            if (useTimer.getValBoolean()) {
                Managers.instance.timerManager.updateTimer(this, 2, 1.3f);
            }

            mc.player.jump();
            double[] dirSpeed = directionSpeed((getBaseMotionSpeed() * boostSpeed.getValDouble()) + (boostFactor.getValBoolean() ? 0.3 : 0));
            mc.player.motionX = dirSpeed[0];
            mc.player.motionZ = dirSpeed[1];
        } else {
            if (jumpMovementFactor.getValBoolean()) {
                mc.player.jumpMovementFactor = jumpMovementFactorSpeed.getValFloat();
            }

            if (y == 1) {
                y = mc.player.getPositionVector().y;
                return;
            }

            if (mc.player.getPositionVector().y < y) {
                y = mc.player.getPositionVector().y;
                mc.player.motionX = 0;
                mc.player.motionZ = 0;
                if (useTimer.getValBoolean()) EntityUtil.resetTimer();
                Managers.instance.timerManager.updateTimer(this, 2, 16);
            } else {
                y = mc.player.getPositionVector().y;

                if (useMotionInAir.getValBoolean()) {
                    bhopSpeedDoMotion(true);
                }
            }
        }
    }

    private void bhopSpeedDoMotion(boolean inAir) {
        if (currentMotion == null) return;

        double offset = inAir ? 0.2 : 0.1;

        switch (currentMotion) {
            case mX:
                mc.player.motionX = mc.player.motionX - offset;
                break;
            case X:
                mc.player.motionX = mc.player.motionX + offset;
                break;
            case mY:
                mc.player.motionY = mc.player.motionY - offset;
                break;
            case Y:
                mc.player.motionY = mc.player.motionY + offset;
                break;
        }
    }

    private Motion getMotion() {
        BlockPos posToCheck = new BlockPos(EntityUtil.getRoundedBlockPos(mc.player).getX(), EntityUtil.getRoundedBlockPos(mc.player).getY(), EntityUtil.getRoundedBlockPos(mc.player).getZ());
        if (getBlock(posToCheck) == Blocks.AIR && lastPos != null) {
            if (posToCheck != lastPos) {
                if (lastPos.add(0, 0, -1).equals(posToCheck)) return Motion.mY;
                if (lastPos.add(0, 0, 1).equals(posToCheck)) return Motion.Y;
                if (lastPos.add(1, 0, 0).equals(posToCheck)) return Motion.X;
                if (lastPos.add(-1, 0, 0).equals(posToCheck)) return Motion.mX;
            }
        } else lastPos = new BlockPos(EntityUtil.getRoundedBlockPos(mc.player).getX(), EntityUtil.getRoundedBlockPos(mc.player).getY(), EntityUtil.getRoundedBlockPos(mc.player).getZ());
        return null;
    }

    private Block getBlock(BlockPos pos) {
        if (pos != null) return mc.world.getBlockState(pos).getBlock();
        return null;
    }

    public enum Motion {X,Y,mX,mY}

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Receive> packetReceiveListener = listener(event -> {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            if (mc.player != null) dist = 0;
            speed = 0;
            stage = 4;
            EntityUtil.setTimer(1);
        }
    });

    @SuppressWarnings("unused")
    private final Listener<PlayerUpdateEvent> playerUpdateListener = listener(event -> {
        if (speedMode.getValString().equalsIgnoreCase("Sti")) {
            ((AccessorTimer) ((AccessorMinecraft) mc).getTimer()).setTickLength(50 / getSpeed());
        }}
    );

    private float getSpeed() {
        return Math.max((float) stiSpeed.getValDouble(), 0.1f);
    }

    private static float getDirection() {
        float var1 = mc.player.rotationYaw;
        if (mc.player.moveForward < 0.0F) {
            var1 += 180.0F;
        }
        float forward = 1.0F;
        if (mc.player.moveForward < 0.0F) {
            forward = -0.5F;
        } else if (mc.player.moveForward > 0.0F) {
            forward = 0.5F;
        }
        if (mc.player.moveStrafing > 0.0F) {
            var1 -= 90.0F * forward;
        }
        if (mc.player.moveStrafing < 0.0F) {
            var1 += 90.0F * forward;
        }
        var1 *= 0.017453292F;
        return var1;
    }
}
