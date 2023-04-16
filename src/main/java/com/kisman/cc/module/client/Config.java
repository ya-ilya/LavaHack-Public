package com.kisman.cc.module.client;

import com.kisman.cc.Kisman;
import com.kisman.cc.config.ConfigLoader;
import com.kisman.cc.gui.csgo.components.Slider;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.Colour;
import i.gishreloaded.gishcode.utils.visual.ChatUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class Config extends Module {
    public static Config instance;

    public final Setting astolfoColorMode = new Setting("Astolfo Color Mode", this, AstolfoColorMode.Old);
    public final Setting friends = new Setting("Friends", this, true);
    public final Setting nameMode = new Setting("Name Mode", this, NameMode.kismancc);
    public final Setting customName = new Setting("Custom Name", this, "kisman.cc", "kisman.cc", true).setVisible(() -> nameMode.getValBoolean());
    public final Setting scrollSpeed = new Setting("Scroll Speed", this, 15, 0, 100, Slider.NumberType.PERCENT);
    public final Setting horizontalScroll = new Setting("Horizontal Scroll", this, false);
    public final Setting keyForHorizontalScroll = new Setting("Key for Horizontal Scroll", this, Keyboard.KEY_LSHIFT).setVisible(() -> horizontalScroll.getValBoolean());
    public final Setting guiGlow = new Setting("Gui Glow", this, false);
    public final Setting glowOffset = new Setting("Glow Offset", this, 6, 1, 20, true).setVisible(() -> guiGlow.getValBoolean());
    public final Setting glowRadius = new Setting("Glow Radius", this, 15, 0, 20, true).setVisible(() -> guiGlow.getValBoolean());
    public final Setting glowBoxSize = new Setting("Glow Box Size", this, 0, 0, 20, true).setVisible(() -> guiGlow.getValBoolean());
    public final Setting guiGradient = new Setting("Gui Gradient", this, HUD.Gradient.None);
    public final Setting guiGradientDiff = new Setting("Gui Gradient Diff", this, 1, 0, 1000, Slider.NumberType.TIME);
    public final Setting guiDesc = new Setting("Gui Desc", this, false);
    public final Setting guiParticles = new Setting("Gui Particles", this, true);
    public final Setting guiOutline = new Setting("Gui Outline", this, true);
    public final Setting guiAstolfo = new Setting("Gui Astolfo", this, false);
    public final Setting guiRenderSize = new Setting("Gui Render Size", this, false);
    public final Setting guiBetterCheckBox = new Setting("Gui Better CheckBox", this, false);
    public final Setting guiBlur = new Setting("Gui Blur", this, true);
    public final Setting guiVisualPreview = new Setting("Gui Visual Preview", this, false);
    public final Setting guiShowBinds = new Setting("Gui Show Binds", this, false);
    public final Setting pulseMin = new Setting("Pulse Min", this, 255, 0, 255, true);
    public final Setting pulseMax = new Setting("Pulse Max", this, 110, 0, 255, true);
    public final Setting pulseSpeed = new Setting("Pulse Speed", this, 1.5, 0.1, 10, false);
    public final Setting saveConfig = new Setting("Save Config", this, false);
    public final Setting loadConfig = new Setting("Load Config", this, false);
    public final Setting configurate = new Setting("Configurate", this, true);
    public final Setting particlesColor = new Setting("Particles Color", this, "Particles Dots Color", new Colour(0, 0, 255)).setVisible(() -> guiParticles.getValBoolean());

    public final Setting particlesRenderLine = new Setting("Particles Render Lines", this, true);

    public final Setting particlesGradientMode = new Setting("Particles Gradient Mode", this, ParticlesGradientMode.None).setVisible(() -> guiParticles.getValBoolean() && particlesRenderLine.getValBoolean());

    public final Setting particlesGStartColor = new Setting("Particles Gradient StartColor", this, "Particles Gradient StartColor", new Colour(0, 0, 255)).setVisible(() -> guiParticles.getValBoolean() && !particlesGradientMode.getValString().equalsIgnoreCase(ParticlesGradientMode.None.name()) && particlesRenderLine.getValBoolean());
    public final Setting particlesGEndColor = new Setting("Particles Gradient EndColor", this, "Particles Gradient EndColor", new Colour(0, 0, 255)).setVisible(() -> guiParticles.getValBoolean() && !particlesGradientMode.getValString().equalsIgnoreCase(ParticlesGradientMode.None.name()) && particlesRenderLine.getValBoolean());

    public final Setting particlesWidth = new Setting("Particles Width", this, 0.5, 0.0, 5, false).setVisible(() -> guiParticles.getValBoolean() && particlesRenderLine.getValBoolean());

    public final Setting particleTest = new Setting("Particle Test", this, true).setVisible(() -> guiParticles.getValBoolean() && particlesRenderLine.getValBoolean());

    public final Setting slowRender = new Setting("Slow Render", this, false);
    public final Setting depthFix = new Setting("Depth Fix", this, true);
    public final Setting antiOpenGLCrash = new Setting("Anti OpenGL Crash", this, false);

    public Config() {
        super("Config", Category.CLIENT, false);

        instance = this;

        register(friends);
        register(nameMode);
        register(customName);
        register(scrollSpeed);
        register(horizontalScroll);
        register(keyForHorizontalScroll);
        register(guiGlow);
        register(glowOffset);
        register(glowRadius);
        register(glowBoxSize);
        register(guiGradient);
        register(guiGradientDiff);
        register(guiDesc);
        register(guiParticles);
        register(guiOutline);
        register(guiAstolfo);
        register(guiRenderSize);
        register(guiBetterCheckBox);
        register(guiBlur);
        register(guiVisualPreview);
        register(guiShowBinds);
        register(pulseMin);
        register(pulseMax);
        register(pulseSpeed);
        register(saveConfig);
        register(loadConfig);
        register(configurate);
        register(particlesColor);
        register(particlesGradientMode);
        register(particlesGStartColor);
        register(particlesGEndColor);
        register(particlesWidth);
        register(particleTest);
        register(slowRender);
        register(depthFix);
        register(antiOpenGLCrash);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(saveConfig.getValBoolean()) {
            try {
                Kisman.instance.configManager.getSaver().init();
            } catch (IOException e) {
                e.printStackTrace();
            }
            saveConfig.setValBoolean(false);
            if(mc.player != null && mc.world != null) ChatUtils.complete("Config saved");
        }

        if(loadConfig.getValBoolean()) {
            ConfigLoader.init();
            try {
                Kisman.instance.configManager.getLoader().init();
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadConfig.setValBoolean(false);
            if(mc.player != null && mc.world != null) ChatUtils.complete("Config loaded");
        }
    }

    public enum NameMode {kismancc, LavaHack, TheKisDevs, kidman, TheClient, BloomWare, custom}
    public enum ParticlesGradientMode {None, TwoGradient, ThreeGradient, Syns}
    public enum AstolfoColorMode {Old, Impr}
}
