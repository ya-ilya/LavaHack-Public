package com.kisman.cc.gui.hud.hudeditor;

import com.kisman.cc.Kisman;
import com.kisman.cc.gui.hud.hudeditor.component.Component;
import com.kisman.cc.gui.hud.hudeditor.component.components.Draggable;
import com.kisman.cc.gui.hud.hudmodule.HudModule;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;

public class HudEditorGui extends GuiScreen {
    private final ArrayList<Component> components = new ArrayList<>();

    public HudEditorGui() {
        for (HudModule mod : Kisman.instance.hudModuleManager.modules) {
            if (mod.drag) components.add(new Draggable(mod));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float particalTicks) {
        drawDefaultBackground();
        for (Component comp : components) comp.drawScreen(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Component comp : components) comp.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Component comp : components) comp.mouseReleased(mouseX, mouseY, state);
    }
}
