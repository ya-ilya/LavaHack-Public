package com.kisman.cc.module.movement;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;

public class Spider extends Module{
	private final Setting mode = new Setting("Mode", this, Mode.Default);

	public Spider() {
		super("Spider", Category.MOVEMENT);

		register(mode);
	}
	
	public void update() {
		if (mc.world != null && mc.player != null) {
			if (mode.getValString().equalsIgnoreCase(Mode.Default.name())) {
				if (!mc.player.isOnLadder() && mc.player.collidedHorizontally && mc.player.motionY < 0.2) mc.player.motionY = 0.2;
			} else {
				if (mc.player.collidedHorizontally && mc.player.ticksExisted % 8 == 0) {
					mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
					mc.player.motionY = 0.42;
					mc.player.fallDistance = 0f;
					mc.player.connection.sendPacket(new CPacketPlayer(true));
					mc.player.motionX = 0.0;
					mc.player.motionZ = 0.0;
				}
			}
		}
	}
	
	public enum Mode {Default, Matrix}
}
