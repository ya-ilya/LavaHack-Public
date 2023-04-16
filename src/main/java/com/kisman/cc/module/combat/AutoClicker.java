package com.kisman.cc.module.combat;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Mouse;

public class AutoClicker extends Module {
	private long lastClick;
	private long hold;
	
	private double speed;
	private double holdLength;

	public AutoClicker() {
		super("AutoClicker", "clicks automatically", Category.COMBAT);
		
		register(new Setting("MinCPS", this, 8, 1, 20, false));
		register(new Setting("MaxCPS", this, 12, 1, 20, false));
	}

	public void update() {
		if (Mouse.isButtonDown(0)) {
			if (System.currentTimeMillis() - lastClick > speed * 1000) {
				lastClick = System.currentTimeMillis();
				if (hold < lastClick) {
					hold = lastClick;
				}
				int key = mc.gameSettings.keyBindAttack.getKeyCode();
				KeyBinding.setKeyBindState(key, true);
				KeyBinding.onTick(key);
				this.updateVals();
			} else if (System.currentTimeMillis() - hold > holdLength * 1000) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
				this.updateVals();
			}
		}
	}
	
	@Override
	public void onEnable() {
		this.updateVals();
	}
	
	private void updateVals() {
		double min = settingManager.getSettingByName(this, "MinCPS").getValDouble();
		double max = settingManager.getSettingByName(this, "MaxCPS").getValDouble();
		
		if (min >= max) {
			max = min + 1;
		}
		
		speed = 1.0 / ThreadLocalRandom.current().nextDouble(min - 0.2, max);
		holdLength = speed / ThreadLocalRandom.current().nextDouble(min, max);
	}
}
