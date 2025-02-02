package com.kisman.cc.module.client;

import com.kisman.cc.RPC;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;

public class DiscordRPC extends Module {
    public static DiscordRPC instance;

    public final Setting impr = new Setting("Impr RPC", this, true);

    public DiscordRPC() {
        super("DiscordRPC", Category.CLIENT);
        instance = this;

        register(impr);
    }

    public void onEnable() {
        if (System.getProperty("os.version").toLowerCase().contains("android")) { return; }
        RPC.startRPC();
    }
    public void onDisable() {
        RPC.stopRPC();
    }
}
