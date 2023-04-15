package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;
import net.minecraft.item.ItemStack;

public class RenderToolTipEvent extends Event {
    public ItemStack stack;
    public int x, y;
    public RenderToolTipEvent(ItemStack stack, int x, int y) {
        this.stack = stack;
        this.x = x;
        this.y = y;
    }
}
