package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.setting.util.BoxRendererPattern;
import com.kisman.cc.util.ColourUtil;
import net.minecraft.tileentity.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StorageESP extends Module{
    private final Setting distance = new Setting("Distance(Squared)", this, 4000, 10, 4000, true);
    private final Setting colorAlpha = new Setting("Color Alpha", this, 255, 0, 255, true);
    private final Setting chest = new Setting("Chest", this, true);
    private final Setting eChest = new Setting("EChest", this, true);
    private final Setting shulkerBox = new Setting("ShulkerBox", this, true);
    private final Setting dispenser = new Setting("Dispenser", this, true);
    private final Setting furnace = new Setting("Furnace", this, true);
    private final Setting hopper = new Setting("Hopper", this, true);
    private final Setting dropper = new Setting("Dropper", this, true);

    private final BoxRendererPattern renderer = new BoxRendererPattern(this);

    public StorageESP() {
        super("StorageESP", "sosat", Category.RENDER);

        register(distance);
        register(colorAlpha);

        register(chest);
        register(eChest);
        register(shulkerBox);
        register(dispenser);
        register(furnace);
        register(hopper);
        register(dropper);

        renderer.init();
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        mc.world.loadedTileEntityList.stream()
            .filter(tileEntity -> tileEntity.getDistanceSq(mc.player.posX, mc.player.posY, mc.player.posZ) <= distance.getValDouble())
            .forEach(tileEntity -> {
                if (tileEntity instanceof TileEntityChest && chest.getValBoolean()) renderer.draw(event.getPartialTicks(), ColourUtil.BlockColors.Companion.getChestColor(), tileEntity.getPos(), colorAlpha.getValInt());
                if (tileEntity instanceof TileEntityEnderChest && eChest.getValBoolean()) renderer.draw(event.getPartialTicks(), ColourUtil.BlockColors.Companion.getEnderChestColor(), tileEntity.getPos(), colorAlpha.getValInt());
                if (tileEntity instanceof TileEntityShulkerBox && shulkerBox.getValBoolean()) renderer.draw(event.getPartialTicks(), ColourUtil.BlockColors.Companion.getShulkerBoxColor(), tileEntity.getPos(), colorAlpha.getValInt());
                if (tileEntity instanceof TileEntityDispenser && dispenser.getValBoolean()) renderer.draw(event.getPartialTicks(), ColourUtil.BlockColors.Companion.getDispenserColor(), tileEntity.getPos(), colorAlpha.getValInt());
                if (tileEntity instanceof TileEntityFurnace && furnace.getValBoolean()) renderer.draw(event.getPartialTicks(), ColourUtil.BlockColors.Companion.getFurnaceColor(), tileEntity.getPos(), colorAlpha.getValInt());
                if (tileEntity instanceof TileEntityHopper && hopper.getValBoolean()) renderer.draw(event.getPartialTicks(), ColourUtil.BlockColors.Companion.getHopperColor(), tileEntity.getPos(), colorAlpha.getValInt());
                if (tileEntity instanceof TileEntityDropper && dropper.getValBoolean()) renderer.draw(event.getPartialTicks(), ColourUtil.BlockColors.Companion.getDropperColor(), tileEntity.getPos(), colorAlpha.getValInt());

                /*if (tileEntity instanceof TileEntityChest && chest) RenderUtil.drawBlockESP(tileEntity.getPos(), 0.94f, 0.60f, 0.11f);
                if (tileEntity instanceof TileEntityEnderChest && eChest) RenderUtil.drawBlockESP(tileEntity.getPos(), 0.53f, 0.11f, 0.94f);
                if (tileEntity instanceof TileEntityShulkerBox && shulkerBox) RenderUtil.drawBlockESP(tileEntity.getPos(), 0.8f, 0.08f, 0.93f);
                if (tileEntity instanceof TileEntityDispenser && dispenser) RenderUtil.drawBlockESP(tileEntity.getPos(), 0.34f, 0.32f, 0.34f);
                if (tileEntity instanceof TileEntityFurnace && furnace) RenderUtil.drawBlockESP(tileEntity.getPos(), 0.34f, 0.32f, 0.34f);
                if (tileEntity instanceof TileEntityHopper && hopper) RenderUtil.drawBlockESP(tileEntity.getPos(), 0.34f, 0.32f, 0.34f);
                if (tileEntity instanceof TileEntityDropper && dropper) RenderUtil.drawBlockESP(tileEntity.getPos(), 0.34f, 0.32f, 0.34f);*/
            }
        );
    }
}
