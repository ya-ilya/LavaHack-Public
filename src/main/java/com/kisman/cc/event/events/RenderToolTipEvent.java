package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;
import net.minecraft.item.ItemStack;

public class RenderToolTipEvent extends Event {
    private final ItemStack stack;
    private final int x, y;

    public RenderToolTipEvent(ItemStack stack, int x, int y) {
        this.stack = stack;
        this.x = x;
        this.y = y;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
