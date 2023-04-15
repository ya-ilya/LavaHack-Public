package com.kisman.cc.event.events.lua;

import com.kisman.cc.event.Event;

public class ClientTickUpdateEvent extends Event {
    public String getName() {
        return "tick";
    }
}
