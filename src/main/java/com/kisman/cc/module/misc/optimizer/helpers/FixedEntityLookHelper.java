package com.kisman.cc.module.misc.optimizer.helpers;

import com.kisman.cc.mixin.mixins.accessor.AccessorEntityLookHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.math.MathHelper;

public class FixedEntityLookHelper extends EntityLookHelper {
    private final EntityLiving entityLiving;

    public FixedEntityLookHelper(EntityLiving entity) {
        super(entity);

        entityLiving = entity;
    }

    public void onUpdateLook() {
        entityLiving.rotationPitch = 0.0f;

        AccessorEntityLookHelper accessor = (AccessorEntityLookHelper) this;
        if (getIsLooking()) {
            accessor.setIsLooking(false);
            double d0 = accessor.getPosX() - entityLiving.posX;
            double d2 = accessor.getPosY() - (entityLiving.posY + entityLiving.getEyeHeight());
            double d3 = accessor.getPosZ() - entityLiving.posZ;
            double d4 = MathHelper.sqrt(d0 * d0 + d3 * d3);
            float f = (float)(Math.atan2(d3, d0) * 180.0 / 3.141592653589793) - 90.0f;
            float f2 = (float)(-(Math.atan2(d2, d4) * 180.0 / 3.141592653589793));
            entityLiving.rotationPitch = accessor.invokeUpdateRotation(entityLiving.rotationPitch, f2, accessor.getDeltaLookPitch());
            entityLiving.rotationYawHead = accessor.invokeUpdateRotation(entityLiving.rotationYawHead, f, accessor.getDeltaLookYaw());
        } else {
            entityLiving.rotationYawHead = accessor.invokeUpdateRotation(entityLiving.rotationYawHead, entityLiving.renderYawOffset, 10.0f);
        }
        float f3 = MathHelper.wrapDegrees(entityLiving.rotationYawHead - entityLiving.renderYawOffset);
        if (!entityLiving.getNavigator().noPath()) {
            if (f3 < -75.0f) entityLiving.rotationYawHead = entityLiving.renderYawOffset - 75.0f;
            if (f3 > 75.0f) entityLiving.rotationYawHead = entityLiving.renderYawOffset + 75.0f;
        }
    }
}