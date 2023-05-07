package com.kisman.cc.gui

import com.kisman.cc.Kisman
import com.kisman.cc.util.Colour
import com.kisman.cc.util.Render2DUtil
import com.kisman.cc.util.customfont.CustomFontUtil
import com.kisman.cc.util.gish.ColorUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution

class MainGui {
    companion object {
        fun openGui(bar : SelectionBar) {
            when (bar.selection) {
                Gui.ClickGui -> Minecraft.getMinecraft().displayGuiScreen(Kisman.instance.halqGui)
                Gui.CSGOGui -> Minecraft.getMinecraft().displayGuiScreen(Kisman.instance.clickGuiNew)
                Gui.Console -> Minecraft.getMinecraft().displayGuiScreen(Kisman.instance.consoleGui)
            }
        }
    }

    class SelectionBar(
            defaultSelection : Gui
    ) {
        var selection : Gui
        val backgroundColor : Colour = Colour(20, 20, 20, 200)
        val offset : Int = 5

        init {
            selection = defaultSelection
        }

        fun drawScreen() {
            var startX = ScaledResolution(Minecraft.getMinecraft()).scaledWidth / 2 - getSelectionBarWidth() / 2
            Render2DUtil.drawRectWH(startX.toDouble(), 0.0, getSelectionBarWidth().toDouble(), (CustomFontUtil.getFontHeight() + offset * 2).toDouble(), backgroundColor.rgb)

            for (gui in Gui.values()) {
                CustomFontUtil.drawStringWithShadow(
                        gui.displayName,
                        (startX + offset).toDouble(),
                        offset.toDouble(),
                        if (gui == selection) ColorUtil.astolfoColors(100, 100) else -1
                )
                startX += offset * 2 + CustomFontUtil.getStringWidth(gui.displayName)
            }
        }

        fun mouseClicked(mouseX : Int, mouseY : Int) : Boolean {
            val startX = ScaledResolution(Minecraft.getMinecraft()).scaledWidth / 2 - getSelectionBarWidth() / 2
            if (mouseX >= startX && mouseX <= startX + getSelectionBarWidth() && mouseY >= 0 && mouseY <= CustomFontUtil.getFontHeight() + offset * 2) {
                for ((count, gui) in Gui.values().withIndex()) {
                    println("Click k $startX")
                    if (mouseX >= startX + count * (offset * 2 + CustomFontUtil.getStringWidth(gui.displayName)) && mouseX <= startX + count * (offset * 2 + CustomFontUtil.getStringWidth(gui.displayName)) + (offset * 2 + CustomFontUtil.getStringWidth(gui.displayName))) {
                        selection = gui
                        return false
                    }
                }
            }
            return true
        }

        private fun getSelectionBarWidth() : Int {
            var width = 0

            for (gui in Gui.values()) {
                width += offset * 2 + CustomFontUtil.getStringWidth(gui.displayName)
            }

            return width
        }
    }

    enum class Gui(val displayName: String) {
        ClickGui("Click Gui"),
        CSGOGui("CSGO Gui"),
        Console("Console")
    }
}