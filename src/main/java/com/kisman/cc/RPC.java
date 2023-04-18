package com.kisman.cc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.kisman.cc.module.client.DiscordRPC;
import com.kisman.cc.util.Globals;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RPC implements Globals {
    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static final club.minnced.discord.rpc.DiscordRPC discordRPC = club.minnced.discord.rpc.DiscordRPC.INSTANCE;
    private static ScheduledFuture<?> schedule;

    public static synchronized void startRPC() {
        if (schedule != null)
            schedule.cancel(false);

        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));

        String discordID = "895232773961445448";
        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);

        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordRichPresence.largeImageKey = "logo";
        discordRichPresence.largeImageText = "join discord now: https://discord.gg/BEnn5xA3hg";
        discordRichPresence.smallImageKey = "plus";
        discordRichPresence.smallImageText = Kisman.NAME;
        discordRichPresence.details = Kisman.NAME + " | " + Kisman.VERSION;
        discordRichPresence.partyId = "5657657-351d-4a4f-ad32-2b9b01c91657";
        discordRichPresence.partySize = 1;
        discordRichPresence.partyMax = 10;
        discordRichPresence.joinSecret = "join";

        discordRPC.Discord_UpdatePresence(discordRichPresence);
        if (DiscordRPC.instance.impr.getValBoolean()) {
            ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

            schedule = service.scheduleWithFixedDelay(() -> {
                try {
                    String details, state;
                    discordRPC.Discord_RunCallbacks();

                    if (mc.isIntegratedServerRunning() || mc.world == null) {
                        details = Kisman.getVersion();
                    } else {
                        details = Kisman.getVersion() + " - Playing Multiplayer";
                    }

                    if (mc.isIntegratedServerRunning()) {
                        state = Kisman.getName() + " on tope!";
                    } else if (mc.getCurrentServerData() != null && !mc.getCurrentServerData().serverIP.equals("")) {
                        state = "Playing on " + mc.getCurrentServerData().serverIP;
                    } else {
                        state = "Main Menu";
                    }

                    discordRichPresence.details = details;
                    discordRichPresence.state = state;
                    discordRPC.Discord_UpdatePresence(discordRichPresence);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }, 0, 5000, TimeUnit.MILLISECONDS);
        }
    }

    public static synchronized void stopRPC() {
        if (schedule != null && !schedule.isCancelled()) {
            schedule.cancel(false);
            schedule = null;
        }
        discordRPC.Discord_Shutdown();
    }
}
