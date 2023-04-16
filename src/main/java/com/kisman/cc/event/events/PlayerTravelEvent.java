package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;

public class PlayerTravelEvent extends Event {
    private final float strafe;
    private final float vertical;
    private final float forward;

    public PlayerTravelEvent(float strafe, float vertical, float forward) {
        this.strafe = strafe;
        this.vertical = vertical;
        this.forward = forward;
    }

    public float getStrafe() {
        return strafe;
    }

    public float getVertical() {
        return vertical;
    }

    public float getForward() {
        return forward;
    }
}
