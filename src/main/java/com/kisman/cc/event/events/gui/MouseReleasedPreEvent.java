package com.kisman.cc.event.events.gui;

import com.kisman.cc.event.Event;

public class MouseReleasedPreEvent extends Event {
    public int mouseX, mouseY, mouseButton;
    public GuiRenderPostEvent.Gui gui;

    public MouseReleasedPreEvent(int mouseX, int mouseY, int mouseButton) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.mouseButton = mouseButton;
        this.gui = GuiRenderPostEvent.Gui.OldGui;
    }

    public MouseReleasedPreEvent(int mouseX, int mouseY, int mouseButton, GuiRenderPostEvent.Gui gui) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.mouseButton = mouseButton;
        this.gui = gui;
    }
}
