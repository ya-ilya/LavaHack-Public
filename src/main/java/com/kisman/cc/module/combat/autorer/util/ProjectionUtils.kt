package com.kisman.cc.module.combat.autorer.util

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.Matrix4f
import net.minecraft.util.math.Vec3d
import javax.vecmath.Vector4f

/**
 * @author TrollHack 0.0.2
 */
object ProjectionUtils {
    private val mc = Minecraft.getMinecraft()

    private val modelMatrix = Matrix4f()
    private val projectionMatrix = Matrix4f()

    var resolution = ScaledResolution(mc); private set
    private var projectPos = Vec3d.ZERO

    fun toScaledScreenPos(pos: Vec3d): Vec3d {
        return toScreenPos(pos, resolution.scaledWidth_double.toFloat(), resolution.scaledHeight_double.toFloat())
    }

    private fun toScreenPos(pos: Vec3d, width: Float, height: Float): Vec3d {
        val vector4f = transformVec3(pos)

        vector4f.x = width / 2.0f + (0.5f * vector4f.x * width + 0.5f)
        vector4f.y = height / 2.0f - (0.5f * vector4f.y * height + 0.5f)
        val posZ = if (isVisible(vector4f, width, height)) 0.0 else -1.0

        return Vec3d(vector4f.x.toDouble(), vector4f.y.toDouble(), posZ)
    }

    private fun transformVec3(vec3d: Vec3d): Vector4f {
        val relativeX = (projectPos.x - vec3d.x).toFloat()
        val relativeY = (projectPos.y - vec3d.y).toFloat()
        val relativeZ = (projectPos.z - vec3d.z).toFloat()
        val vector4f = Vector4f(relativeX, relativeY, relativeZ, 1.0f)

        transformVec4(vector4f, modelMatrix)
        transformVec4(vector4f, projectionMatrix)

        if (vector4f.w > 0.0f) {
            vector4f.x *= -100000.0f
            vector4f.y *= -100000.0f
        } else {
            val invert = 1.0f / vector4f.w
            vector4f.x *= invert
            vector4f.y *= invert
        }

        return vector4f
    }

    private fun transformVec4(vec: Vector4f, matrix: Matrix4f) {
        val x = vec.x
        val y = vec.y
        val z = vec.z
        vec.x = x * matrix.m00 + y * matrix.m10 + z * matrix.m20 + matrix.m30
        vec.y = x * matrix.m01 + y * matrix.m11 + z * matrix.m21 + matrix.m31
        vec.z = x * matrix.m02 + y * matrix.m12 + z * matrix.m22 + matrix.m32
        vec.w = x * matrix.m03 + y * matrix.m13 + z * matrix.m23 + matrix.m33
    }

    private fun isVisible(vector4f: Vector4f, width: Float, height: Float): Boolean {
        return vector4f.x in 0.0f..width && vector4f.y in 0.0f..height
    }
}