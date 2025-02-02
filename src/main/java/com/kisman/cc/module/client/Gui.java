package com.kisman.cc.module.client;

import com.kisman.cc.Kisman;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.Colour;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.input.Keyboard.KEY_RSHIFT;

public class Gui extends Module {
    public final Setting primaryColor = new Setting("Primary Color", this, "Primary Color", new Colour(255, 0, 0));
    public final Setting background = new Setting("Background", this, true);
    public final Setting shadow = new Setting("Shadow", this, false);
    public final Setting test = new Setting("test", this, false);
    public final Setting shadowRects = new Setting("Shadow Rects", this, false);
    public final Setting line = new Setting("Line", this, true);

    public static Gui instance;

    public Gui() {
        super("Gui", Category.CLIENT);
        super.setKey(KEY_RSHIFT);

        instance = this;

        register(primaryColor);
        register(background);
        register(shadow);
        register(test);
        register(shadowRects);
        register(line);
    }

    public void onEnable() {
        mc.displayGuiScreen(Kisman.instance.halqGui);
        super.setToggled(false);
        if (Config.instance.guiBlur.getValBoolean()) {
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }
}
