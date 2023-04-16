package com.kisman.cc.module.client;

import com.kisman.cc.Kisman;
import com.kisman.cc.gui.csgo.components.Slider;
import com.kisman.cc.hud.hudmodule.render.ArrayListModule;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.Colour;

import java.util.ArrayList;
import java.util.Arrays;

public class HUD extends Module {
	public static HUD instance;

	public final Setting astolfoColor = new Setting("Astolfo Color", this, false);
	public final Setting offsets = new Setting("Offsets", this, 2, 0, 10, true);
	public final Setting glow = new Setting("Glow", this, false);
	public final Setting glowV2 = new Setting("Second glow", this, false);
	public final Setting glowOffset = new Setting("Glow Offset", this, 5, 1, 20, true);
	public final Setting glowRadius = new Setting("Glow Radius", this, 0, 0, 20, true);
	public final Setting glowAlpha = new Setting("Glow Alpha", this, 255, 0, 255, true);
	public final Setting background = new Setting("Background", this, false);
	public final Setting bgAlpha = new Setting("Bg Alpha", this, 255, 0, 255, true);

	private final Setting arrLine = new Setting("ArrLine", this, "ArrayList").setVisible(() -> ArrayListModule.instance.isToggled());
	public final Setting arrMode = new Setting("ArrayList Mode", this, "RIGHT", new ArrayList<>(Arrays.asList("LEFT", "RIGHT"))).setVisible(() -> ArrayListModule.instance.isToggled());
	public final Setting arrY = new Setting("ArrayList Y", this, 150, 0, mc.displayHeight, true).setVisible(() -> ArrayListModule.instance.isToggled());
	public final Setting arrColor = new Setting("ArrayList Color", this, " ArrayList Color", new Colour(255, 0, 0, 255)).setVisible(() -> ArrayListModule.instance.isToggled());
	public final Setting arrGradient = new Setting("Array Gradient", this, Gradient.None).setVisible(() -> ArrayListModule.instance.isToggled());
	public final Setting arrGradientDiff = new Setting("Array Gradient Diff", this, 200, 0, 1000, Slider.NumberType.TIME).setVisible(() -> ArrayListModule.instance.isToggled() && !arrGradient.getValString().equalsIgnoreCase(Gradient.None.name()));
	public final Setting arrGlowBackground = new Setting("Array Glow Background", this, false).setVisible(() -> ArrayListModule.instance.isToggled());

	private final Setting welLine = new Setting("WelLine", this, "Welcomer");
	public final Setting welColor = new Setting("WelColor", this, "WelcomerColor", new Colour(255, 0, 0, 255));

	private final Setting pvpLine = new Setting("PvpLine", this, "PvpInfo");
	public final Setting pvpY = new Setting("PvpInfo Y", this, 200, 0, mc.displayHeight, true);

	private final Setting armLine = new Setting("ArmLine", this, "Armor");
	public final Setting armExtra = new Setting("Extra Info", this, false);
	public final Setting armDmg = new Setting("Damage", this, false);

	private final Setting radarLine = new Setting("RadarLine", this, "Radar");
	public final Setting radarDist = new Setting("Max Distance", this, 50, 10, 50, true);

	private final Setting speedLine = new Setting("SpeedLine", this, "Speed");
	public final Setting speedMode = new Setting("Speed Mode", this, "km/h", new ArrayList<>(Arrays.asList("b/s", "km/h")));

	private final Setting logoLine = new Setting("LogoLine", this, "Logo");
	public final Setting logoMode = new Setting("Logo Mode", this, LogoMode.Simple);
	public final Setting logoImage = new Setting("Logo Image", this, LogoImage.New).setVisible(() -> logoMode.checkValString("Image"));
	public final Setting logoGlow = new Setting("Glow", this, false);
	public final Setting logoBold = new Setting("Name Bold", this, false);

	private final Setting indicLine = new Setting("IndicLine", this, "Indicators");
	public final Setting indicThemeMode = new Setting("Indicators Theme", this, IndicatorsThemeMode.Default);
	public final Setting indicShadowSliders = new Setting("Indicators Shadow Sliders", this, false);

	private final Setting thudLine = new Setting("ThudLine", this, "TargetHud");
	public final Setting thudTheme = new Setting("TargetHud Theme", this, TargetHudThemeMode.Vega);
	public final Setting thudShadowSliders = new Setting("TargetHud Shadow Sliders", this, false);

	private final Setting crystalpsLine = new Setting("CrystalPRLine", this, "Crystal Per Second");
	public final Setting crystalpsY = new Setting("Crystal Per Second Y", this, 50, 0, mc.displayHeight, true); 

	public HUD() {
		super("HudEditor", "hud editor", Category.CLIENT);

		instance = this;

		settingManager.register(astolfoColor);
		settingManager.register(offsets);
		settingManager.register(glow);
		settingManager.register(glowV2);
		settingManager.register(glowOffset);
		settingManager.register(glowRadius);
		settingManager.register(glowAlpha);
		settingManager.register(background);
		settingManager.register(bgAlpha);

		settingManager.register(arrLine);
		settingManager.register(arrMode);
		settingManager.register(arrY);
		settingManager.register(arrColor);
		settingManager.register(arrGradient);
		settingManager.register(arrGradientDiff);
		settingManager.register(arrGlowBackground);

		settingManager.register(welLine);
		settingManager.register(welColor);

		settingManager.register(pvpLine);
		settingManager.register(pvpY);

		settingManager.register(armLine);
		settingManager.register(armExtra);
		settingManager.register(armDmg);

		settingManager.register(radarLine);
		settingManager.register(radarDist);

		settingManager.register(speedLine);
		settingManager.register(speedMode);

		settingManager.register(logoLine);
		settingManager.register(logoMode);
		settingManager.register(logoImage);
		settingManager.register(logoGlow);
		settingManager.register(logoBold);

		settingManager.register(indicLine);
		settingManager.register(indicThemeMode);
		settingManager.register(indicShadowSliders);

		settingManager.register(thudLine);
		settingManager.register(thudTheme);
		settingManager.register(thudShadowSliders);

		settingManager.register(crystalpsLine);
		settingManager.register(crystalpsY);
	}

	public void onEnable() {
        mc.displayGuiScreen(Kisman.instance.hudGui);
		super.setToggled(false);
	}

	public enum LogoMode {Simple, CSGO, Image, GishCode}
	public enum LogoImage {Old, New}
	public enum Gradient {None, Rainbow, Astolfo, Pulsive}
	public enum IndicatorsThemeMode {Default, Rewrite}
	public enum TargetHudThemeMode {Vega, Rewrite, NoRules, Simple, Astolfo}
}
