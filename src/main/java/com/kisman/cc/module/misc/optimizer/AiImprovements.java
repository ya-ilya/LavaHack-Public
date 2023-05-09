package com.kisman.cc.module.misc.optimizer;

import com.kisman.cc.mixin.mixins.accessor.AccessorEntityLiving;
import com.kisman.cc.mixin.mixins.accessor.AccessorEntityLookHelper;
import com.kisman.cc.module.misc.Optimizer;
import com.kisman.cc.module.misc.optimizer.helpers.FixedEntityLookHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;

public class AiImprovements {
    private final Optimizer optimizer;

    public AiImprovements(Optimizer optimizer) {
        this.optimizer = optimizer;

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!optimizer.isToggled()) return;

        if (event.getEntity() instanceof EntityLiving) {
            EntityLiving entity = (EntityLiving) event.getEntity();

            boolean removeLookAi = optimizer.removeLookAi.getValBoolean();
            boolean removeLookIdle = optimizer.removeLookIdle.getValBoolean();
            boolean replaceLookHelper = optimizer.replaceLookHelper.getValBoolean();

            if (removeLookAi || removeLookIdle) {
                Iterator<EntityAITasks.EntityAITaskEntry> it = entity.tasks.taskEntries.iterator();
                while (it.hasNext()) {
                    EntityAITasks.EntityAITaskEntry obj = it.next();
                    if (obj != null) {
                        if (!(removeLookAi && obj.action instanceof EntityAIWatchClosest)) {
                            if (!removeLookIdle || !(obj.action instanceof EntityAILookIdle)) continue;
                        }
                        it.remove();
                    }
                }
            }

            if (replaceLookHelper) {
                AccessorEntityLookHelper oldHelper = (AccessorEntityLookHelper) ((AccessorEntityLiving) entity).getLookHelper();
                EntityLookHelper newHelper = new FixedEntityLookHelper(entity);
                newHelper.setLookPosition(oldHelper.getPosX(), oldHelper.getPosY(), oldHelper.getPosZ(), oldHelper.getDeltaLookYaw(), oldHelper.getDeltaLookPitch());
                ((AccessorEntityLiving) entity).setLookHelper(newHelper);
            }
        }
    }
}
