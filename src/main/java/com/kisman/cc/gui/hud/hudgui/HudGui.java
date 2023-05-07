package com.kisman.cc.gui.hud.hudgui;

import com.kisman.cc.gui.component.Component;
import com.kisman.cc.gui.component.Frame;
import com.kisman.cc.gui.hud.hudmodule.HudCategory;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;

public class HudGui extends GuiScreen {
    public static ArrayList<Frame> frames;

    public HudGui() {
		frames = new ArrayList<>();
		int frameX = 5;
		for (HudCategory category : HudCategory.values()) {
			Frame frame = new Frame(category);
			frame.setX(frameX);
			frames.add(frame);
			frameX += frame.getWidth() + 1;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		for (Frame frame : frames) {
			frame.renderFrame(this.fontRenderer);
			frame.updatePosition(mouseX, mouseY);
			for (Component comp : frame.getComponents()) comp.updateComponent(mouseX, mouseY);
		}
	}

	@Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (Frame frame : frames) {
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) frame.setOpen(!frame.isOpen());
			if (frame.isOpen() && !frame.getComponents().isEmpty()) {
				for (Component component : frame.getComponents()) component.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for (Frame frame : frames) {
			if (frame.isOpen() && keyCode != 1 && !frame.getComponents().isEmpty()) {
				for (Component component : frame.getComponents()) component.keyTyped(typedChar, keyCode);
			}
		}
		if (keyCode == 1) this.mc.displayGuiScreen(null);
	}

	@Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
		for (Frame frame : frames) frame.setDrag(false);
		for (Frame frame : frames) {
			if (frame.isOpen() && !frame.getComponents().isEmpty()) {
				for (Component component : frame.getComponents()) component.mouseReleased(mouseX, mouseY, state);
			}
		}
	}
}
