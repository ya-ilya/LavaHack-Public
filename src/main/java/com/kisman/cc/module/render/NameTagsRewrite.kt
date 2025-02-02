package com.kisman.cc.module.render

import com.kisman.cc.manager.managers.FriendManager
import com.kisman.cc.module.Category
import com.kisman.cc.module.Module
import com.kisman.cc.module.combat.autorer.util.ProjectionUtils
import com.kisman.cc.setting.Setting
import com.kisman.cc.setting.util.GlowRendererPattern
import com.kisman.cc.util.Colour
import com.kisman.cc.util.ColourUtil
import com.kisman.cc.util.Render2DUtil
import com.kisman.cc.util.customfont.CustomFontUtil
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11

class NameTagsRewrite : Module("NameTagsRewrite", "Renders info about players.", Category.RENDER) {
    val scale = Setting("Scale", this, 2.5, 0.1, 5.0, false)
    private val ping = Setting("Ping", this, true)
//  val pops = Setting("Pops", this, true)
    val health = Setting("Health", this, true)
    val background = Setting("Background", this, false)
    private val backgroundAlpha = Setting("BG Alpha", this, 100.0, 0.0, 255.0, true).setVisible {background.valBoolean}

    val glow: Setting = Setting("Glow Background", this, false).setVisible { background.valBoolean }

    private val glowSetting = GlowRendererPattern(this) { background.valBoolean && glow.valBoolean }

    init {
        register(scale)
        register(ping)
//      register(pops)
        register(health)
        register(background)
        register(backgroundAlpha)
        register(glow)

        glowSetting.init()
    }

    @SubscribeEvent fun onRender(event: RenderGameOverlayEvent.Text) {
        for (player in mc.world.playerEntities) {
            if (player == mc.player) continue

            val health = player.health + player.getAbsorptionAmount()
            val builder = StringBuilder()
            val yOffset = if (player.isSneaking) 1.75 else 2.25
            val deltas = Render2DUtil.getDeltas(event.partialTicks, player)
            val projection = ProjectionUtils.toScaledScreenPos(
                Vec3d(deltas[0], deltas[1], deltas[2]).add(0.0, yOffset, 0.0)
            )

            GL11.glPushMatrix()

            GL11.glTranslated(projection.x, projection.y, 0.0)
            GL11.glTranslated(scale.valDouble, scale.valDouble, 0.0)


            if (FriendManager.instance.isFriend(player)) builder.append(TextFormatting.AQUA)
            builder.append("${player.name}${TextFormatting.RESET}")
            var ping = -1
            try {
                ping = mc.player.connection.getPlayerInfo(player.uniqueID).responseTime
            } catch (_: Exception) {}
            if (this.ping.valBoolean) builder.append(" $ping ms")
            // if (this.pops.valBoolean) ...
            if (this.health.valBoolean) builder.append(" ${ColourUtil.healthColor(player)}${MathHelper.ceil(health)}${TextFormatting.RESET}")

            if (glow.valBoolean) {
                glowSetting.draw(
                    Colour(12, 12, 12, backgroundAlpha.valInt),
                    -((CustomFontUtil.getStringWidth(builder.toString()) - 2) / 2),
                    -(CustomFontUtil.getFontHeight() + 2),
                    CustomFontUtil.getStringWidth(builder.toString()) + 4,
                    CustomFontUtil.getFontHeight()
                )
            }
            Render2DUtil.drawRect(
                -((CustomFontUtil.getStringWidth(builder.toString()) - 2) / 2).toDouble(),
                -(CustomFontUtil.getFontHeight() + 2).toDouble(),
                ((CustomFontUtil.getStringWidth(builder.toString()) + 2) / 2).toDouble(),
                1.0,
                Colour(12, 12, 12, if (glow.valBoolean) 0 else backgroundAlpha.valInt).rgb
            )

            CustomFontUtil.drawStringWithShadow(
                builder.toString(),
                -(CustomFontUtil.getStringWidth(builder.toString()) / 2).toDouble(),
                -CustomFontUtil.getFontHeight().toDouble(),
                -1
            )

            GL11.glPopMatrix()
        }
    }
}