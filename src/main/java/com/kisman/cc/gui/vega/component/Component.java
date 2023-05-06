package com.kisman.cc.gui.vega.component;

public abstract class Component {
    public abstract void renderComponent();
    public abstract void updateComponent(int mouseX, int mouseY);
    public abstract void mouseClicked(int mouseX, int mouseY, int button);
    public abstract void mouseReleased(int mouseX, int mouseY, int button);
    public abstract void keyTyped(char typedChar, int key);
    public abstract void newOff(int newOff);
}
