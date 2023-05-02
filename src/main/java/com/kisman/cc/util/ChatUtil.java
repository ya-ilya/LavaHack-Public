package com.kisman.cc.util;

import com.kisman.cc.Kisman;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class ChatUtil {
	private static final Minecraft mc = Minecraft.getMinecraft();

	public static void component(ITextComponent component) {
		if (mc.player == null || mc.ingameGUI == null) return;

		mc.ingameGUI.getChatGUI()
				.printChatMessage(new TextComponentTranslation(TextFormatting.WHITE.toString())
				.appendSibling(component));
	}

	public static void simpleMessage(Object message) {
		component(new TextComponentTranslation((String) message));
	}
	
	public static void message(Object message) {
		component(new TextComponentTranslation(TextFormatting.GRAY + "[" + TextFormatting.WHITE + Kisman.getName() + TextFormatting.GRAY + "] " + message));
	}

	public static void complete(Object message) {
		component(new TextComponentTranslation(TextFormatting.GRAY + "[" + TextFormatting.LIGHT_PURPLE + Kisman.getName() + TextFormatting.GRAY + "] " + message));
	}
	
	public static void warning(Object message) {
		component(new TextComponentTranslation(TextFormatting.GRAY + "[" + TextFormatting.GOLD + Kisman.getName() + TextFormatting.GRAY + "] " + message));
	}
	
	public static void error(Object message) {
		component(new TextComponentTranslation(TextFormatting.GRAY + "[" + TextFormatting.RED + Kisman.getName() + TextFormatting.GRAY + "] " + message));
	}
}
