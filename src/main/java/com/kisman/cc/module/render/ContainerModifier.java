package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.settings.Setting;

public class ContainerModifier extends Module {
    public final Setting containerShadow = new Setting("Container Shadow", this, false);
    public final Setting itemESP = new Setting("Item ESP", this, false);

    public static ContainerModifier instance;

    public ContainerModifier() {
        super("ContainerModifier", Category.RENDER);

        instance = this;

        setmgr.rSetting(containerShadow);
        setmgr.rSetting(itemESP);
    }
}
