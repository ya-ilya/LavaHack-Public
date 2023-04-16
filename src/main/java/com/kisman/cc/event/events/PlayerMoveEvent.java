package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;
import net.minecraft.entity.MoverType;

public class PlayerMoveEvent extends Event {
    private final MoverType type;
    private final double x, y, z;

    public PlayerMoveEvent(MoverType type, double x, double y, double z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MoverType getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
