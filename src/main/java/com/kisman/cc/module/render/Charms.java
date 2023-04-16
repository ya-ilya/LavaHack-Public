package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.Colour;

public class Charms extends Module {
    public final Setting polygonOffset = new Setting("PolygonOffset", this, true);
    public final Setting targetRender = new Setting("TargetRender", this, true);
    public final Setting customColor = new Setting("Use Color", this, false);
    public final Setting color = new Setting("Color", this, "Color", new Colour(255, 0, 0));

    public static Charms instance;

    public Charms() {
        super("Charms", "Charms", Category.RENDER);

        instance = this;

        setmgr.rSetting(polygonOffset);

        setmgr.rSetting(new Setting("Texture", this, false));
        setmgr.rSetting(targetRender);

        setmgr.rSetting(customColor);
        setmgr.rSetting(color);
    }
}
