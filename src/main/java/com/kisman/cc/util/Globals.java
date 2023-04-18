package com.kisman.cc.util;

import net.minecraft.client.Minecraft;

import java.util.Random;

public interface Globals {
    Minecraft mc = Minecraft.getMinecraft();
    Random random = new Random();
}