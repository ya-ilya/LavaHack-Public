package com.kisman.cc.module.player;

import com.kisman.cc.mixin.mixins.accessor.AccessorItemRenderer;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;

public class Swing extends Module {
    private final Setting mode = new Setting("Mode", this, Hand.MAINHAND);

    public Swing() {
        super("Swing", Category.PLAYER);

        register(mode);
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;

        AccessorItemRenderer accessorItemRenderer = (AccessorItemRenderer) mc.entityRenderer.itemRenderer;
        if (mode.getValString().equals(Hand.MAINHAND.name())) {
            mc.player.swingingHand = EnumHand.MAIN_HAND;
        } else if (mode.getValString().equals(Hand.OFFHAND.name())) {
            mc.player.swingingHand = EnumHand.OFF_HAND;
        } else if (mode.getValString().equals(Hand.PACKETSWING.name()) && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && accessorItemRenderer.getPrevEquippedProgressMainHand() >= 0.9) {
            accessorItemRenderer.setEquippedProgressMainHand(1f);
            accessorItemRenderer.setItemStackMainHand(mc.player.getHeldItemMainhand());
        }
    }

    public enum Hand {
        OFFHAND,
        MAINHAND,
        PACKETSWING
    }
}
