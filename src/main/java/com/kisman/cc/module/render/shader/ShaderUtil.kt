package com.kisman.cc.module.render.shader

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.shader.Framebuffer

class ShaderUtil {
    companion object {
        fun setupUniforms(shader: net.minecraft.client.shader.Shader, uniforms: Array<Uniform>) {
            for (uniform in uniforms) {
                if (uniform is Uniform.Float) {
                    shader.shaderManager.getShaderUniform(uniform.name)?.set(uniform.value)
                }
                if (uniform is Uniform.Int) {
                    shader.shaderManager.getShaderUniform(uniform.name)?.set(uniform.value.toFloat())
                }
            }
        }

        fun clearFrameBuffer(frameBuffer: Framebuffer) {
            frameBuffer.apply {
                bindFramebuffer(true)
                GlStateManager.clearColor(framebufferColor[0], framebufferColor[1], framebufferColor[2], framebufferColor[3])

                var mask = 16384
                if (useDepth) {
                    GlStateManager.clearDepth(1.0)
                    mask = mask or 256
                }

                GlStateManager.clear(mask)
            }
        }
    }

    sealed interface Uniform {
        class Float(val name: String, val value: kotlin.Float) : Uniform
        class Int(val name: String, val value: kotlin.Int) : Uniform
    }
}