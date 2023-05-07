package com.kisman.cc.event.events

import com.kisman.cc.event.Event
import net.minecraft.entity.Entity

sealed class RenderEntityEvent(val entity: Entity) : Event() {
    class Pre(entity: Entity) : RenderEntityEvent(entity)

    class Post(entity: Entity) : RenderEntityEvent(entity)

    companion object {
        @JvmStatic
        var renderingEntities = false
    }
}