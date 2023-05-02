package com.kisman.cc.module.movement;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import net.minecraft.init.Blocks;

public class IceSpeed extends Module {
    private final float DEFAULT_SLIPPERINESS = 0.98f;

    private final Setting speed = new Setting("Speed", this, 0.4f, 0.2f, 1.5f, false);

    public IceSpeed() {
        super("IceSpeed", "IceSpeed", Category.MOVEMENT);

        register(speed);
    }

    public void update() {
        double speed = this.speed.getValDouble();
        Blocks.ICE.setDefaultSlipperiness((float) speed);
        Blocks.PACKED_ICE.setDefaultSlipperiness((float) speed);
        Blocks.FROSTED_ICE.setDefaultSlipperiness((float) speed);
    }

    public void onDisable() {
        Blocks.ICE.setDefaultSlipperiness(DEFAULT_SLIPPERINESS);
        Blocks.PACKED_ICE.setDefaultSlipperiness(DEFAULT_SLIPPERINESS);
        Blocks.FROSTED_ICE.setDefaultSlipperiness(DEFAULT_SLIPPERINESS);
    }
}
