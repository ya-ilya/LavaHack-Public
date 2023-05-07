package com.kisman.cc.gui.misc;

import com.kisman.cc.gui.book.components.ActionButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/* Right panel, special module configuration GUI displayed next to Main GUI */
@SideOnly(Side.CLIENT)
public abstract class RightPanel extends GuiScreen {
    public RightPanel() {
        ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
        this.setWorldAndResolution(Minecraft.getMinecraft(), scale.getScaledWidth(), scale.getScaledHeight());
    }

    protected void actionPerformed(GuiButton button) {
        if (button instanceof ActionButton) {
            ((ActionButton) button).onClick(this);
        }
    }
}
