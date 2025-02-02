package com.kisman.cc.mixin.mixins;

import com.kisman.cc.gui.other.container.ItemESP;
import com.kisman.cc.module.render.ContainerModifier;
import com.kisman.cc.util.Render2DUtil;
import com.kisman.cc.util.gish.ColorUtil;
import com.kisman.cc.util.render.objects.AbstractGradient;
import com.kisman.cc.util.render.objects.Vec4d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer extends GuiScreen {
    @Shadow protected int guiLeft, guiTop, xSize, ySize;
    @Shadow public Container inventorySlots;
    @Shadow protected abstract boolean checkHotbarKeys(int keyCode);
    @Shadow private Slot hoveredSlot;
    @Shadow protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {}

    public ItemESP itemESP = new ItemESP();

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGuiHook(CallbackInfo ci) {
        itemESP.init(guiLeft, guiTop, xSize);
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    public void drawScreenTailHook(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (ContainerModifier.instance.isToggled() && ContainerModifier.instance.containerShadow.getValBoolean()) {
            if (ContainerModifier.instance.containerShadow.getValBoolean()) {
                {
                    double x = 0, y = (guiTop + xSize / 2.0) - guiLeft / 2.0, y2 = (guiTop + xSize / 2.0) + guiLeft / 2.0;
                    double x2 = guiLeft, y3 = guiTop, y4 = guiTop + ySize;

                    Render2DUtil.drawAbstract(new AbstractGradient(new Vec4d(new double[]{x, y}, new double[]{x2, y3}, new double[]{x2, y4}, new double[]{x, y2}), Color.BLACK, new Color(0, 0, 0, 0), false));
                }

                {
                    double x = new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth_double(), y = (guiTop + xSize / 2.0) - guiLeft / 2.0, y2 = (guiTop + xSize / 2.0) + guiLeft / 2.0;

                    Render2DUtil.drawAbstract(new AbstractGradient(new Vec4d(new double[]{guiLeft + xSize, guiTop}, new double[]{x, y}, new double[]{x, y2}, new double[]{guiLeft + xSize, guiTop + ySize}), new Color(0, 0, 0, 0), Color.BLACK, false));
                }
            }
            if (ContainerModifier.instance.itemESP.getValBoolean()) itemESP.getGuiTextField().drawTextBox();
        }
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    private void drawScreenHeadHook(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        itemESP.getItemStacks().clear();
        if (!itemESP.getGuiTextField().getText().isEmpty()) for (Slot slot : inventorySlots.inventorySlots) if (slot.getHasStack() && slot.getStack().getDisplayName().toLowerCase().contains(itemESP.getGuiTextField().getText().toLowerCase()))  itemESP.getItemStacks().add(slot.getStack());
    }

    /**
     * @author _kisman_
     * @reason .
     */
    @Overwrite
    protected void keyTyped(char typedChar, int keyCode) {
        if (ContainerModifier.instance.itemESP.getValBoolean()) itemESP.getGuiTextField().textboxKeyTyped(typedChar, keyCode);

        if (keyCode == 1 || (this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode) && !itemESP.getGuiTextField().isFocused())) {
            this.mc.player.closeScreen();
        }

        checkHotbarKeys(keyCode);
        if (this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
            if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(keyCode)) {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, 0, ClickType.CLONE);
            } else if (this.mc.gameSettings.keyBindDrop.isActiveAndMatches(keyCode)) {
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClickedHook(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        if (ContainerModifier.instance.isToggled() && ContainerModifier.instance.itemESP.getValBoolean()) {
            itemESP.getGuiTextField().mouseClicked(mouseX, mouseY, mouseButton);
            if (itemESP.getGuiTextField().isFocused()) ci.cancel();
        }
    }
    
    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;enableDepth()V"))
    private void drawSlotHook(Slot slot, CallbackInfo ci) {
        if (ContainerModifier.instance.isToggled() && ContainerModifier.instance.itemESP.getValBoolean() && !itemESP.getItemStacks().isEmpty() && itemESP.getItemStacks().contains(slot.getStack())) drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, ColorUtil.astolfoColors(100, 100));
    }
}
