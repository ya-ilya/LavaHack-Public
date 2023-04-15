package com.kisman.cc.util;

import net.minecraft.client.Minecraft;

public class MoveUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float getSpeed() {
        return (float)Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
    }
}
