package com.kisman.cc.gui.particle;

import com.kisman.cc.module.client.Config;

import java.awt.*;

public class StaticParticles {
    public static Color getColor() {
        return Config.instance.particlesColor.getColour().getColor();
    }

    public static Color getStartColor() {
        return Config.instance.particlesGStartColor.getColour().getColor();
    }

    public static Color getEndColor() {
        return Config.instance.particlesGEndColor.getColour().getColor();
    }

    public static boolean isIsTwoGParticlesEnabled() {
        return !Config.instance.particlesGradientMode.getValString().equals(Config.ParticlesGradientMode.None.name());
    }

    public static float getParticleWidth() {
        return Config.instance.particlesWidth.getValFloat();
    }

    public static String getMode() {
        return Config.instance.particlesGradientMode.getValString();
    }

    public static String getModeDEfType() {
        return Config.ParticlesGradientMode.TwoGradient.name();
    }

    public static String getModeTGfType() {
        return Config.ParticlesGradientMode.ThreeGradient.name();
    }
}
