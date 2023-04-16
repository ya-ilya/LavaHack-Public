package com.kisman.cc.module.movement;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import net.minecraft.init.Blocks;

public class IceSpeed extends Module {
    public IceSpeed() {
        super("IceSpeed", "IceSpeed", Category.MOVEMENT);

        setmgr.rSetting(new Setting("Speed", this, 0.4f, 0.2f, 1.5f, false));
    }

    public void update() {
        double speed = setmgr.getSettingByName(this, "Speed").getValDouble();
        Blocks.ICE.slipperiness = (float) speed;
        Blocks.PACKED_ICE.slipperiness = (float) speed;
        Blocks.FROSTED_ICE.slipperiness = (float) speed;
    }

    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }
}
