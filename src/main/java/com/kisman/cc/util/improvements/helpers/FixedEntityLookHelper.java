package com.kisman.cc.util.improvements.helpers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.math.MathHelper;

public class FixedEntityLookHelper extends EntityLookHelper {
    public FixedEntityLookHelper(EntityLiving entity) {
        super(entity);
    }
    
    public void onUpdateLook() {
        entity.rotationPitch = 0.0f;
        if (isLooking) {
            isLooking = false;
            double d0 = this.posX - this.entity.posX;
            double d2 = this.posY - (this.entity.posY + this.entity.getEyeHeight());
            double d3 = this.posZ - this.entity.posZ;
            double d4 = MathHelper.sqrt(d0 * d0 + d3 * d3);
            float f = (float)(Math.atan2(d3, d0) * 180.0 / 3.141592653589793) - 90.0f;
            float f2 = (float)(-(Math.atan2(d2, d4) * 180.0 / 3.141592653589793));
            entity.rotationPitch = updateRotation(entity.rotationPitch, f2, deltaLookPitch);
            entity.rotationYawHead = updateRotation(entity.rotationYawHead, f, deltaLookYaw);
        } else entity.rotationYawHead = updateRotation(entity.rotationYawHead, entity.renderYawOffset, 10.0f);
        float f3 = MathHelper.wrapDegrees(entity.rotationYawHead - entity.renderYawOffset);
        if (!entity.getNavigator().noPath()) {
            if (f3 < -75.0f) entity.rotationYawHead = entity.renderYawOffset - 75.0f;
            if (f3 > 75.0f) entity.rotationYawHead = entity.renderYawOffset + 75.0f;
        }
    }
}
