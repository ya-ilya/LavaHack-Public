package com.kisman.cc.event.events.lua;

import com.kisman.cc.event.Event;

public class Render3DEvent extends Event {
    public float particalTicks;
    public Render3DEvent(float particalTicks) {
        this.particalTicks = particalTicks;
    }

    public String getName() {
        return "render_3d";
    }
}
