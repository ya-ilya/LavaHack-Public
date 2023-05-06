package com.kisman.cc.module.client;

import com.kisman.cc.Kisman;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import net.minecraft.util.ResourceLocation;

public class VegaGui extends Module {
    public static VegaGui instance;

    public final Setting test = new Setting("Test Gui Update", this, false);

    public VegaGui() {
        super("VegaGui", "gui", Category.CLIENT);

        instance = this;

        register(test);
    }

    public boolean isBeta() {return true;}

    public void onEnable() {
        mc.displayGuiScreen(Kisman.instance.gui);
        this.setToggled(false);

        if (Config.instance.guiBlur.getValBoolean()) mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
    }
}
