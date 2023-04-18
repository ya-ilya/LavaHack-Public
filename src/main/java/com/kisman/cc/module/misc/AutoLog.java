package com.kisman.cc.module.misc;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;

public class AutoLog extends Module {
    private final Setting health = new Setting("Health", this, 10, 1, 36, true);

    public AutoLog() {
        super("AutoLog", "5", Category.MISC);

        register(health);
    }

    public void update() {
        if(mc.player == null || mc.world == null) return;

        int health = (int) this.health.getValDouble();

        if(mc.player.getHealth() < health) {
            mc.player.connection.handleDisconnect(new SPacketDisconnect(new TextComponentString("your health < " + health)));
            setToggled(false);
        }
    }
}
