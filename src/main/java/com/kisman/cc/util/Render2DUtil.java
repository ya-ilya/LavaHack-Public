package com.kisman.cc.util;

import com.kisman.cc.module.client.Config;
import com.kisman.cc.util.gish.ColorUtil;
import com.kisman.cc.util.glow.ShaderShell;
import com.kisman.cc.util.render.objects.AbstractGradient;
import com.kisman.cc.util.render.objects.AbstractObject;
import com.kisman.cc.util.render.objects.ObjectWithGlow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author _kisman_
 * Credits: github.com/TheKisDevs/loader & NoneCode Free
 */
public class Render2DUtil extends GuiScreen {
    public static Render2DUtil instance = new Render2DUtil();

    public static void drawRect(int mode, int left, int top, int right, int bottom, int color) {
        if (left < right) {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        vertexbuffer.begin(mode, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(left, bottom, 0.0D).endVertex();
        vertexbuffer.pos(right, bottom, 0.0D).endVertex();
        vertexbuffer.pos(right, top, 0.0D).endVertex();
        vertexbuffer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawProgressCircle2(double x, double y, double radius, int color, double degrees, double modificator) {
        float f = (float)(color >> 24 & 255) / 255.0F;
        float f1 = (float)(color >> 16 & 255) / 255.0F;
        float f2 = (float)(color >> 8 & 255) / 255.0F;
        float f3 = (float)(color & 255) / 255.0F;
        boolean flag = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean flag1 = GL11.glIsEnabled(GL11.GL_LINE_SMOOTH);
        boolean flag2 = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        if (!flag) GL11.glEnable(GL11.GL_BLEND);
        if (!flag1) GL11.glEnable(GL11.GL_LINE_SMOOTH);
        if (flag2) GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glLineWidth(2.5F);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (double i = 0; i <= degrees; i += modificator) GL11.glVertex2d(x + Math.sin(i * Math.PI / 180.0D) * radius, y + Math.cos(i * Math.PI / 180.0D) * radius);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        if (flag2) GL11.glEnable(GL11.GL_TEXTURE_2D);
        if (!flag1) GL11.glDisable(GL11.GL_LINE_SMOOTH);
        if (!flag) GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawModalRectWithCustomSizedTexture(double x, double y, float u, float v, double width, double height, double textureWidth, double textureHeight) {
        float f = 1.0F / (float) textureWidth;
        float f1 = 1.0F / (float) textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, (y + height), 0.0D).tex((u * f), ((v + (float)height) * f1)).endVertex();
        bufferbuilder.pos((x + width), (y + height), 0.0D).tex(((u + (float)width) * f), ((v + (float)height) * f1)).endVertex();
        bufferbuilder.pos((x + width), y, 0.0D).tex(((u + (float)width) * f), (v * f1)).endVertex();
        bufferbuilder.pos(x, y, 0.0D).tex((u * f), (v * f1)).endVertex();
        tessellator.draw();
    }

    public static void drawAbstract(AbstractObject drawing) {
        if (drawing != null) drawing.render();
    }

    public static void drawAbstract(ObjectWithGlow drawing) {
        if (drawing != null) drawing.render();
    }

    public static void drawAbstract(AbstractGradient drawing) {
        if (drawing != null) drawing.render();
    }

    public static void drawRectWH(double x, double y, double width, double height, int color) {drawRect(x, y, x + width, y + height, color);}

    public static void drawProgressCircle(double cx, double cy, double radius, Color color, float width, double degrees, int segments) {
        ColorUtil.glColor(color);
        glLineWidth(width);
        glBegin(GL_LINE);
        for (int i = 0; i < segments; i++) {
            if (i > degrees) break;
            float theta = (float) (2f * Math.PI * i / segments);

            float x = (float) (radius * Math.cos(theta));
            float y = (float) (radius * Math.sin(theta));

            glVertex2d(x + cx, y + cy);
        }
        glEnd();
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        double j;
        if (left < right) {
            j = left;
            left = right;
            right = j;
        }

        if (top < bottom) {
            j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, top, 0.0D).endVertex();
        bufferbuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawPolygonPart(final double x, final double y, final int radius, final int part, final int color, final int endcolor) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final float alpha2 = (endcolor >> 24 & 0xFF) / 255.0f;
        final float red2 = (endcolor >> 16 & 0xFF) / 255.0f;
        final float green2 = (endcolor >> 8 & 0xFF) / 255.0f;
        final float blue2 = (endcolor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, 0.0).color(red, green, blue, alpha).endVertex();
        for (int i = part * 90; i <= part * 90 + 90; ++i) {
            final double angle = 6.283185307179586 * i / 360.0 + Math.toRadians(180.0);
            bufferbuilder.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0.0).color(red2, green2, blue2, alpha2).endVertex();
        }
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawVGradientRect(final float left, final float top, final float right, final float bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(left, top, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(left, bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawGlow(final double x, final double y, final double x1, final double y1, final int color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        drawVGradientRect((float)(int)x, (float)(int)y, (float)(int)x1, (float)(int)(y + (y1 - y) / 2.0), com.kisman.cc.util.ColorUtil.injectAlpha(new Color(color), 0).getRGB(), color);
        drawVGradientRect((float)(int)x, (float)(int)(y + (y1 - y) / 2.0), (float)(int)x1, (float)(int)y1, color, com.kisman.cc.util.ColorUtil.injectAlpha(new Color(color), 0).getRGB());
        final int radius = (int)((y1 - y) / 2.0);
        drawPolygonPart(x, y + (y1 - y) / 2.0, radius, 0, color, com.kisman.cc.util.ColorUtil.injectAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x, y + (y1 - y) / 2.0, radius, 1, color, com.kisman.cc.util.ColorUtil.injectAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x1, y + (y1 - y) / 2.0, radius, 2, color, com.kisman.cc.util.ColorUtil.injectAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x1, y + (y1 - y) / 2.0, radius, 3, color, com.kisman.cc.util.ColorUtil.injectAlpha(new Color(color), 0).getRGB());
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void gradient(int minX, int minY, int maxX, int maxY, int startColor, int endColor, boolean left) {
        if (left) {
            final float startA = (startColor >> 24 & 0xFF) / 255.0f;
            final float startR = (startColor >> 16 & 0xFF) / 255.0f;
            final float startG= (startColor >> 8 & 0xFF) / 255.0f;
            final float startB = (startColor & 0xFF) / 255.0f;

            final float endA = (endColor >> 24 & 0xFF) / 255.0f;
            final float endR = (endColor >> 16 & 0xFF) / 255.0f;
            final float endG = (endColor >> 8 & 0xFF) / 255.0f;
            final float endB = (endColor & 0xFF) / 255.0f;

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL_SMOOTH);
            GL11.glBegin(GL11.GL_POLYGON);
            {
                GL11.glColor4f(startR, startG, startB, startA);
                GL11.glVertex2f(minX, minY);
                GL11.glVertex2f(minX, maxY);
                GL11.glColor4f(endR, endG, endB, endA);
                GL11.glVertex2f(maxX, maxY);
                GL11.glVertex2f(maxX, minY);
            }
            GL11.glEnd();
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        } else instance.drawGradientRect(minX, minY, maxX, maxY, startColor, endColor);
    }

    public static void drawLeftGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL_SMOOTH);
        builder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(right, top, 0).color((float) (endColor >> 24 & 255) / 255.0F, (float) (endColor >> 16 & 255) / 255.0F, (float) (endColor >> 8 & 255) / 255.0F, (float) (endColor >> 24 & 255) / 255.0F).endVertex();
        builder.pos(left, top, 0).color((float) (startColor >> 16 & 255) / 255.0F, (float) (startColor >> 8 & 255) / 255.0F, (float) (startColor & 255) / 255.0F, (float) (startColor >> 24 & 255) / 255.0F).endVertex();
        builder.pos(left, bottom, 0).color((float) (startColor >> 16 & 255) / 255.0F, (float) (startColor >> 8 & 255) / 255.0F, (float) (startColor & 255) / 255.0F, (float) (startColor >> 24 & 255) / 255.0F).endVertex();
        builder.pos(right, bottom, 0).color((float) (endColor >> 24 & 255) / 255.0F, (float) (endColor >> 16 & 255) / 255.0F, (float) (endColor >> 8 & 255) / 255.0F, (float) (endColor >> 24 & 255) / 255.0F).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawRoundedRect(double startX, double startY, double endX, double endY, Color color, double radius) {
        drawRoundedRect((float) startX - radius, (float) startY - radius, (float) endX + radius, (float) endY + radius, color.getRGB(), Config.instance.glowRadius.getValFloat());
    }

    public static void drawRoundedRect(double startX, double startY, double endX, double endY, int color, float radius) {
        drawRoundedRect((float) startX, (float) startY, (float) endX, (float) endY, color, radius);
    }

    public static void drawRoundedRect(float startX, float startY, float endX, float endY, int color, float radius) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        float alpha = ((float) (color >> 24 & 0xFF) / 255F);
        float red = (float) (color >> 16 & 0xFF) / 255F;
        float green = (float) (color >> 8 & 0xFF) / 255F;
        float blue = (float) (color & 0xFF) / 255F;
        ShaderShell.ROUNDED_RECT.attach();
        ShaderShell.ROUNDED_RECT.set4F("color", red, green, blue, alpha);
        ShaderShell.ROUNDED_RECT.set2F("resolution", Minecraft.getMinecraft().displayWidth,
                Minecraft.getMinecraft().displayHeight);
        ShaderShell.ROUNDED_RECT.set2F("center", (startX + (endX - startX) / 2) * 2,
                (startY + (endY - startY) / 2) * 2);
        ShaderShell.ROUNDED_RECT.set2F("dst", (endX - startX - radius) * 2, (endY - startY - radius) * 2);
        ShaderShell.ROUNDED_RECT.set1F("radius", radius);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(endX, startY);
        GL11.glVertex2d(startX, startY);
        GL11.glVertex2d(startX, endY);
        GL11.glVertex2d(endX, endY);
        GL11.glEnd();
        ShaderShell.ROUNDED_RECT.detach();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void drawRoundedRect(float startX, float startY, float endX, float endY, int color) {
        drawRoundedRect(startX, startY, endX, endY, color, Config.instance.glowRadius.getValFloat());
    }

    public static void drawRoundedRect2(double x, double y, double width, double height, double radius, int color) {
        double x1 = x + width;
        double y1 = y + height;
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        GlStateManager.enableBlend();
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBegin(9);
        int i;
        for (i = 0; i <= 90; i += 3) GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 3) GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 3) GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 3) GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GlStateManager.disableBlend();
        GL11.glScaled(2.0D, 2.0D, 2.0D);
        GL11.glPopAttrib();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawRoundedRect(double startX, double startY, double endX, double endY, Color color) {
        drawRoundedRect((float) startX, (float) startY, (float) endX, (float) endY, color.getRGB(), Config.instance.glowRadius.getValFloat());
    }

    public static void drawShadowSliders(double x, double y, double sliderWidth, double sliderHeight, int color, double lineWidth) {
        //slider base
        drawGradientRect(x, y, x + sliderWidth, y + sliderHeight, color, Color.BLACK.getRGB());

        //outline
        drawOutlineRect(x, y, x + sliderWidth, y + sliderHeight, lineWidth, color);
    }

    public static void drawOutlineRect(double x1, double y1, double x2, double y2, double lineWidth, int color) {
        drawRect(x1, y1, x2, y1 + lineWidth, color);
        drawRect(x1, y2 - lineWidth, x2, y2, color);
        drawRect(x1, y1, x1 + lineWidth, y2, color);
        drawRect(x2 - lineWidth, y1, x2, y2, color);
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, instance.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, top, instance.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, instance.zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, bottom, instance.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static double[] getDeltas(Float ticks, EntityPlayer player) {
        return new double[] {
                MathHelper.clampedLerp(player.lastTickPosX, player.posX, ticks.doubleValue()),
                MathHelper.clampedLerp(player.lastTickPosY, player.posY, ticks.doubleValue()),
                MathHelper.clampedLerp(player.lastTickPosZ, player.posZ, ticks.doubleValue())
        };
    }
}
