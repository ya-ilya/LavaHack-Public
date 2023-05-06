package com.kisman.cc.module.combat;

import com.kisman.cc.gui.csgo.components.Slider;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.Colour;
import com.kisman.cc.util.CrystalUtils;
import com.kisman.cc.util.RenderUtil;
import com.kisman.cc.util.TimerUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

public class AntiTrapDoorBurrow extends Module {
    private final Setting delay = new Setting("Delay", this, 0, 0, 1000, Slider.NumberType.TIME);
    private final Setting range = new Setting("Range", this, 5, 1, 5, true);
    private final Setting render = new Setting("Render", this, false);
    private final Setting color = new Setting("Color", this, "Color", new Colour(Color.WHITE)).setVisible(render::getValBoolean);
    private final Setting rotate = new Setting("Rotate", this, false);

    public static AntiTrapDoorBurrow instance;

    private final Vec3d interactVector = (new Vec3d(0.5, 1, 0.5));
    private final TimerUtil timer = new TimerUtil();
    private BlockPos currentPos = null;

    public AntiTrapDoorBurrow() {
        super("AntiTrapDoorBurrow", "Close trap doors", Category.COMBAT);

        instance = this;

        register(delay);
        register(range);
        register(render);
        register(color);
        register(rotate);
    }

    public void onEnable() {
        currentPos = null;
        timer.reset();
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;

        ArrayList<BlockPos> blocks = new ArrayList<>();

        for (BlockPos pos : CrystalUtils.getSphere(range.getValFloat(), true, false)) if (mc.world.getBlockState(pos).getBlock() == Blocks.TRAPDOOR) blocks.add(pos);
        for (BlockPos pos : blocks) {
            if (timer.passedMillis(delay.getValLong())) {
                interactTrapdoor(pos, new Vec3d(pos).add(interactVector));
                timer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld() {
        if (currentPos != null && render.getValBoolean()) {
            RenderUtil.drawBlockESP(currentPos, color.getColour().r1, color.getColour().g1, color.getColour().b1);
        }
    }

    private void interactTrapdoor(BlockPos pos, Vec3d vec) {
        if (rotate.getValBoolean()) mc.player.	rotationPitch = 90.0f;
        mc.playerController.processRightClickBlock(mc.player, mc.world, pos, EnumFacing.UP, vec, EnumHand.MAIN_HAND);
    }
}
