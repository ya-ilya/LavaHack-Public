package com.kisman.cc.setting.util

import com.kisman.cc.Kisman
import com.kisman.cc.module.Module
import com.kisman.cc.setting.Setting
import com.kisman.cc.util.Colour
import com.kisman.cc.util.Rendering
import com.kisman.cc.util.enums.RenderingRewriteModes
import net.minecraft.client.Minecraft
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import java.util.function.Supplier

class RenderingRewritePattern(
    val module : Module,
    val visible : Supplier<Boolean>
) {
    val mode: Setting = Setting("Render Mode", module, RenderingRewriteModes.Filled).setVisible { visible.get() }
    val lineWidth: Setting = Setting("Render Line Width", module, 1.0, 0.1, 5.0, false).setVisible {
        visible.get() && mode.valEnum != RenderingRewriteModes.Filled && mode.valEnum != RenderingRewriteModes.FilledGradient
    }

    //Colors
    private val color1 = Setting("Render Color", module, "Render Color", Colour(255, 0, 0, 255)).setVisible { visible.get() }
    private val color2 = Setting("Render Second Color", module, "Render Second Color", Colour(0, 120, 255, 255)).setVisible {
        visible.get() && (
                mode.valEnum == RenderingRewriteModes.FilledGradient ||
                        mode.valEnum == RenderingRewriteModes.OutlineGradient ||
                        mode.valEnum == RenderingRewriteModes.BothGradient ||
                        mode.valEnum == RenderingRewriteModes.GlowOutline ||
                        mode.valEnum == RenderingRewriteModes.Glow
                )
    }

    fun init() {
        Kisman.instance.settingManager.register(mode)
        Kisman.instance.settingManager.register(lineWidth)
        Kisman.instance.settingManager.register(color1)
        Kisman.instance.settingManager.register(color2)
    }

    fun draw(aabb : AxisAlignedBB) {
        Rendering.draw(
            Rendering.correct(aabb),
            lineWidth.valFloat,
            color1.colour,
            color2.colour,
            (mode.valEnum as RenderingRewriteModes).mode
        )
    }

    fun draw(pos : BlockPos) {
        draw(
            Minecraft.getMinecraft().world.getBlockState(pos).getSelectedBoundingBox(
                Minecraft.getMinecraft().world,
                pos
            )
        )
    }
}