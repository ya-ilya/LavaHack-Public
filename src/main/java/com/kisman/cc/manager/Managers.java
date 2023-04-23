package com.kisman.cc.manager;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.manager.managers.CPSManager;
import com.kisman.cc.manager.managers.TimerManager;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.atomic.AtomicLong;

public class Managers {
    public static Managers instance;

    public TimerManager timerManager;
    public CPSManager cpsManager;

    public AtomicLong lagTimer = new AtomicLong();

    public Managers() {
        instance = this;
    }

    public void init() {
        timerManager = new TimerManager();
        cpsManager = new CPSManager();

        MinecraftForge.EVENT_BUS.register(this);
        Kisman.EVENT_BUS.subscribe(listener);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        timerManager.onUpdate();
    }

    @EventHandler
    private final Listener<PacketEvent.Receive> listener = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            lagTimer.set(System.currentTimeMillis());
        }
    });

    public boolean passed(int ms) {
        return System.currentTimeMillis() - lagTimer.get() >= ms;
    }

    public void reset() {
        lagTimer.set(System.currentTimeMillis());
    }
}
