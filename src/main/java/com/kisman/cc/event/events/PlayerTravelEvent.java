package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;

public class PlayerTravelEvent extends Event {
    public float strafe;
    public float vertical;
    public float forward;

    public PlayerTravelEvent(float strafe, float vertical, float forward) {
        this.strafe = strafe;
        this.vertical = vertical;
        this.forward = forward;
    }
}
