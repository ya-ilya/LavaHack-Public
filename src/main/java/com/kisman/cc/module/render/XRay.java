package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.BlockInteractionHelper;
import com.kisman.cc.util.Colour;
import com.kisman.cc.util.PlayerUtil;
import com.kisman.cc.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class XRay extends Module {
    public Block[] xrayBlocks = new Block[]{
            Blocks.COAL_ORE,
            Blocks.IRON_ORE,
            Blocks.GOLD_ORE,
            Blocks.REDSTONE_ORE,
            Blocks.LAPIS_ORE,
            Blocks.DIAMOND_ORE,
            Blocks.EMERALD_ORE
    };

    private final Colour[] blocksColor = new Colour[] {
            new Colour(0f, 0f, 0f),
            new Colour(0.99f, 0.52f, 0.01f),
            new Colour(0.99f, 0.75f, 0.01f),
            new Colour(0.99f, 0.01f, 0.01f),
            new Colour(0.01f, 0.11f, 0.99f),
            new Colour(0.01f, 0.56f, 0.99f),
            new Colour(0.01f, 0.99f, 0.69f)
    };

    public final Setting range = new Setting("Range", this, 50, 0, 100, false);

    public final Setting coal = new Setting("Coal", this, false);
    public final Setting iron = new Setting("Iron", this, false);
    public final Setting gold = new Setting("Gold", this, false);
    public final Setting redstone = new Setting("Redstone", this, false);
    public final Setting lapis = new Setting("Lapis", this, false);
    public final Setting diamond = new Setting("Diamond", this, false);
    public final Setting emerald = new Setting("Emerald", this, false);

    public XRay() {
        super("XRay", Category.RENDER);

        register(range);
        register(coal);
        register(iron);
        register(gold);
        register(redstone);
        register(lapis);
        register(diamond);
        register(emerald);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        for (BlockPos pos : BlockInteractionHelper.getSphere(PlayerUtil.GetLocalPlayerPosFloored(), (float) range.getValDouble(), range.getValInt(), false, true, 0)) {
            for (int i = 0; i < xrayBlocks.length; i++) renderBlock(pos, i);
        }
    }

    private void renderBlock(BlockPos pos, int i) {
        if (i == 0 && mc.world.getBlockState(pos).getBlock() == xrayBlocks[0] && coal.getValBoolean()) drawBlockESP(pos, blocksColor[0]);
        else if (i == 1 && mc.world.getBlockState(pos).getBlock() == xrayBlocks[1] && iron.getValBoolean()) drawBlockESP(pos, blocksColor[1]);
        else if (i == 2 && mc.world.getBlockState(pos).getBlock() == xrayBlocks[2] && gold.getValBoolean()) drawBlockESP(pos, blocksColor[2]);
        else if (i == 3 && (mc.world.getBlockState(pos).getBlock() == xrayBlocks[3] || mc.world.getBlockState(pos).getBlock() == Blocks.LIT_REDSTONE_ORE) && redstone.getValBoolean()) drawBlockESP(pos, blocksColor[3]);
        else if (i == 4 && mc.world.getBlockState(pos).getBlock() == xrayBlocks[4] && lapis.getValBoolean()) drawBlockESP(pos, blocksColor[4]);
        else if (i == 5 && mc.world.getBlockState(pos).getBlock() == xrayBlocks[5] && diamond.getValBoolean()) drawBlockESP(pos, blocksColor[5]);
        else if (i == 6 && mc.world.getBlockState(pos).getBlock() == xrayBlocks[6] && emerald.getValBoolean()) drawBlockESP(pos, blocksColor[6]);
    }

    private void drawBlockESP(BlockPos pos, Colour color) {RenderUtil.drawBlockESP(pos, color.r1, color.g1, color.b1);}
}
