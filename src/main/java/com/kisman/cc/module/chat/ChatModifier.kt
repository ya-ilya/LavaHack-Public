package com.kisman.cc.module.chat

import com.kisman.cc.Kisman
import com.kisman.cc.module.Category
import com.kisman.cc.module.Module
import com.kisman.cc.setting.Setting
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.random.Random

class ChatModifier : Module(
        "ChatModifier",
        "ChatAnimation + CustomY + Suffix + AntiSpamBypass + TTF + AutoGlobal",
        Category.CHAT
) {
    companion object {
        lateinit var INSTANCE: ChatModifier
    }

    val animation = Setting("Animation", this, false)
    private val suffix = Setting("Suffix", this, false)
    private val antiSpamBypass = Setting("Anti Spam Bypass", this, false)
    private val autoGlobal = Setting("Auto Global", this, false)
    val customY = Setting("Custom Y", this, false)
    val customYVal: Setting = Setting("Custom Y Value", this, 50.0, 0.0, 100.0, true).setVisible { customY.valBoolean }
    val ttf = Setting("TTF", this, false)

    init {
        INSTANCE = this

        register(animation)
        register(suffix)
        register(antiSpamBypass)
        register(autoGlobal)
        register(customY)
        register(customYVal)
        register(ttf)
    }

    @SubscribeEvent
    fun onChat(event: ClientChatEvent) {
        if (!event.message.startsWith("/") &&
                !event.message.startsWith(Kisman.instance.commandManager.cmdPrefixStr) &&
                !event.message.startsWith(".") &&
                !event.message.startsWith(",") &&
                !event.message.startsWith(";") &&
                !event.message.startsWith(":") &&
                !event.message.startsWith("-") &&
                !event.message.startsWith("+")) {
            if (autoGlobal.valBoolean) {
                event.message = "!${event.message}"
            }
            if (suffix.valBoolean) {
                event.message = "${event.message} | ${Kisman.getName()} own you and all"
            }
            if (antiSpamBypass.valBoolean) {
                event.message = "${event.message} | ${Random.nextInt()}"
            }
        }
    }
}