package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.BlockUtil;
import com.kisman.cc.util.Colour;
import com.kisman.cc.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class BlockOutline extends Module {
    public static BlockOutline instance;

    private final Setting colorVal = new Setting("Color", this, "Color", new Colour(0, 0, 255));
    private final Setting renderMode = new Setting("RenderMode", this, "Outline", new ArrayList<>(Arrays.asList("Outline", "Box", "OutlineBox", "Flat")));

    public BlockOutline() {
        super("BlockOutline", "BlockOutline", Category.RENDER);

        instance = this;

        register(colorVal);
        register(renderMode);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if(mc.objectMouseOver == null) return;
        if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            Block block = BlockUtil.getBlock(mc.objectMouseOver.getBlockPos());
            BlockPos blockPos = mc.objectMouseOver.getBlockPos();

            if (Block.getIdFromBlock(block) == 0) return;
            float[] color = new float[] {colorVal.getColour().getColor().getRed() / 255f, colorVal.getColour().getColor().getGreen() / 255f, colorVal.getColour().getColor().getBlue() / 255f};
            String renderMode = this.renderMode.getValString();
            if(renderMode.equalsIgnoreCase("OutlineBox")) {
                RenderUtil.drawBlockESP(
                        blockPos,
                        color[0],
                        color[1],
                        color[2]
                );
            } else if(renderMode.equalsIgnoreCase("Flat")) {
                RenderUtil.drawBlockFlatESP(
                        blockPos,
                        color[0],
                        color[1],
                        color[2]
                );
            } else if(renderMode.equalsIgnoreCase("Outline")) {
                RenderUtil.drawBlockOutlineESP(
                        blockPos,
                        color[0],
                        color[1],
                        color[2]
                );
            }
        }
    }
}
