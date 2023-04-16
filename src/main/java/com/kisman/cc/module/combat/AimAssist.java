package com.kisman.cc.module.combat;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.AnimationUtils;
import com.kisman.cc.util.EntityUtil;
import com.kisman.cc.util.RotationUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;

public class AimAssist extends Module {
    private final Setting range = new Setting("Range", this, 4.5f, 1, 6, false);
    private final Setting speed = new Setting("Speed", this, 0.5f, 0.1f, 2, false);

    public AimAssist() {
        super("AimAssist", Category.COMBAT);

        settingManager.register(range);
        settingManager.register(speed);
    }

    public void update() {
        if(mc.player == null || mc.world == null) return;
        EntityPlayer target = EntityUtil.getTarget(range.getValFloat());

        if(target == null) return;
        else super.setDisplayInfo("[" + target.getName() + TextFormatting.GRAY + "]");

        if(mc.objectMouseOver.entityHit != null && mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY) && mc.objectMouseOver.entityHit != target) {
            float[] rotsToTarget = RotationUtils.getRotation(target);

            mc.player.rotationYaw = (float) AnimationUtils.animate(rotsToTarget[0], mc.player.rotationYaw, speed.getValFloat());
            mc.player.rotationPitch = (float) AnimationUtils.animate(rotsToTarget[1], mc.player.rotationPitch, speed.getValFloat());
        }
    }

    public boolean isVisible() {return false;}
}
