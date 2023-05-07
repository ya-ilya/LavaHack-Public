package com.kisman.cc.module.player;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;

public class NoInteract extends Module {
    private final Setting enderChest = new Setting("EnderChest", this, true);
    private final Setting craft = new Setting("CraftingTable", this, true);
    private final Setting chest = new Setting("Chest", this, true);
    private final Setting furnace = new Setting("Furnace", this, true);
    private final Setting armorStand = new Setting("ArmorStand", this, true);
    private final Setting anvil = new Setting("Anvil", this, true);

    public NoInteract() {
        super("NoInteract", Category.PLAYER);

        register(enderChest);
        register(craft);
        register(chest);
        register(furnace);
        register(armorStand);
        register(anvil);
    }

    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
        if (mc.player == null || mc.world == null) return;

        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && mc.objectMouseOver != null &&mc.objectMouseOver.getBlockPos() != null && mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock() != null) {

            Block block = mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();

            if ((block == Blocks.CRAFTING_TABLE && craft.getValBoolean()) ||
                    (block == Blocks.FURNACE && furnace.getValBoolean()) ||
                    (block == Blocks.ENDER_CHEST && enderChest.getValBoolean()) ||
                    (block == Blocks.CHEST && chest.getValBoolean()) ||
                    (block == Blocks.ANVIL && anvil.getValBoolean()) ||
                    (mc.objectMouseOver.entityHit instanceof EntityArmorStand && armorStand.getValBoolean())) {
                event.cancel();
            }
        }
    });
}
