package com.kisman.cc.event;

import com.kisman.cc.Kisman;
import com.kisman.cc.command.CommandManager;
import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.event.events.TotemPopEvent;
import com.kisman.cc.module.Module;
import com.kisman.cc.module.client.Config;
import com.kisman.cc.module.combat.AutoRer;
import com.kisman.cc.util.TickRateUtil;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class EventProcessor {
    private final Minecraft mc = Minecraft.getMinecraft();

    public EventProcessor() {
        MinecraftForge.EVENT_BUS.register(this);
        Kisman.EVENT_BUS.subscribe(totemPopListener);
        Kisman.EVENT_BUS.subscribe(TickRateUtil.INSTANCE.packetListener);
        Kisman.EVENT_BUS.subscribe(packetReceiveListener);
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        try {
            if (AutoRer.instance.lagProtect.getValBoolean()) disableCa();
            AutoRer.instance.placePos = null;
            Kisman.instance.configManager.getSaver().init();
        } catch (Exception ignored) {}
    }

    private void disableCa() {
        AutoRer.instance.setToggled(false);
    }

    @SubscribeEvent
    public void onConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (AutoRer.instance.lagProtect.getValBoolean()) disableCa();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatMessage(ClientChatEvent event) {
        if (event.getMessage().startsWith(String.valueOf(CommandManager.PREFIX))) {
            try {
                Kisman.instance.commandManager.runCommand(event.getMessage());
                event.setCanceled(true);
            } catch (Exception ignored) {}
        }
    }

    @EventHandler
    private final Listener<PacketEvent.Receive> packetReceiveListener = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketRespawn && AutoRer.instance.lagProtect.getValBoolean()) disableCa();
        if (event.getPacket() instanceof SPacketChat && !Kisman.allowToConfiguredAnotherClients && Config.instance.configurate.getValBoolean()) {
            SPacketChat packet = (SPacketChat) event.getPacket();
            String message = packet.getChatComponent().getUnformattedText();
            if (message.contains("+")) {
                String formattedMessage = message.substring(message.indexOf("+"));
                try {
                    String[] args = formattedMessage.split(" ");
                    if (args[0] != null && args[1] != null) {
                        Module module = Kisman.instance.moduleManager.getModule(args[1]);
                        if (module == null) return;
                        if (args[0].equalsIgnoreCase("disable") || args[0].equalsIgnoreCase("+disable")) module.setToggled(false);
                        else if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("+enable")) module.setToggled(true);
                        else if (args[0].equalsIgnoreCase("block") || args[0].equalsIgnoreCase("+block")) module.block = true;
                        else if (args[0].equalsIgnoreCase("unblock") || args[0].equalsIgnoreCase("+unlock")) module.block = true;
                    }
                } catch (Exception ignored) {}
            }
        }
    });

    @EventHandler
    private final Listener<PacketEvent.Receive> totemPopListener = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketEntityStatus && ((SPacketEntityStatus) event.getPacket()).getOpCode() == 35) {
            TotemPopEvent totemPopEvent = new TotemPopEvent(((SPacketEntityStatus) event.getPacket()).getEntity(mc.world));
            MinecraftForge.EVENT_BUS.post(totemPopEvent);
            if (totemPopEvent.isCanceled()) event.cancel();
        }
    });
}