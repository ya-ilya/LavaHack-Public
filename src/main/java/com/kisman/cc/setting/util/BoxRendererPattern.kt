package com.kisman.cc.setting.util

import com.kisman.cc.Kisman
import com.kisman.cc.module.Module
import com.kisman.cc.setting.Setting
import com.kisman.cc.util.Colour
import com.kisman.cc.util.enums.BoxRenderModes
import com.kisman.cc.util.render.objects.Box
import com.kisman.cc.util.render.objects.BoxObject
import net.minecraft.client.Minecraft
import net.minecraft.util.math.BlockPos

class BoxRendererPattern(val module: Module) {
    val mode = Setting("Mode", module, BoxRenderModes.Filled)
    val depth = Setting("Depth", module, false)
    val alpha = Setting("Alpha", module, true)
    val width = Setting("Width", module, 2.0, 0.25, 5.0, false).setVisible { !mode.valEnum.equals(BoxRenderModes.Filled) }
    val offset = Setting("Offset", module, 0.002, 0.002, 0.2, false)

    fun init() {
        Kisman.instance.settingManager.rSetting(mode)
        Kisman.instance.settingManager.rSetting(depth)
        Kisman.instance.settingManager.rSetting(alpha)
        Kisman.instance.settingManager.rSetting(width)
        Kisman.instance.settingManager.rSetting(offset)
    }

    fun draw(ticks: Float, color: Colour, pos: BlockPos, alphaVal: Int) {
        BoxObject(
            Box.byAABB(
                Minecraft.getMinecraft().world.getBlockState(pos).getSelectedBoundingBox(Minecraft.getMinecraft().world, pos)
                    .grow(offset.valDouble)),
            color.withAlpha(alphaVal),
            mode.valEnum as BoxRenderModes,
            width.valFloat,
            depth.valBoolean,
            alpha.valBoolean
        ).draw(ticks)
    }
}