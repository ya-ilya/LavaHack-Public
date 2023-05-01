package com.kisman.cc.module.misc;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;

public class Optimizer extends Module {
    public final Setting removeLookAi = new Setting("Remove Entity AI Watch Closest", this, false);
    public final Setting removeLookIdle = new Setting("Remove Entity AI LookIdle", this, false);
    public final Setting replaceLookHelper = new Setting("Replace Look Helper", this, true);
    public final Setting tileEntityRenderRange = new Setting("TileEntity Render Range(Squared)", this, 4096, 0, 4096, true);
    public final Setting customEntityRenderRange = new Setting("Custom Entity Render Range", this, false);
    public final Setting entityRenderRange = new Setting("Entity Render Range", this, 50, 0, 50, true).setVisible(customEntityRenderRange::getValBoolean);

    public static Optimizer instance;

    public Optimizer() {
        super("Optimizer", Category.MISC);

        instance = this;

        register(removeLookAi);
        register(removeLookIdle);
        register(replaceLookHelper);

        register(tileEntityRenderRange);

        register(customEntityRenderRange);
        register(entityRenderRange);
    }
}
