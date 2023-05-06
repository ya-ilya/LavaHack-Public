package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
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
    private final Setting potion = new Setting("Potion", this, false);
    private final Setting weather = new Setting("Weather", this, false);
    private final Setting block = new Setting("Block", this, false);
    private final Setting lava = new Setting("Lava", this, false);

    private final int[] potionIds = new int[] { 25, 2, 4, 9, 15, 17, 18, 27, 20 };

    public NoRender() {
        super("NoRender", Category.RENDER);

        instance = this;

        register(fog);
        register(hurtCam);
        register(armor);
        register(overlay);
        register(guiOverlay);
        register(book);
        register(chatBackground);
        register(bossBar);
        register(scoreboard);
        register(particle);
        register(portal);
        register(items);
        register(defaultBlockHighlight);
        register(handItemsTex);
        register(enchantGlint);
        register(potion);
        register(weather);
        register(block);
        register(lava);
    }

    public void update() {
        if (mc.player == null || mc.world == null) return;

        if (potion.getValBoolean()) {
            for (int potionId : potionIds) {
                Potion potion = Potion.getPotionById(potionId);

                if (potion != null && mc.player.isPotionActive(potion)) {
                    mc.player.removeActivePotionEffect(potion);
                }
            }
        }

        if (weather.getValBoolean()) mc.world.setRainStrength(0.0f);
    }

    @SubscribeEvent
    public void renderBlockEvent(RenderBlockOverlayEvent event) {
        if (mc.player != null && mc.world != null) {
            if (block.getValBoolean()) event.setCanceled(true);
            if (lava.getValBoolean() && event.getBlockForOverlay().getBlock().equals(Blocks.LAVA)) event.setCanceled(true);
        }
    }

    public enum ParticleMode {None, All, AllButIgnorePops}
}
