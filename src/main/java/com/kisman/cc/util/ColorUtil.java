package com.kisman.cc.util;

import java.awt.*;

public class ColorUtil {
    public static float seconds = 2;
    public static float saturation = 1;
    public static float briqhtness = 1;

    public int r;
    public int g;
    public int b;
    public int a;

    public int color;

    public int getColor() {
        float hue = (System.currentTimeMillis() % (int)(seconds * 1000)) / (seconds * 1000);
        int color = Color.HSBtoRGB(hue, saturation, briqhtness);
        this.color = color;
        return color;
    }

    public static Color injectAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static Color injectAlpha(int color, int alpha) {
        return new Color(
                com.kisman.cc.util.gish.ColorUtil.getRed(color),
                com.kisman.cc.util.gish.ColorUtil.getGreen(color),
                com.kisman.cc.util.gish.ColorUtil.getBlue(color),
                alpha
        );
    }

    public int alpha(Color color, float alpha) {
        final float red = (float) color.getRed() / 255;
        final float green = (float) color.getGreen() / 255;
        final float blue = (float) color.getBlue() / 255;
        r = (int) red;
        g = (int) green;
        b = (int) blue;
        a = (int) alpha;
        return new Color(red, green, blue, alpha).getRGB();
    }

    public static float getSaturation() {
        return saturation;
    }

    public static void setSaturation(float saturation) {
        ColorUtil.saturation = saturation;
    }
}
