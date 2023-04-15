package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;

public class Render2DEvent extends Event {
    public float particalTicks;

    public Render2DEvent(float particalTicks) {
        this.particalTicks = particalTicks;
    }
}
