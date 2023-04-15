package com.kisman.cc.event.events.lua;

import com.kisman.cc.event.Event;

public class Render2DEvent extends Event {
    public float particalTicks;
    public Render2DEvent(float particalTicks) {
        this.particalTicks = particalTicks;
    }

    public String getName() {
        return "render_2d";
    }
}
