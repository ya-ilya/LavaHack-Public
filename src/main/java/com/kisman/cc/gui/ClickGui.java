package com.kisman.cc.gui;

import com.kisman.cc.util.enums.LineMode;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClickGui extends GuiScreen {
	public static boolean line = false;
	public static boolean rainbowLine = false;
	public static boolean rainbowBackground = false;

	public static LineMode lineMode = LineMode.LEFT;
	public static LineMode setLineMode = LineMode.SETTINGDEFAULT;

	public static int RLine = 255;
	public static int GLine = 0;
	public static int BLine = 0;
	public static int ALine = 150;

	public static int RBackground = 80;
	public static int GBackground = 75;
	public static int BBackground = 75;
	public static int ABackground = 150;

	public static int RABackground = 136;
	public static int GABackground = 189;
	public static int BABackground = 189;
	public static int AABackground = 255;

	public static int RHoveredModule = 95;
	public static int GHoveredModule = 95;
	public static int BHoveredModule = 87;
	public static int AHoveredModule = 150;

	public static int RNoHoveredModule = 14;
	public static int GNoHoveredModule = 14;
	public static int BNoHoveredModule = 14;
	public static int ANoHoveredModule = 255;


	public static int RText = 166;
	public static int GText = 161;
	public static int BText = 160;
	public static int AText = 255;

	public static int RActiveText = 255;
	public static int GActiveText = 255;
	public static int BActiveText = 255;
	public static int AActiveText = 255;

	public static LineMode getSetLineMode() {
		return setLineMode;
	}

	public static boolean isRainbowBackground() {
		return rainbowBackground;
	}

	public static int getRHoveredModule() {
		return RHoveredModule;
	}

	public static int getGHoveredModule() {
		return GHoveredModule;
	}

	public static int getBHoveredModule() {
		return BHoveredModule;
	}

	public static int getAHoveredModule() {
		return AHoveredModule;
	}

	public static int getRNoHoveredModule() {
		return RNoHoveredModule;
	}

	public static int getGNoHoveredModule() {
		return GNoHoveredModule;
	}

	public static int getBNoHoveredModule() {
		return BNoHoveredModule;
	}

	public static int getANoHoveredModule() {
		return ANoHoveredModule;
	}

	public static int getGActiveText() {
		return GActiveText;
	}

	public static int getBActiveText() {
		return BActiveText;
	}

	public static int getAActiveText() {
		return AActiveText;
	}

	public static int getRText() {
		return RText;
	}

	public static int getGText() {
		return GText;
	}

	public static int getBText() {
		return BText;
	}

	public static int getAText() {
		return AText;
	}

	public static LineMode getLineMode() {
		return lineMode;
	}

	public static int getALine() {
		return ALine;
	}

	public static int getABackground() {
		return ABackground;
	}

	public static boolean isLine() {
		return line;
	}

	public static void setLine(boolean line) {
		ClickGui.line = line;
	}

	public static int getRLine() {
		return RLine;
	}

	public static int getGLine() {
		return GLine;
	}

	public static int getBLine() {
		return BLine;
	}

	public static int getRBackground() {
		return RBackground;
	}

	public static int getGBackground() {
		return GBackground;
	}

	public static int getBBackground() {
		return BBackground;
	}
}