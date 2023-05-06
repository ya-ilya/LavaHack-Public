package com.kisman.cc.module.misc;

import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.event.events.PlayerMotionUpdateEvent;
import com.kisman.cc.event.events.SpawnEntityEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.module.combat.AntiTrap;
import com.kisman.cc.module.combat.AutoRer;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.ChatUtil;
import com.kisman.cc.util.TimerUtil;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Tracker extends Module {
    private final Setting autoEnable = new Setting("AutoEnable", this, false);
    private final Setting autoDisable = new Setting("AutoDisable", this, true);

    private final TimerUtil timer = new TimerUtil();
    private final Set<BlockPos> manuallyPlaced = new HashSet<>();
    private EntityPlayer trackedPlayer;
    private int usedExp = 0;
    private int usedStacks = 0;
    private int usedCrystals = 0;
    private int usedCStacks = 0;
    private boolean shouldEnable = false;

    public Tracker() {
        super("Tracker", "Tracks players in 1v1s. Only good in duels tho!", Category.MISC);

        register(autoEnable);
        register(autoDisable);
    }

    public boolean isBeta() {
        return true;
    }

    public void onEnable() {
        this.manuallyPlaced.clear();
        this.shouldEnable = false;
        this.trackedPlayer = null;
        this.usedExp = 0;
        this.usedStacks = 0;
        this.usedCrystals = 0;
        this.usedCStacks = 0;
    }

    public void onDisable() {
        this.manuallyPlaced.clear();
        this.shouldEnable = false;
        this.trackedPlayer = null;
        this.usedExp = 0;
        this.usedStacks = 0;
        this.usedCrystals = 0;
        this.usedCStacks = 0;
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;

        trackedPlayer = AutoRer.currentTarget;

        if (trackedPlayer == null) return;

        if (this.usedStacks != this.usedExp / 64) {
            this.usedStacks = this.usedExp / 64;
            ChatUtil.message(this.trackedPlayer.getName() + " used: " + this.usedStacks + " Stacks of EXP.");
        }

        if (this.usedCStacks != this.usedCrystals / 64) {
            this.usedCStacks = this.usedCrystals / 64;
            ChatUtil.message(this.trackedPlayer.getName() + " used: " + this.usedCStacks + " Stacks of Crystals.");
        }
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (autoDisable.getValBoolean()) {
            super.setToggled(false);
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity().equals(mc.player) || event.getEntity().equals(trackedPlayer)) {
            this.usedExp = 0;
            this.usedStacks = 0;
            this.usedCrystals = 0;
            this.usedCStacks = 0;

            if (autoDisable.getValBoolean()) {
                super.setToggled(false);
            }
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Send> packetSendListener = listener(event -> {
        if (mc.player != null && mc.world != null && event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.getPacket();
            if (Tracker.mc.player.getHeldItem(packet.hand).getItem() == Items.END_CRYSTAL && !AntiTrap.instance.placedPos.contains(packet.position) && !AutoRer.instance.placedList.contains(packet.position)) {
                this.manuallyPlaced.add(packet.position);
            }
        }
    });

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<PacketEvent.Receive> packetReceiveListener = listener(event -> {
        if (mc.player != null && mc.world != null && (this.autoEnable.getValBoolean() || this.autoDisable.getValBoolean()) && event.getPacket() instanceof SPacketChat) {
            SPacketChat packet = (SPacketChat)event.getPacket();
            String message = packet.getChatComponent().getFormattedText();
            if (this.autoEnable.getValBoolean() && (message.contains("has accepted your duel request") || message.contains("Accepted the duel request from")) && !message.contains("<")) {
                ChatUtil.message("Tracker will enable in 5 seconds.");
                this.timer.reset();
                this.shouldEnable = true;
            }
            else if (this.autoDisable.getValBoolean() && message.contains("has defeated") && message.contains(mc.player.getName()) && !message.contains("<")) {
                super.setToggled(false);
            }
        }
    });

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<PlayerMotionUpdateEvent> motionUpdateListener = listener(event -> {
        if (shouldEnable && timer.passedSec(5L) && !super.isToggled()) {
            super.setToggled(true);
        }
    });

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<SpawnEntityEvent> spawnEntityListener = listener(event -> {
        Entity entity = event.getEntity();
        
        if (entity instanceof EntityExpBottle && Objects.equals(mc.world.getClosestPlayerToEntity(entity, 3.0), this.trackedPlayer)) {
            ++this.usedExp;
        }

        if (entity instanceof EntityEnderCrystal) {
            if (AntiTrap.instance.placedPos.contains(entity.getPosition().down())) {
                AntiTrap.instance.placedPos.remove(entity.getPosition().down());
            } else if (this.manuallyPlaced.contains(entity.getPosition().down())) {
                this.manuallyPlaced.remove(entity.getPosition().down());
            } else if (!AutoRer.instance.placedList.contains(entity.getPosition().down())) {
                ++this.usedCrystals;
            }
        }
    });
}
