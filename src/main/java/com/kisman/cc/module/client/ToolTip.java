package com.kisman.cc.module.client;

import com.kisman.cc.event.events.RenderToolTipEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.customfont.CustomFontUtil;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.awt.*;

public class ToolTip extends Module {
    private final Setting shulkers = new Setting("Shulkers", this, true);

    public ToolTip() {
        super("ToolTip", Category.CLIENT);

        register(shulkers);
    }

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<RenderToolTipEvent> renderToolTipListener = listener(event -> {
        if (shulkers.getValBoolean() && event.getStack().getItem() instanceof ItemShulkerBox) {
            renderShulkerTip(event.getStack(), event.getX(), event.getY());
            event.cancel();
        }
    });

    private void renderShulkerTip(ItemStack shulkerStack, int x, int y) {
        final NBTTagCompound tagCompound = shulkerStack.getTagCompound();

        GlStateManager.enableBlend();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        {
            // Width we need for our box
            float width = Math.max(144, CustomFontUtil.getStringWidth(shulkerStack.getDisplayName()) + 3); //9*16
            int offsetX = x + 12;
            int offsetT = y - 12;
            int height = 48 + CustomFontUtil.getFontHeight(); //3*16

            mc.getRenderItem().zLevel = 300.0F;

            // That last bit looks awful, but basically it gets the color!
            final Color color = new Color(((BlockShulkerBox) ((ItemShulkerBox) shulkerStack.getItem()).getBlock()).getColor().getColorValue());
            final Color modifiedColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 150);

            GuiScreen.drawRect(offsetX - 3, offsetT - 3, (int) (offsetX + width + 3), offsetT + height + 3, modifiedColor.getRGB());

            try {CustomFontUtil.drawStringWithShadow(shulkerStack.getDisplayName(), x + 12, y - 12, 0xFFFFFFFF);} catch (NullPointerException exception) {System.out.println("Error rendering font");}

            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            if (tagCompound != null) {
                NBTTagCompound blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag");
                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(blockEntityTag, nonnulllist);

                for (int i = 0; i < nonnulllist.size(); i++) {
                    int iX = x + (i % 9) * 16 + 11;
                    int iY = y + (i / 9) * 16 - 11 + 8;
                    ItemStack itemStack = nonnulllist.get(i);
                    mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, iX, iY);
                    mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, itemStack, iX, iY, null);
                }
            }
            RenderHelper.disableStandardItemLighting();
            mc.getRenderItem().zLevel = 0.0F;
        }

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }
}
