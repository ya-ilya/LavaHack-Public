package com.kisman.cc.module.client;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.customfont.CustomFontUtilKt;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomFont extends Module {
    private final Setting antiAlias = new Setting("Anti Alias", this, true);
    private final Setting fractionMetrics = new Setting("Fraction Metrics", this, true);
    public final Setting mode = new Setting("Mode", this, "Comfortaa", new ArrayList<>(Arrays.asList("Verdana", "Comfortaa", "Comfortaa Light", "Comfortaa Bold", "Consolas", "LexendDeca", "Futura", "SfUi")));
    public final Setting bold = new Setting("Bold", this, false).setVisible(() -> mode.checkValString("Verdana"));
    public final Setting italic = new Setting("Italic", this, false).setVisible(() -> mode.checkValString("Verdana"));

    public static boolean turnOn = false;

    public static CustomFont instance;

    public CustomFont() {
        super("CustomFont", "custom font", Category.CLIENT);
        super.setDisplayInfo(() -> "[" + mode.getValString() + "]");

        instance = this;

        setmgr.rSetting(antiAlias);
        setmgr.rSetting(fractionMetrics);

        setmgr.rSetting(mode);
        setmgr.rSetting(bold);
        setmgr.rSetting(italic);
    }

    public void update() {
        turnOn = true;

        if(CustomFontUtilKt.Companion.getAntiAlias() != antiAlias.getValBoolean()) CustomFontUtilKt.Companion.setAntiAlias(antiAlias.getValBoolean());
        if(CustomFontUtilKt.Companion.getFractionMetrics() != fractionMetrics.getValBoolean()) {
            CustomFontUtilKt.Companion.setFractionalMetrics(fractionMetrics.getValBoolean());
            CustomFontUtilKt.Companion.setAntiAlias(antiAlias.getValBoolean());
        }
    }
    public void onDisable(){
        turnOn = false;
    }
}
