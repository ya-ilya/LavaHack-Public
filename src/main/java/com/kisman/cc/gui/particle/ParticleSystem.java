package com.kisman.cc.gui.particle;

import com.kisman.cc.module.client.Config;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class ParticleSystem {
    private static final float SPEED = 0.2f;
    private final List<Particle> particleList;

    public ParticleSystem(final int initAmount) {
        this.particleList = new ArrayList<>();
        this.addParticles(initAmount);
    }

    public void addParticles(final int n) {
        for (int i = 0; i < n; ++i) this.particleList.add(Particle.generateParticle());
    }

    public static double distance(final float x, final float y, final float x1, final float y1) {
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
    }

    public void tick(final int delta) {
        for (Particle particle : this.particleList) particle.tick(delta, 0.1f);
    }


    private void drawLine(final float f, final float f2, final float f3, final float f4, Color color) {
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GL11.glLineWidth(0.5f);
        GL11.glBegin(1);
        GL11.glVertex2f(f, f2);
        GL11.glVertex2f(f3, f4);
        GL11.glEnd();
    }

    private void drawGradientLine(final float f, final float f2, final float f3, final float f4, Color startcolor,  Color endcolor, float width) {
        GL11.glPushMatrix();
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);

        GL11.glColor4f(startcolor.getRed() / 255.0f, startcolor.getGreen() / 255.0f, startcolor.getBlue() / 255.0f, startcolor.getAlpha() / 255.0f);
        GL11.glLineWidth(width);
        GL11.glBegin(1);

        GL11.glVertex2f(f, f2);

        GL11.glColor4f(endcolor.getRed() / 255.0f, endcolor.getGreen() / 255.0f, endcolor.getBlue() / 255.0f, endcolor.getAlpha() / 255.0f);

        GL11.glVertex2f(f3, f4);

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        GL11.glEnd();
        glPopMatrix();
    }


    private void drawThreeGradientLine(final float f, final float f2, final float f3, final float f4, Color startcolor,  Color midcolor, Color endcolor, float width) {
        GL11.glPushMatrix();
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);

        GL11.glColor4f(startcolor.getRed() / 255.0f, startcolor.getGreen() / 255.0f, startcolor.getBlue() / 255.0f, startcolor.getAlpha() / 255.0f);
        GL11.glLineWidth(width);
        GL11.glBegin(1);

        GL11.glVertex2f(f, f2);

        //

        GL11.glColor4f(midcolor.getRed() / 255.0f, midcolor.getGreen() / 255.0f, midcolor.getBlue() / 255.0f, midcolor.getAlpha() / 255.0f);

        float y;

        if (f2 >= f4) {
            y = f4 + ((f2-f4) / 2);
        } else {
            y = f2 + ((f4-f2) / 2);
        }

        float x;

        if (f >= f3) {
            x = f3 + ((f-f3) / 2);
        } else {
            x = f + ((f3-f) / 2);
        }

        GL11.glVertex2f(x, y);
        GL11.glEnd();
        //////////////////////////

        GL11.glBegin(1);

        GL11.glColor4f(midcolor.getRed() / 255.0f, midcolor.getGreen() / 255.0f, midcolor.getBlue() / 255.0f, midcolor.getAlpha() / 255.0f);

        GL11.glVertex2f(x, y);

        //

        GL11.glColor4f(endcolor.getRed() / 255.0f, endcolor.getGreen() / 255.0f, endcolor.getBlue() / 255.0f, endcolor.getAlpha() / 255.0f);

        GL11.glVertex2f(f3, f4);

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        GL11.glEnd();
        glPopMatrix();
    }

    public void render() {
        if (Minecraft.getMinecraft().currentScreen == null) return;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);

        for (Particle particle : this.particleList) {
            Color color = StaticParticles.getColor();
            if (Config.instance.particlesGradientMode.getValString().equals(Config.ParticlesGradientMode.Syns.name())) {
                particle.color.glColor();
            } else {
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, particle.getAlpha() / 255.0f);
            }
            GL11.glPointSize(particle.getSize());
            GL11.glBegin(0);
            GL11.glVertex2f(particle.getX(), particle.getY());
            GL11.glEnd();
            int Width = Mouse.getEventX() * Minecraft.getMinecraft().currentScreen.width / Minecraft.getMinecraft().displayWidth;
            int Height = Minecraft.getMinecraft().currentScreen.height - Mouse.getEventY() * Minecraft.getMinecraft().currentScreen.height / Minecraft.getMinecraft().displayHeight - 1;
            float nearestDistance = 0.0f;
            Particle nearestParticle = null;
            int dist = 100;
            for (Particle particle2 : this.particleList) {
                float distance = particle.getDistanceTo(particle2);
                if (distance <= dist && (distance((float)Width, (float)Height, particle.getX(), particle.getY()) <= dist || distance((float)Width, (float)Height, particle2.getX(), particle2.getY()) <= dist)) {
                    if (nearestDistance > 0.0f && distance > nearestDistance) continue;
                    nearestDistance = distance;
                    nearestParticle = particle2;
                }
            }
            if (nearestParticle == null) continue;
            float alpha = Math.min(1.0f, Math.min(1.0f, 1.0f - nearestDistance / dist));

            //Checks if two gradient particles mode is enabled
            if (StaticParticles.isIsTwoGParticlesEnabled()){
                //Checks if rendering gradient mode is default
                String mode = StaticParticles.getMode();
                Color startColor = StaticParticles.getStartColor();
                Color endColor = StaticParticles.getEndColor();
                float particleWidth = StaticParticles.getParticleWidth();

                if (mode.equals(StaticParticles.getModeDEfType())) {
                    this.drawGradientLine(particle.getX(), particle.getY(), nearestParticle.getX(), nearestParticle.getY(), startColor, endColor, particleWidth);
                    //this.drawGradientLineGlowing(particle.getX(), particle.getY(), nearestParticle.getX(), nearestParticle.getY(), StaticParticles.startColor, StaticParticles.endColor, StaticParticles.particleWidth);
                } else if (mode.equals(StaticParticles.getModeTGfType())) {
                    this.drawThreeGradientLine(particle.getX(), particle.getY(), nearestParticle.getX(), nearestParticle.getY(), startColor, Color.CYAN, endColor, particleWidth);
                } else {
                    this.drawGradientLine(particle.getX(), particle.getY(), nearestParticle.getX(), nearestParticle.getY(), particle.color.getColor(), nearestParticle.color.getColor(), particleWidth);
                }
            } else {
                this.drawLine(particle.getX(), particle.getY(), nearestParticle.getX(), nearestParticle.getY(), color);
            }
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
}

