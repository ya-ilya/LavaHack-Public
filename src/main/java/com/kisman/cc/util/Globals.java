package com.kisman.cc.util;

import com.kisman.cc.Kisman;
import com.kisman.cc.setting.SettingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Random;

public interface Globals {
    Minecraft mc = Minecraft.getMinecraft();
    Random random = new Random();
    ScaledResolution sr = new ScaledResolution(mc);
    SettingManager setmgr = Kisman.instance.settingManager;

    default boolean nullCheck(){
        return mc.player == null || mc.world == null;
    }
}