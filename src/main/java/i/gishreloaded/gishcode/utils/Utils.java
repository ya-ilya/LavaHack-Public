package i.gishreloaded.gishcode.utils;

import i.gishreloaded.gishcode.utils.system.Mapping;
import i.gishreloaded.gishcode.wrappers.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Utils {
	private static final Random RANDOM = new Random();

	public static String formatTime(long l) {
		long minutes = l / 1000 / 60;
		l -= minutes * 1000 * 60;
		long seconds = l / 1000;
		l -= seconds * 1000;
		StringBuilder sb = new StringBuilder();
		if (minutes != 0) sb.append(minutes).append("min ");
		if (seconds != 0) sb.append(seconds).append("s ");
		if (l != 0 || minutes == 0 && seconds == 0) sb.append(l).append("ms ");
		return sb.substring(0, sb.length() - 1);
	}

	public static int random(int min, int max) {
		return RANDOM.nextInt(max - min) + min;
	}

	public static boolean isMoving(Entity e) {
		return e.motionX != 0.0 && e.motionZ != 0.0 && (e.motionY != 0.0 || e.motionY > 0.0);
	}

	public static Vec3d getEyesPos() {
		return new Vec3d(Wrapper.INSTANCE.player().posX,
				Wrapper.INSTANCE.player().posY + Wrapper.INSTANCE.player().getEyeHeight(),
				Wrapper.INSTANCE.player().posZ);
	}

	public static void swingMainHand() {
		Wrapper.INSTANCE.player().swingArm(EnumHand.MAIN_HAND);
	}

	public static double round(final double value, final int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static float[] getNeededRotations(Vec3d vec) {
		final Vec3d eyesPos = getEyesPos();
		final double diffX = vec.x - eyesPos.x;
		final double diffY = vec.y - eyesPos.y;
		final double diffZ = vec.z - eyesPos.z;
		final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
		final float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
		final float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
		return new float[] {
				Wrapper.INSTANCE.player().rotationYaw
						+ MathHelper.wrapDegrees(yaw - Wrapper.INSTANCE.player().rotationYaw),
				Wrapper.INSTANCE.player().rotationPitch
						+ MathHelper.wrapDegrees(pitch - Wrapper.INSTANCE.player().rotationPitch) };
	}

	public static float getDirection() {
		float var1 = Wrapper.INSTANCE.player().rotationYaw;
		if (Wrapper.INSTANCE.player().moveForward < 0.0F) {
			var1 += 180.0F;
		}
		float forward = 1.0F;
		if (Wrapper.INSTANCE.player().moveForward < 0.0F) {
			forward = -0.5F;
		} else if (Wrapper.INSTANCE.player().moveForward > 0.0F) {
			forward = 0.5F;
		}
		if (Wrapper.INSTANCE.player().moveStrafing > 0.0F) {
			var1 -= 90.0F * forward;
		}
		if (Wrapper.INSTANCE.player().moveStrafing < 0.0F) {
			var1 += 90.0F * forward;
		}
		var1 *= 0.017453292F;
		return var1;
	}

	public static void faceVectorPacket(Vec3d vec) {
		float[] rotations = getNeededRotations(vec);
		EntityPlayerSP pl = Minecraft.getMinecraft().player;

		float preYaw = pl.rotationYaw;
		float prePitch = pl.rotationPitch;

		pl.rotationYaw = rotations[0];
		pl.rotationPitch = rotations[1];

		try {
			Method onUpdateWalkingPlayer = pl.getClass().getDeclaredMethod(Mapping.onUpdateWalkingPlayer);
			onUpdateWalkingPlayer.setAccessible(true);
			onUpdateWalkingPlayer.invoke(pl, new Object[0]);
		} catch (Exception ignored) {

		}

		pl.rotationYaw = preYaw;
		pl.rotationPitch = prePitch;
	}
}

