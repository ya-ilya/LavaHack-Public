package com.kisman.cc.module.combat.autorer

import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos

class PlaceInfo(
    var target: EntityLivingBase,
    var blockPos: BlockPos,
    var selfDamage: Float,
    var targetDamage: Float,
    var side: EnumFacing?
)