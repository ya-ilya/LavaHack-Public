package com.kisman.cc.event;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.lua.ClientChatEvent;
import com.kisman.cc.event.events.lua.ClientTickUpdateEvent;
import com.kisman.cc.event.events.lua.Render2DEvent;
import com.kisman.cc.event.events.lua.Render3DEvent;
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
        Kisman.EVENT_BUS.post(new ClientTickUpdateEvent());
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        Kisman.instance.scriptManager.runCallback("hud");
        Kisman.EVENT_BUS.post(new Render2DEvent(event.getPartialTicks()));
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        Kisman.EVENT_BUS.post(new Render3DEvent(event.getPartialTicks()));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onClientChat(net.minecraftforge.client.event.ClientChatEvent event) {
        ClientChatEvent eventClientChat = new ClientChatEvent(event.getMessage());
        Kisman.EVENT_BUS.post(eventClientChat);
        if(eventClientChat.isCancelled()) event.setMessage(eventClientChat.message);
    }
}
