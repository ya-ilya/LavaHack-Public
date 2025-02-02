package com.kisman.cc.module.render.shader.shaders.troll

import com.kisman.cc.util.ChatUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.shader.ShaderGroup
import net.minecraft.client.shader.ShaderLinkHelper
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

/**
 * @author TrollHack 0.0.2
 */
class ShaderHelper(shaderIn: ResourceLocation) {
    private val mc = Minecraft.getMinecraft()
    private var oldWidth = -1
    private var oldHeight = -1

    val shader: ShaderGroup? =
        if (!OpenGlHelper.shadersSupported) {
            ChatUtil.warning("Shaders are unsupported by OpenGL!")
            null
        } else {
            try {
                ShaderLinkHelper.setNewStaticShaderLinkHelper()

                ShaderGroup(mc.textureManager, mc.resourceManager, mc.framebuffer, shaderIn).also {
                    it.createBindFramebuffers(mc.displayWidth, mc.displayHeight)
                }
            } catch (e: Exception) {
                ChatUtil.warning("Failed to load shaders")
                e.printStackTrace()
                null
            }
        }

    private var frameBuffersInitialized = false

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!frameBuffersInitialized) {
            shader?.createBindFramebuffers(mc.displayWidth, mc.displayHeight)

            frameBuffersInitialized = true
        }
        if (oldWidth != mc.displayWidth || oldHeight != mc.displayHeight) {
            oldWidth = mc.displayWidth
            oldHeight = mc.displayHeight
            shader?.createBindFramebuffers(mc.displayWidth, mc.displayHeight)
        }
    }

    fun getFrameBuffer(name: String) = shader?.getFramebufferRaw(name)
}