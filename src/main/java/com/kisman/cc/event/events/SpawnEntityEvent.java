package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;
import net.minecraft.entity.Entity;

public class SpawnEntityEvent extends Event {
    public Entity entity;

    public SpawnEntityEvent(Entity entity) {
        this.entity = entity;
    }
}
