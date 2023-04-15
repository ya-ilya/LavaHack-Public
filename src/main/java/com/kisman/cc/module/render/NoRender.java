package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.settings.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRender extends Module {
    public static NoRender instance;

    public final Setting fog = new Setting("Fog", this, false);
    public final Setting hurtCam = new Setting("HurtCam", this, false);
    public final Setting armor = new Setting("Armor", this, false);
    public final Setting overlay = new Setting("Overlay", this, false);
    public final Setting guiOverlay = new Setting("Gui Overlay", this, false);
    public final Setting book = new Setting("Book", this, false);
    public final Setting chatBackground = new Setting("Chat Background", this, false);
    public final Setting bossBar = new Setting("Boss Bar", this, false);
    public final Setting scoreboard = new Setting("Scoreboard", this, false);
    public final Setting particle = new Setting("Particle", this, ParticleMode.None);
    public final Setting portal = new Setting("Portal", this, false);
    public final Setting items = new Setting("Items", this, false);
    public final Setting defaultBlockHighlight = new Setting("Default Block Highlight", this, false);
    public final Setting handItemsTex  = new Setting("Hand Items Texture", this, false);
    public final Setting enchantGlint = new Setting("Enchant Glint", this, false);

    public NoRender() {
        super("NoRender", "no render", Category.RENDER);

        instance = this;

        setmgr.rSetting(fog);
        setmgr.rSetting(hurtCam);
        setmgr.rSetting(armor);
        setmgr.rSetting(overlay);
        setmgr.rSetting(guiOverlay);
        setmgr.rSetting(book);
        setmgr.rSetting(chatBackground);
        setmgr.rSetting(bossBar);
        setmgr.rSetting(scoreboard);
        setmgr.rSetting(particle);
        setmgr.rSetting(portal);
        setmgr.rSetting(items);
        setmgr.rSetting(defaultBlockHighlight);
        setmgr.rSetting(handItemsTex);
        setmgr.rSetting(enchantGlint);
        setmgr.rSetting(new Setting("Potion", this, false));
        setmgr.rSetting(new Setting("Weather", this, false));
        setmgr.rSetting(new Setting("Block", this, false));
        setmgr.rSetting(new Setting("Lava", this, false));
    }

    public void update() {
        if(mc.player == null && mc.world == null) return;

        boolean potion = setmgr.getSettingByName(this, "Potion").getValBoolean();
        boolean weather = setmgr.getSettingByName(this, "Weather").getValBoolean();

        if(potion) {
            if(mc.player.isPotionActive(Potion.getPotionById(25))) mc.player.removeActivePotionEffect(Potion.getPotionById(25));
            if(mc.player.isPotionActive(Potion.getPotionById(2))) mc.player.removeActivePotionEffect(Potion.getPotionById(2));
            if(mc.player.isPotionActive(Potion.getPotionById(4))) mc.player.removeActivePotionEffect(Potion.getPotionById(4));
            if(mc.player.isPotionActive(Potion.getPotionById(9))) mc.player.removeActivePotionEffect(Potion.getPotionById(9));
            if(mc.player.isPotionActive(Potion.getPotionById(15))) mc.player.removeActivePotionEffect(Potion.getPotionById(15));
            if(mc.player.isPotionActive(Potion.getPotionById(17))) mc.player.removeActivePotionEffect(Potion.getPotionById(17));
            if(mc.player.isPotionActive(Potion.getPotionById(18))) mc.player.removeActivePotionEffect(Potion.getPotionById(18));
            if(mc.player.isPotionActive(Potion.getPotionById(27))) mc.player.removeActivePotionEffect(Potion.getPotionById(27));
            if(mc.player.isPotionActive(Potion.getPotionById(20))) mc.player.removeActivePotionEffect(Potion.getPotionById(20));
        }

        if(weather) mc.world.setRainStrength(0.0f);
    }

    @SubscribeEvent
    public void renderBlockEvent(RenderBlockOverlayEvent event) {
        if(mc.player != null && mc.world != null) {
            boolean block = setmgr.getSettingByName(this, "Block").getValBoolean();
            boolean lava = setmgr.getSettingByName(this, "Lava").getValBoolean();
            if(block) event.setCanceled(true);
            if(lava && event.getBlockForOverlay().getBlock().equals(Blocks.LAVA)) event.setCanceled(true);
        }
    }

    public enum ParticleMode {None, All, AllButIgnorePops}
}
