package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.Colour;
import net.minecraft.entity.item.EntityEnderCrystal;

public class CrystalModifier extends Module {
    public static CrystalModifier instance;

    public final Setting mode = new Setting("Mode", this, Modes.Fill);
    public final Setting preview = new Setting("Crystal", this, "Crystal", new EntityEnderCrystal(mc.world));

    private final Setting scaleLine = new Setting("ScaleLine", this, "Scale");

    public final Setting scale = new Setting("Scale", this,false);
    public final Setting scaleVal = new Setting("ScaleVal", this, 1, 0.1, 2, false);

    private final Setting translateLine = new Setting("TranslateLine", this, "Translate");

    public final Setting translateX = new Setting("TranslateX", this, 0, -2, 2, false);
    public final Setting translateY = new Setting("TranslateY", this, 0, -2, 2, false);
    public final Setting translateZ = new Setting("TranslateZ", this, 0, -2, 2, false);

    private final Setting crystalSettingLine = new Setting("CrystalSettingLine", this, "CrystalSetting");

    public final Setting insideCube = new Setting("InsideCube", this, true);
    public final Setting outsideCube = new Setting("OutsideCube", this, true);
    public final Setting outsideCube2 = new Setting("OutsideCube2", this, true);
    public final Setting texture = new Setting("Texture", this, false);
    public final Setting customColor = new Setting("CustomColor", this, false);
    public final Setting crystalColor = new Setting("CrystalColor", this, "Color", new Colour(0, 0, 255));

    private final Setting outlineLine = new Setting("OutLineLine", this, "OutLine");

    public final Setting outline = new Setting("Outline", this, false);
    public final Setting outlineMode = new Setting("OutlineMode", this, OutlineModes.Wire);
    public final Setting lineWidth = new Setting("LineWidth", this, 3, 0.5, 5, false);
    public final Setting color = new Setting("Outline Color", this, "Color", new Colour(255, 0, 0));


    private final Setting speedLine = new Setting("SpeedLine", this, "Speed");

    public final Setting speed = new Setting("CrystalSpeed", this, 3, 0, 50, false);
    public final Setting bounce = new Setting("CrystalBounce", this, 0.2f, 0, 10, false);

    public CrystalModifier() {
        super("CrystalCharms", "r", Category.RENDER);
        super.setDisplayInfo(() -> "[" + mode.getValString() + "]");

        instance = this;

        register(mode);

        register(scaleLine);
        register(scale);
        register(scaleVal);

        register(translateLine);
        register(translateX);
        register(translateY);
        register(translateZ);

        register(crystalSettingLine);
        register(insideCube);
        register(outsideCube);
        register(outsideCube2);
        register(texture);
        register(customColor);
        register(crystalColor);

        register(outlineLine);
        register(outline);
        register(outlineMode);
        register(lineWidth);
        register(color);

        register(speedLine);
        register(speed);
        register(bounce);
    }

    public enum OutlineModes {Wire, Flat}
    public enum Modes {Fill, Wireframe,}
}
