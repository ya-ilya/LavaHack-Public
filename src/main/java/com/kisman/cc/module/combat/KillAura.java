package com.kisman.cc.module.combat;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.*;
import i.gishreloaded.gishcode.utils.visual.ColorUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class KillAura extends Module {
    public static KillAura instance;

    public EntityPlayer target;

    private final Setting mode = new Setting("Mode", this, "HvH", Arrays.asList("HvH", "Default"));

    private final Setting hitLine = new Setting("HitLine", this, "Hit");
    private final Setting useFallDist = new Setting("Use Fall Dist", this, false);
    private final Setting fallDistance = new Setting("Fall Distance", this, 0.25, 0, 1, false).setVisible(useFallDist::getValBoolean);
    private final Setting shieldBreaker = new Setting("Shield Breaker", this, true);
    private final Setting packetAttack = new Setting("Packet Attack", this, false);
    private final Setting rotations = new Setting("Rotations", this, RotateMode.Silent);
    private final Setting betterRots = new Setting("Better Rotations", this, false).setVisible(() -> !rotations.checkValString(RotateMode.None.name()));
    private final Setting packetRots = new Setting("Packet Rotations", this, false).setVisible(() -> !rotations.checkValString(RotateMode.None.name()));
    private final Setting preRots = new Setting("Pre Rots", this, true).setVisible(() -> !rotations.checkValString(RotateMode.None.name()));
    private final Setting cooldownCheck = new Setting("Cooldown Check", this, false);
    private final Setting attackCooldown = new Setting("Attack Cooldown", this, 1, 0, 1, false).setVisible(cooldownCheck::getValBoolean);
    private final Setting onlyCrits = new Setting("Only Crits", this, false).setVisible(cooldownCheck::getValBoolean);
    private final Setting resetCd = new Setting("Reset Cooldown", this, false);
    private final Setting packetSwing = new Setting("Packet Swing", this, false);

    private final Setting weapon = new Setting("Weapon", this, "Sword", new ArrayList<>(Arrays.asList("Sword", "Axe", "Both", "None")));

    private final Setting invisible = new Setting("Invisible", this, false);

    private final Setting renderLine = new Setting("RenderLine", this, "Render");
    private final Setting targetEsp = new Setting("Target ESP", this, true);

    private final Setting targetRange = new Setting("Target Range", this, 6, 1, 20, true);
    private final Setting wallDistance = new Setting("Wall Distance", this, 3, 0, 5, false);

    private final Setting switchMode = new Setting("Switch Mode", this, "None", new ArrayList<>(Arrays.asList("None", "Normal", "Silent")));
    private final Setting packetSwitch = new Setting("Packet Switch", this, true).setVisible(!switchMode.checkValString("None"));

    public KillAura() {
        super("KillAura", "8", Category.COMBAT);

        instance = this;

        setmgr.rSetting(mode);

        setmgr.rSetting(hitLine);
        setmgr.rSetting(useFallDist);
        setmgr.rSetting(fallDistance);
        setmgr.rSetting(shieldBreaker);
        setmgr.rSetting(new Setting("HitSound", this, false));
        setmgr.rSetting(packetAttack);
        setmgr.rSetting(rotations);
        setmgr.rSetting(betterRots);
        setmgr.rSetting(packetRots);
        setmgr.rSetting(preRots);
        setmgr.rSetting(cooldownCheck);
        setmgr.rSetting(attackCooldown);
        setmgr.rSetting(onlyCrits);
        setmgr.rSetting(resetCd);
        setmgr.rSetting(packetSwing);

        setmgr.rSetting(new Setting("WeaponLine", this, "Weapon"));
        setmgr.rSetting(weapon);

        setmgr.rSetting(new Setting("TargetsLine", this, "Targets"));
        setmgr.rSetting(new Setting("Player", this, true));
        setmgr.rSetting(new Setting("Monster", this, true));
        setmgr.rSetting(new Setting("Passive", this, true));
        setmgr.rSetting(invisible);

        setmgr.rSetting(new Setting("DistanceLine", this, "Distance"));

        setmgr.rSetting(targetRange);
        setmgr.rSetting(new Setting("Distance", this, 4.25f, 0, 6, false));
        setmgr.rSetting(wallDistance);

        setmgr.rSetting(renderLine);
        setmgr.rSetting(targetEsp);

        setmgr.rSetting(new Setting("SwitchLine", this, "Switch"));
        setmgr.rSetting(switchMode);
        setmgr.rSetting(packetSwitch);
    }

    public void update() {
        if(mc.player == null || mc.world == null) return;
        if(mc.player.isDead) return;
        if(mc.player.getCooledAttackStrength(0) <= (onlyCrits.getValBoolean() ? 0.95f : attackCooldown.getValFloat()) && cooldownCheck.getValBoolean()) return;

        boolean player = setmgr.getSettingByName(this, "Player").getValBoolean();
        boolean monster = setmgr.getSettingByName(this, "Monster").getValBoolean();
        boolean passive = setmgr.getSettingByName(this, "Passive").getValBoolean();

        boolean hitsound = setmgr.getSettingByName(this, "HitSound").getValBoolean();

        float distance = setmgr.getSettingByName(this, "Distance").getValFloat();

        if(mode.getValString().equalsIgnoreCase("Default")) {
            Entity target1 = EntityUtil.getTarget(distance, distance, player, passive, monster);
            target = null;
            if(target1 == null) return;
            super.setDisplayInfo("[" + target1.getName() + "]");
            if(preRots.getValBoolean()) doRots(target1);
            if(!weaponCheck()) return;
            if(!fallCheck() && useFallDist.getValBoolean()) return;
            doKillAura(target1, distance, hitsound, true);
        } else if(mode.getValString().equalsIgnoreCase("HvH")) {
           target = EntityUtil.getTarget(distance);
           if(target == null) return;
           super.setDisplayInfo("[" + target.getName() + "]");
           if(preRots.getValBoolean()) doRots(target);
           if(!weaponCheck()) return;
           if(!fallCheck() && useFallDist.getValBoolean()) return;
           doKillAura(target, distance, hitsound, true);
        }
    }

    private boolean fallCheck() {
        return mc.player.fallDistance > fallDistance.getValFloat();
    }


    private void doKillAura(Entity entity, double distance, boolean hitsound, boolean single) {
        if(mc.player.getDistance(entity) <= distance && entity.ticksExisted % 20 == 0 && mc.player != entity) {

            boolean isShieldActive = false;

            if(shieldBreaker.getValBoolean() && single && entity instanceof EntityPlayer) if (((EntityPlayer) entity).getHeldItemMainhand().getItem() instanceof ItemShield || ((EntityPlayer) entity).getHeldItemOffhand().getItem() instanceof ItemShield) if (((EntityPlayer) entity).isHandActive()) isShieldActive = true;

            int oldSlot = mc.player.inventory.currentItem;
            int weaponSlot = InventoryUtil.findWeaponSlot(0, 9, isShieldActive);

            boolean isHit = false;
            if(!switchMode.getValString().equalsIgnoreCase("None")) {
                if(weaponSlot != -1) {
                    switch (switchMode.getValString()) {
                        case "Normal": {
                            InventoryUtil.switchToSlot(weaponSlot, false);
                            break;
                        }
                        case "Silent": {
                            InventoryUtil.switchToSlot(weaponSlot, true);
                            break;
                        }
                    }
                } else return;
            }

            attack(entity);
            isHit = true;

            if (hitsound && isHit) mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_STONE_BREAK, 1));
            if(switchMode.getValString().equalsIgnoreCase("Silent") && oldSlot != -1) InventoryUtil.switchToSlot(oldSlot, true);
        }
    }

    private void attack(Entity entity) {
        float oldYaw = mc.player.rotationYaw, oldPitch = mc.player.rotationPitch, oldYawOffset = mc.player.renderYawOffset, oldYawHead = mc.player.rotationYawHead;
        if(!preRots.getValBoolean()) doRots(entity);

        if(packetAttack.getValBoolean()) mc.player.connection.sendPacket(new CPacketUseEntity(entity));
        else mc.playerController.attackEntity(mc.player, entity);

        if(packetSwing.getValBoolean()) mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        else mc.player.swingArm(EnumHand.MAIN_HAND);
        if(resetCd.getValBoolean()) mc.player.resetCooldown();

        if(rotations.getValString().equals("Silent")) {
            mc.player.rotationYaw = oldYaw;
            mc.player.rotationPitch = oldPitch;
        } else if(rotations.getValString().equals("SilentWellMore")) {
            mc.player.rotationYaw = oldYaw;
            mc.player.renderYawOffset = oldYawOffset;
            mc.player.rotationYawHead = oldYawHead;
            mc.player.rotationPitch = oldPitch;
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if(target == null) return;
            RenderUtil.drawFadeESP(target, new Colour(Color.GREEN), new Colour(ColorUtils.injectAlpha(Color.GREEN, 0)));
    }

    private void doRots(Entity entityToRotate) {
        switch (rotations.getValString()) {
            case "None": break;
            case "Normal":
            case "Silent": {
                float[] rots = RotationUtils.getRotation(entityToRotate);
                if(packetRots.getValBoolean()) packetRotation(rots);
                else rotation(rots);
                break;
            }
            case "SilentWellMore":
            case "WellMore": {
                float[] rots = RotationUtils.lookAtRandomed(entityToRotate);
                if(packetRots.getValBoolean()) packetRotation(rots);
                else rotation(rots);
                break;
            }
        }
    }

    private void packetRotation(float[] rots) {
        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rots[0], rots[1], mc.player.onGround));
    }

    private void rotation(float[] rots) {
        mc.player.rotationYaw = rots[0];
        if (betterRots.getValBoolean()) {
            mc.player.renderYawOffset = rots[0];
            mc.player.rotationYawHead = rots[0];
        }
        mc.player.rotationPitch = rots[1];
    }

    private boolean weaponCheck() {
        if(switchMode.getValString().equals("None")) {
            switch (weapon.getValString()) {
                case "None": break;
                case "Sword": if(!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) return false;
                case "Axe": if(!(mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe)) return false;
                case "Both": if(!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe)) return false;
            }
        }

        return true;
    }

    public enum RotateMode {None, Normal, Silent, WellMore, SilentWellMore}
}