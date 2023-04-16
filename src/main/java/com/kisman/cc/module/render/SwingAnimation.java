package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.glRotatef;

public class SwingAnimation extends Module {
    public static SwingAnimation instance;

    public final Setting mode = new Setting("Mode", this, "Strong", new ArrayList<>(Arrays.asList("Hand", "Strong")));


    private final Setting simpleLine = new Setting("SimpleLine", this, "Hand");

    private final Setting strongLine = new Setting("StrongLine", this, "Strong");
    public final Setting ignoreEating = new Setting("IgnoreEating", this, true);

    public final Setting strongMode = new Setting("StrongMode", this, StrongMode.Blockhit1);

    public final Setting ifKillAura = new Setting("If KillAura", this, true);

    private String swingModeString;

    public SwingAnimation() {
        super("SwingAnimation", "SwingAnimation", Category.RENDER);

        instance = this;

        settingManager.register(mode);

        settingManager.register(simpleLine);
        settingManager.register(new Setting("SwingMode", this, "1", new ArrayList<>(Arrays.asList("1", "2", "3"))));

        settingManager.register(strongLine);
        settingManager.register(strongMode);
        settingManager.register(ignoreEating);
        settingManager.register(ifKillAura);

        super.setDisplayInfo(() -> "[" + (mode.getValString().equalsIgnoreCase("Hand") ? settingManager.getSettingByName(this, "SwingMode").getValString() : strongMode.getValString()) + "]");
    }

    public void update() {
        this.swingModeString = settingManager.getSettingByName(this, "SwingMode").getValString();
    }

    @SubscribeEvent
    public void onRenderArms(final RenderSpecificHandEvent event) {
        if(mode.getValString().equalsIgnoreCase("Hand")) {
            if (event.getSwingProgress() > 0) {
                final float angle = (1f - event.getSwingProgress()) * 360f;

                switch (swingModeString) {
                    case "1":
                        glRotatef(angle, 1, 0, 0);
                        break;
                    case "2":
                        glRotatef(angle, 0, 1, 0);
                        break;
                    case "3":
                        glRotatef(angle, 0, 0, 1);
                        break;
                }
            }
        }
    }

    public enum StrongMode {
        Blockhit1,
        Blockhit2,
        Knife
    }
}
