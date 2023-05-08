package com.kisman.cc.module.movement;

import com.kisman.cc.mixin.mixins.accessor.AccessorEntity;
import com.kisman.cc.mixin.mixins.accessor.AccessorEntityLivingBase;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;

public class NoJumpDelay extends Module {
    public NoJumpDelay() {
        super("NoJumpDelay", Category.MOVEMENT);
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;

        ((AccessorEntityLivingBase) mc.player).setJumpTicks(0);
        ((AccessorEntity) mc.player).setNextStepDistance(0);
    }
}
