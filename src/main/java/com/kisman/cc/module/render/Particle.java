package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import net.minecraft.util.EnumParticleTypes;

import java.util.ArrayList;
import java.util.Arrays;

public class Particle extends Module {
    public Particle() {
        super("Particle", "Particle", Category.RENDER);

        settingManager.register(new Setting("RenderMode", this, "Single", new ArrayList<>(Arrays.asList("Single", "Multy"))));

        settingManager.register(new Setting("Particle", this, "Particle"));

        settingManager.register(new Setting("Heart", this, false));
        settingManager.register(new Setting("Crit", this, false));

        settingManager.register(new Setting("voidsetting", this, "void", "setting"));
    }

    public boolean isBeta() {return true;}

    public void update() {
        if(mc.player == null || mc.world == null) return;

        String renderMode = settingManager.getSettingByName(this, "RenderMode").getValString();

        boolean heart = settingManager.getSettingByName(this, "Heart").getValBoolean();
        boolean crit = settingManager.getSettingByName(this, "Crit").getValBoolean();

        if(renderMode.equalsIgnoreCase("Single") && heart) mc.world.spawnParticle(EnumParticleTypes.HEART, mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.motionX, mc.player.motionY, mc.player.motionZ, 1);
        if(renderMode.equalsIgnoreCase("Single") && crit) mc.world.spawnParticle(EnumParticleTypes.CRIT, mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.motionX, mc.player.motionY, mc.player.motionZ, 1);

        if(renderMode.equalsIgnoreCase("Multy")) {
            if(heart) {
                mc.world.spawnParticle(EnumParticleTypes.HEART, mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.motionX, mc.player.motionY, mc.player.motionZ, 1);
                mc.world.spawnParticle(EnumParticleTypes.HEART, mc.player.posX, mc.player.posY + 0.5, mc.player.posZ, mc.player.motionX, mc.player.motionY, mc.player.motionZ, 1);
                mc.world.spawnParticle(EnumParticleTypes.HEART, mc.player.posX, mc.player.posY + 0.5, mc.player.posZ, mc.player.motionX, mc.player.motionY, mc.player.motionZ, 1);
                mc.world.spawnParticle(EnumParticleTypes.HEART, mc.player.posX, mc.player.posY + 0.5, mc.player.posZ, mc.player.motionX, mc.player.motionY, mc.player.motionZ, 1);
            }
        }
    }
}
