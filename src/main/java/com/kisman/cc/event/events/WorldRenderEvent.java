package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;

public class WorldRenderEvent extends Event{
    public final float particalTicks;

    public WorldRenderEvent(float particalTicks) {
        super();
        this.particalTicks = particalTicks;
    }

    public float getParticalTicks() {
        return particalTicks;
    }
}
