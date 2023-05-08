package com.kisman.cc.mixin.mixins.accessor;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemRenderer.class)
public interface AccessorItemRenderer {
    @Accessor("prevEquippedProgressMainHand")
    float getPrevEquippedProgressMainHand();

    @Accessor("equippedProgressMainHand")
    void setEquippedProgressMainHand(float equippedProgressMainHand);

    @Accessor("itemStackMainHand")
    void setItemStackMainHand(ItemStack itemStackMainHand);
}
