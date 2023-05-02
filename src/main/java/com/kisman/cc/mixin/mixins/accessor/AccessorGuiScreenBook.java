package com.kisman.cc.mixin.mixins.accessor;

import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiScreenBook.class)
public interface AccessorGuiScreenBook {
    @Accessor("book")
    ItemStack getBook();

    @Accessor("bookIsUnsigned")
    boolean bookIsUnsigned();
}
