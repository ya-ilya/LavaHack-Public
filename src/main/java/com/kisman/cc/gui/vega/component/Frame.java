package com.kisman.cc.gui.vega.component;

import com.kisman.cc.Kisman;
import com.kisman.cc.gui.vega.component.components.Button;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.module.client.Config;
import com.kisman.cc.util.Timer;
import com.kisman.cc.util.customfont.CustomFontUtil;
import com.kisman.cc.util.gish.ColorUtil;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;

public class Frame {
    public ArrayList<Button> buttons;

    public Category cat;

    public int width = 114, height = 13;
    public int x, y;
    public int dragX = 0, dragY = 0;
    public boolean dragging = false;
    public boolean open = true;
    public Timer renderTimer;
    public Frame(Category cat, int x, int y) {
        buttons = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.cat = cat;
        this.renderTimer = new Timer();
        this.renderTimer.reset();
        int offset = height;

        for (Module mod : Kisman.instance.moduleManager.modules) {
            if (mod.getCategory().equals(cat)) {
                buttons.add(new Button(this.x, this.y, offset, width, height, this, mod));
                offset += height;
            }
        }
    }

    public void renderComponent() {
        Gui.drawRect(this.x + 3, this.y + 3, this.x + this.width + 3, this.y + this.height - 3, (ColorUtil.getColor(33, 33, 42)));
        Gui.drawRect(this.x + 3, this.y, this.x + this.width + 3, this.y + this.height, (ColorUtil.getColor(33, 33, 42)));
        Gui.drawRect(this.x + 2, this.y + 2, this.x + this.width + 2, this.y + this.height - 2, (ColorUtil.getColor(45, 45, 55)));
        Gui.drawRect(this.x + 2, this.y, this.x + this.width + 2, this.y + this.height, (ColorUtil.getColor(45, 45, 55)));
        Gui.drawRect(this.x + 1, this.y + 1, this.x + this.width + 1, this.y + this.height - 1, (ColorUtil.getColor(60, 60, 70)));
        Gui.drawRect(this.x + 1, this.y, this.x + this.width + 1, this.y + this.height, (ColorUtil.getColor(60, 60, 70)));
        Gui.drawRect(this.x - 3, this.y - 8, this.x + this.width + 3, this.y + this.height - 3, (ColorUtil.getColor(33, 33, 42)));
        Gui.drawRect(this.x - 3, this.y, this.x + this.width + 3, this.y + this.height, (ColorUtil.getColor(33, 33, 42)));
        Gui.drawRect(this.x - 2, this.y - 7, this.x + this.width + 2, this.y + this.height - 2, (ColorUtil.getColor(45, 45, 55)));
        Gui.drawRect(this.x - 2, this.y, this.x + this.width + 2, this.y + this.height, (ColorUtil.getColor(45, 45, 55)));
        Gui.drawRect(this.x - 1, this.y - 6, this.x + this.width + 1, this.y + this.height - 1, (ColorUtil.getColor(60, 60, 70)));
        Gui.drawRect(this.x - 1, this.y, this.x + this.width + 1, this.y + this.height, (ColorUtil.getColor(60, 60, 70)));
        Gui.drawRect(this.x, this.y - 5, this.x + this.width, this.y + this.height, (ColorUtil.astolfoColors(100, 100)));
        Gui.drawRect(this.x - 3, this.y - 1, this.x + this.width + 3, this.y + this.height + 3, (ColorUtil.getColor(33, 33, 42)));
        Gui.drawRect(this.x - 2, this.y - 2, this.x + this.width + 2, this.y + this.height + 2, (ColorUtil.getColor(45, 45, 55)));
        Gui.drawRect(this.x - 1, this.y - 3, this.x + this.width + 1, this.y + this.height + 1, (ColorUtil.getColor(60, 60, 70)));
        Gui.drawRect(this.x, this.y - 4, this.x + this.width, this.y + this.height, (ColorUtil.getColor(34, 34, 40)));

        String str = cat.getName() + (Config.instance.guiRenderSize.getValBoolean() && !buttons.isEmpty() ? " [" + buttons.size() + "]" : "");

        CustomFontUtil.drawCenteredStringWithShadow(str, x + (width / 2.0), y + ((height - CustomFontUtil.getFontHeight()) / 2.0), open ? ColorUtil.astolfoColors(100, 100) : -1);

        if (open && !buttons.isEmpty()) for (Button button : buttons) button.renderComponent();
    }

    public void updateComponent(int mouseX, int mouseY) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    public void refresh() {
        int off = height;

        for (Button b : buttons) {
            b.offset = off;
            off += height;

            if (b.open) {
                for (Component comp : b.comp) {
                    comp.newOff(off);
                    comp.renderComponent();
                    off += height;
                }
                off += 3;
            }
        }
    }
}
