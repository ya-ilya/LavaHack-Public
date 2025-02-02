package com.kisman.cc.module.combat.autorer

import com.kisman.cc.util.CrystalUtils
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

object AutoRerUtil {
    @JvmStatic private val mc: Minecraft = Minecraft.getMinecraft()

    @JvmStatic
    fun getDamageByCrystal(target: Entity, terrain: Boolean, crystal: BlockPos): Float {
        if (mc.world == null) {
            return 0f
        }
        return CrystalUtils.calculateDamage(mc.world, crystal.x + 0.5f, crystal.y + 1, crystal.z + 0.5, target, terrain, true)
    }

    @JvmStatic
    fun getSelfDamageByCrystal(terrain: Boolean, crystal: BlockPos): Float {
        return getDamageByCrystal(mc.player, terrain, crystal)
    }

    @JvmStatic
    fun getPlaceInfo(placePos: BlockPos, target: EntityLivingBase, terrain: Boolean): PlaceInfo {
        return PlaceInfo(target, placePos, getSelfDamageByCrystal(terrain, placePos), getDamageByCrystal(target, terrain, placePos), null)
    }

    @JvmStatic
    fun toVec3dCenter(pos : BlockPos) : Vec3d {
        return Vec3d(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
    }
}