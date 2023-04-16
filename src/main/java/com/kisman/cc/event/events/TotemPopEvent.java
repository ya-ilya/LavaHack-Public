package com.kisman.cc.event.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class TotemPopEvent extends Event {

    private final Entity entity;

    public TotemPopEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}
