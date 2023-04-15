package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;
import net.minecraft.entity.MoverType;

public class PlayerMoveEvent extends Event {
    public MoverType type;
    public double x, y, z;

    public PlayerMoveEvent(MoverType type, double x, double y, double z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getName() {
        return "player_move";
    }
}
