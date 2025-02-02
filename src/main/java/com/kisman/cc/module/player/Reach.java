package com.kisman.cc.module.player;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;

public class Reach extends Module {
    public final Setting distance = new Setting("Distance", this, 5, 0, 10, true);

    public static Reach instance;

    public Reach() {
        super("Reach", Category.PLAYER);

        instance = this;

        register(distance);
    }
}
