package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;
import net.minecraft.util.text.ITextComponent;

public class DisconnectEvent extends Event {
    public ITextComponent component;
    public DisconnectEvent(ITextComponent component) {
        this.component = component;
    }
}
