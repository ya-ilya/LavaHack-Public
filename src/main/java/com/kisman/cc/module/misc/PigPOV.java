package com.kisman.cc.module.misc;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.module.misc.pigpov.NoRenderPig;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.passive.EntityPig;

public class PigPOV extends Module {
    public PigPOV() {
        super("PigPOV", Category.MISC);
    }

    public void onEnable() {
        if(mc.player == null || mc.world == null) return;
        mc.player.eyeHeight = 0.6f;
        mc.getRenderManager().entityRenderMap.put(EntityPig.class, new NoRenderPig(mc.getRenderManager(), mc));
    }

    public void onDisable() {
        if(mc.player == null || mc.world == null) return;
        mc.player.eyeHeight = mc.player.getDefaultEyeHeight();
        mc.getRenderManager().entityRenderMap.put(EntityPig.class, new RenderPig(mc.getRenderManager()));
    }
}
