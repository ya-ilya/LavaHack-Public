package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import i.gishreloaded.gishcode.utils.visual.ColorUtils;

import java.awt.*;

public class HotbarModifier extends Module {
    public final Setting containerShadow = new Setting("Shadow", this, false);
    public final Setting primaryAstolfo = new Setting("Primary Astolfo", this, true).setVisible(containerShadow::getValBoolean);
    public final Setting offhand = new Setting("Offhand", this, true).setVisible(containerShadow::getValBoolean);
    public final Setting offhandGradient = new Setting("Offhand Gradient", this, false).setVisible(() -> offhand.getValBoolean() && containerShadow.getValBoolean());

    public static HotbarModifier instance;

    public HotbarModifier() {
        super("HotbarModifier", Category.RENDER);

        instance = this;

        settingManager.register(containerShadow);
        settingManager.register(primaryAstolfo);
        settingManager.register(offhand);
        settingManager.register(offhandGradient);
    }

    public static Color getPrimaryColor() {
        return instance.primaryAstolfo.getValBoolean() ? ColorUtils.astolfoColorsToColorObj(100, 100) : new Color(255, 255, 255, 152);
    }
}
