package com.kisman.cc.util.optimization.aiimpr;

import com.kisman.cc.util.optimization.aiimpr.math.FastTrig;
import com.kisman.cc.util.optimization.aiimpr.math.FixedEntityLookHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.Iterator;

public class MainAiImpr {
    public static boolean ENABLED = false;
    public static boolean REMOVE_LOOK_AI = false;
    public static boolean REMOVE_LOOK_IDLE = false;
    public static boolean REPLACE_LOOK_HELPER = true;

    public void init() {
        FastTrig.init();
    }

    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if(!ENABLED) return;
        final Entity entity = event.getEntity();
        if (entity instanceof EntityLiving) {
            final EntityLiving living = (EntityLiving)entity;
            if (REMOVE_LOOK_AI || REMOVE_LOOK_IDLE) {
                final Iterator<EntityAITasks.EntityAITaskEntry> it = living.tasks.taskEntries.iterator();
                while (it.hasNext()) {
                    final EntityAITasks.EntityAITaskEntry obj = it.next();
                    if (obj != null) {
                        if(!(REMOVE_LOOK_AI && obj.action instanceof EntityAIWatchClosest)) if (!REMOVE_LOOK_IDLE || !(obj.action instanceof EntityAILookIdle)) continue;
                        it.remove();
                    }
                }
            }
            if (REPLACE_LOOK_HELPER && (living.getLookHelper() == null || living.getLookHelper().getClass() == EntityLookHelper.class)) {
                final EntityLookHelper oldHelper = living.lookHelper;
                living.lookHelper = new FixedEntityLookHelper(living);
                living.lookHelper.posX = oldHelper.posX;
                living.lookHelper.posY = oldHelper.posY;
                living.lookHelper.posZ = oldHelper.posZ;
                living.lookHelper.isLooking = oldHelper.isLooking;
                living.lookHelper.deltaLookPitch = oldHelper.deltaLookPitch;
                living.lookHelper.deltaLookYaw = oldHelper.deltaLookYaw;
            }
        }
    }
}
