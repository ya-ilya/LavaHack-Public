package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;
import net.minecraft.entity.Entity;

public class RenderEntityNameEvent extends Event {
    public Entity entityIn;
    public double x, y, z, distanceSq;
    public String name;

    public RenderEntityNameEvent(Entity entityIn, double x, double y, double z, String name, double distanceSq) {
        super();
        this.entityIn = entityIn;
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
        this.distanceSq = distanceSq;
    }
}
