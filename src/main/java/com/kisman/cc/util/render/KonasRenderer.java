package com.kisman.cc.util.render;

import com.kisman.cc.mixin.mixins.accessor.AccessorRenderManager;
import com.kisman.cc.util.Colour;
import com.kisman.cc.util.Globals;
import com.kisman.cc.util.render.konas.BlockRenderUtil;
import com.kisman.cc.util.render.konas.FaceMasks;
import com.kisman.cc.util.render.konas.TessellatorUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;

public class KonasRenderer implements Globals {
    public static void drawHole(
            AxisAlignedBB axisAlignedBB,
            String mode, String lines,
            double height,
            Colour color,
            Colour lineColor,
            boolean notSelf,
            int fadeAlpha,
            boolean sides,
            boolean noLineDepth,
            boolean depth,
            float width
    ) {
        AxisAlignedBB tBB = axisAlignedBB;
        axisAlignedBB = axisAlignedBB.setMaxY(axisAlignedBB.minY + height);
        int sidesByte = sides ? FaceMasks.Quad.NORTH | FaceMasks.Quad.SOUTH | FaceMasks.Quad.WEST | FaceMasks.Quad.EAST : FaceMasks.Quad.ALL;

        if (mode.equalsIgnoreCase("FULL")) {
            drawFullHole(axisAlignedBB, color, lineColor, width, sidesByte);
        } else if (mode.equalsIgnoreCase("OUTLINE")) {
            drawOutlineHole(axisAlignedBB, lineColor, width);
        } else if (mode.equalsIgnoreCase("WIREFRAME")) {
            drawWireframeHole(axisAlignedBB, lineColor, width);
        } else if (mode.equalsIgnoreCase("FADE")) {
            drawFadeHole(tBB, lines, color, lineColor, depth, noLineDepth, notSelf, width, height, fadeAlpha, sidesByte);
        }
    }
    
    private static void drawFullHole(AxisAlignedBB axisAlignedBB, Colour color, Colour lineColor, float width, int sides) {
        TessellatorUtil.prepare();
        TessellatorUtil.drawBox(axisAlignedBB, true, 1, color, color.a, sides);
        TessellatorUtil.release();
        drawOutlineHole(axisAlignedBB, lineColor, width);
    }
    
    private static void drawOutlineHole(AxisAlignedBB axisAlignedBB, Colour lineColor, float width) {
        TessellatorUtil.prepare();
        TessellatorUtil.drawBoundingBox(axisAlignedBB, width, lineColor);
        TessellatorUtil.release();
    }
    
    private static void drawWireframeHole(AxisAlignedBB axisAlignedBB, Colour lineColor, float width) {
        BlockRenderUtil.prepareGL();
        BlockRenderUtil.drawWireframe(
                axisAlignedBB.offset(
                    -((AccessorRenderManager) mc.getRenderManager()).getRenderPosX(),
                    -((AccessorRenderManager) mc.getRenderManager()).getRenderPosY(),
                    -((AccessorRenderManager) mc.getRenderManager()).getRenderPosZ()
                ),
                lineColor.getRGB(), width
        );
        BlockRenderUtil.releaseGL();
    }

    private static void drawFadeHole(
            AxisAlignedBB axisAlignedBB,
            String lines,
            Colour color,
            Colour lineColor,
            boolean depth,
            boolean noLineDepth,
            boolean notSelf,
            float width,
            double height,
            int fadeAlpha,
            int sides
    ) {
        axisAlignedBB = axisAlignedBB.setMaxY(axisAlignedBB.minY + height);

        if (axisAlignedBB.intersects(mc.player.getEntityBoundingBox()) && notSelf) axisAlignedBB = axisAlignedBB.setMaxY(Math.min(axisAlignedBB.maxY, mc.player.posY + 1D));

        TessellatorUtil.prepare();
        if (depth) {
            GlStateManager.enableDepth();
            axisAlignedBB = axisAlignedBB.shrink(0.01D);
        }
        TessellatorUtil.drawBox(axisAlignedBB, true, height, color, fadeAlpha, sides);
        if (width >= 0.1F) {
            if (lines.equalsIgnoreCase("BOTTOM")) axisAlignedBB = new AxisAlignedBB(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
            else if (lines.equalsIgnoreCase("TOP")) axisAlignedBB = new AxisAlignedBB(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
            if (noLineDepth) GlStateManager.disableDepth();
            TessellatorUtil.drawBoundingBox(axisAlignedBB, width, lineColor, fadeAlpha);
        }
        TessellatorUtil.release();
    }
}
