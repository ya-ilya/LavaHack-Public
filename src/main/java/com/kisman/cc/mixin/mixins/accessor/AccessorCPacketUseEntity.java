package com.kisman.cc.mixin.mixins.accessor;

import net.minecraft.network.play.client.CPacketUseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CPacketUseEntity.class)
public interface AccessorCPacketUseEntity {
    @Accessor("entityId")
    int getEntityId();

    @Accessor("entityId")
    void setEntityId(int entityId);

    @Accessor("action")
    void setAction(CPacketUseEntity.Action action);
}
