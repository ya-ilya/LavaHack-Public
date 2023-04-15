package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;
import net.minecraft.entity.Entity;

public class PlayerApplyCollisionEvent extends Event {
    public Entity entity;

    public PlayerApplyCollisionEvent(Entity entity) {
        this.entity = entity;
    }
}
