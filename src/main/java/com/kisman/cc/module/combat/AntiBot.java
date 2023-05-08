package com.kisman.cc.module.combat;

import com.google.common.collect.Ordering;
import com.kisman.cc.mixin.mixins.accessor.AccessorGuiPlayerTabOverlay;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.ChatUtil;
import com.kisman.cc.util.EntityUtil;
import com.kisman.cc.util.RotationUtils;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AntiBot extends Module {
    public final Setting mode = new Setting("Mode", this, "WellMore", Arrays.asList("Matrix 6.3", "Classic", "Vanish", "Zamorozka"));

    private final List<EntityPlayer> bots = new ArrayList<>();
    public static AntiBot instance;
    public EntityPlayer target;
    private boolean clicked = false;

	public AntiBot() {
		super("AntiBot", "Prevents you from targeting bots", Category.COMBAT);

        instance = this;

        register(mode);
	}

	public void update() {
        if (mc.player == null || mc.world == null) return;

        if (mode.checkValString("Zamorozka") && mc.currentScreen == null && Mouse.isButtonDown(0)) {
            if (!clicked) {
                clicked = true;
                RayTraceResult result = mc.objectMouseOver;
                if (result == null || result.typeOfHit != RayTraceResult.Type.ENTITY) return;
                Entity entity = mc.objectMouseOver.entityHit;
                if (!(entity instanceof EntityPlayer)) return;
                target = (EntityPlayer) entity;
                ChatUtil.complete("[AntiBot] Current target is " + entity.getName());
            } else clicked = false;
        }

        for (EntityPlayer entity : mc.world.playerEntities) {
            if (entity != mc.player && !entity.isDead) {
                if (mode.getValString().equalsIgnoreCase("Classic")) {
                    List<EntityPlayer> tabList = getTabPlayerList();
                    if (!bots.contains(entity) && !tabList.contains(entity)) bots.add(entity);
                    else if (bots.contains(entity) && tabList.contains(entity)) bots.remove(entity);
                } else {
                    if (mode.getValString().equalsIgnoreCase("Matrix 6.3")) {
                        boolean contains = RotationUtils.isInFOV(entity, mc.player, 100.0) && AntiBot.mc.player.getDistance(entity) <= 6.5 && entity.canEntityBeSeen(mc.player);
                        boolean speedAnalysis = entity.getActivePotionEffect(MobEffects.SPEED) == null && entity.getActivePotionEffect(MobEffects.JUMP_BOOST) == null && entity.getActivePotionEffect(MobEffects.LEVITATION) == null && !entity.isInWater() && entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA && EntityUtil.getSpeedBPS(entity) >= 11.9;
                        if (!contains || !speedAnalysis || entity.isDead) continue;
                    } else if (!entity.isInvisible()) continue;
                    entity.isDead = true;
                    ChatUtil.complete(entity.getName() + " was been deleted!");
                }
            }
        }

        if (mode.getValString().equalsIgnoreCase("Classic")) for (EntityPlayer bot : bots) {
            bot.isDead = true;
            ChatUtil.complete(bot.getName() + " was been deleted!");
        }
	}

    public void onEnable() {
        clicked = false;
    }

    public void onDisable() {
        target = null;
    }

    private List<EntityPlayer> getTabPlayerList() {
        List<EntityPlayer> list = new ArrayList<>();
        Ordering<NetworkPlayerInfo> ENTRY_ORDERING = AccessorGuiPlayerTabOverlay.getEntryOrdering();
        if (ENTRY_ORDERING == null) return list;
        List<NetworkPlayerInfo> players = ENTRY_ORDERING.sortedCopy(mc.player.connection.getPlayerInfoMap());
        for (NetworkPlayerInfo info : players) {
            if (info == null) continue;
            list.add(mc.world.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }
}