package com.kisman.cc.module.combat;

import com.kisman.cc.module.Category;
import com.kisman.cc.util.BlockUtil2;
import com.kisman.cc.util.ChatUtil;
import com.kisman.cc.util.EntityUtil;
import com.kisman.cc.util.InventoryUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class SelfTrap extends AutoTrap {
    public SelfTrap() {
        super("SelfTrap", Category.COMBAT);

        register(disableOnComplete);
        register(placeDelay);
        register(rotate);
        register(blocksPerTick);
        register(antiScaffold);
        register(antiStep);
        register(surroundPlacing);
        register(range);
        register(raytrace);
        register(packet);
        register(rewrite);
        register(dynamic);
        register(supportBlocks);
        register(rewriteRetries);
        register(rotateMode);
    }

    @Override
    protected void doRewriteTrap() {
        target = mc.player;

        int blockSlot;
        int oldSlot = mc.player.inventory.currentItem;
        if (InventoryUtil.findBlock(Blocks.OBSIDIAN, 0, 9) != -1) blockSlot = InventoryUtil.findBlock(Blocks.OBSIDIAN, 0, 9);
        else if (InventoryUtil.findBlock(Blocks.ENDER_CHEST, 0, 9) != -1) blockSlot = InventoryUtil.findBlock(Blocks.OBSIDIAN, 0, 9);
        else return;

        InventoryUtil.switchToSlot(blockSlot, switch_.getValString().equalsIgnoreCase("Silent"));
        for (BlockPos pos : getPosList()) {
            if (!BlockUtil2.isPositionPlaceable(pos, true, true, tries <= rewriteRetries.getValInt())) continue;
            place(pos);
            tries++;
        }
        rewrPlacements = 0;
        if (switch_.getValString().equalsIgnoreCase(RewriteSwitchModes.Silent.name())) InventoryUtil.switchToSlot(oldSlot, true);
        if (!getPosList().isEmpty()) return;
        tries = 0;
        if (disableOnComplete.getValBoolean()) setToggled(false);
    }

    @Override
    protected boolean check() {
        if (mc.player == null || startPos == null) return false;

        didPlace = false;
        placements = 0;
        int obbySlot2 = InventoryUtil.findBlock(Blocks.OBSIDIAN, 0, 9);
        if (obbySlot2 == -1) setToggled(false);
        int obbySlot3 = InventoryUtil.findBlock(Blocks.OBSIDIAN, 0, 9);
        if (!super.isToggled()) return true;
        if (!startPos.equals(EntityUtil.getRoundedBlockPos(mc.player))) {
            setToggled(false);
            return true;
        }
        if (retryTimer.passedMillis(2000L)) {
            retries.clear();
            retryTimer.reset();
        }
        if (obbySlot3 == -1) {
            ChatUtil.error(ChatFormatting.RED + "No Obsidian in hotbar, SelfTrap disabling...");
            setToggled(false);
            return true;
        }
        if (mc.player.inventory.currentItem != this.oldSlot && mc.player.inventory.currentItem != obbySlot3) this.oldSlot = mc.player.inventory.currentItem;
        isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        target = mc.player;
        return target == null || !timer.passedMillis(placeDelay.getValInt());
    }
}
