package com.kisman.cc.event;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.lua.EventClientChat;
import com.kisman.cc.event.events.lua.EventClientTickUpdate;
import com.kisman.cc.event.events.lua.EventRender2D;
import com.kisman.cc.event.events.lua.EventRender3D;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventProcessorLua {
    public EventProcessorLua() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Kisman.instance.scriptManager.runCallback("tick");
        Kisman.EVENT_BUS.post(new EventClientTickUpdate());
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        Kisman.instance.scriptManager.runCallback("hud");
        Kisman.EVENT_BUS.post(new EventRender2D(event.getPartialTicks()));
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        Kisman.EVENT_BUS.post(new EventRender3D(event.getPartialTicks()));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onClientChat(ClientChatEvent event) {
        EventClientChat eventClientChat = new EventClientChat(event.getMessage());
        Kisman.EVENT_BUS.post(eventClientChat);
        if(eventClientChat.isCancelled()) event.setMessage(eventClientChat.message);
    }
}
