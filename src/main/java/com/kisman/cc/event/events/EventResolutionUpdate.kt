package com.kisman.cc.event.events

import com.kisman.cc.event.Event

class EventResolutionUpdate(val width: Int, val height: Int) : Event() {
    override fun getName(): String {
        return "update_resolution"
    }
}