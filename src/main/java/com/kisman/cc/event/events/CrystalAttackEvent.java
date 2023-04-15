package com.kisman.cc.event.events;

import com.kisman.cc.event.Event;

public class CrystalAttackEvent extends Event {
    public int entityId;

    public CrystalAttackEvent(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityID() {
        return this.entityId;
    }
}