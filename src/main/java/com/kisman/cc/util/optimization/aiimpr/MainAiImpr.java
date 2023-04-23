package com.kisman.cc.util.optimization.aiimpr;

import com.kisman.cc.module.misc.Optimizer;
import com.kisman.cc.util.optimization.aiimpr.math.FastTrig;
import com.kisman.cc.util.optimization.aiimpr.math.FixedEntityLookHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;

public class MainAiImpr {
    public static boolean ENABLED = false;

    public void init() {
        FastTrig.init();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if(Optimizer.instance == null || !Optimizer.instance.isToggled()) return;
        final Entity entity = event.getEntity();
        if (entity instanceof EntityLiving) {
            final EntityLiving living = (EntityLiving)entity;

            boolean removeLookAi = Optimizer.instance.removeLookAi.getValBoolean();
            boolean removeLookIdle = Optimizer.instance.removeLookIdle.getValBoolean();
            boolean replaceLookHelper = Optimizer.instance.replaceLookHelper.getValBoolean();

            if (removeLookAi || removeLookIdle) {
                final Iterator<EntityAITasks.EntityAITaskEntry> it = living.tasks.taskEntries.iterator();
                while (it.hasNext()) {
                    final EntityAITasks.EntityAITaskEntry obj = it.next();
                    if (obj != null) {
                        if(!(removeLookAi && obj.action instanceof EntityAIWatchClosest)) if (!removeLookIdle || !(obj.action instanceof EntityAILookIdle)) continue;
                        it.remove();
                    }
                }
            }
            if (replaceLookHelper && (living.getLookHelper() == null || living.getLookHelper().getClass() == EntityLookHelper.class)) {
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
