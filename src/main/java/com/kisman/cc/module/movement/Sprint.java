package com.kisman.cc.module.movement;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;

public class Sprint extends Module {
	public static Sprint instance;

	private final Setting mode = new Setting("Mode", this, Mode.Rage);

	public Sprint() {
		super("Sprint", Category.MOVEMENT);
		super.setDisplayInfo(() -> "[" + mode.getValString() + "]");

		instance = this;

		register(mode);
	}

	public void update() {
		if (mc.player != null && mc.world != null) mc.player.setSprinting(mode.checkValString("Rage") || (mode.checkValString("Legit") && mc.gameSettings.keyBindForward.isKeyDown()));
	}

	public void onDisable() {
		if (mc.player != null && mc.world != null) mc.player.setSprinting(false);
	}

	public enum Mode {Rage, Legit}
}
