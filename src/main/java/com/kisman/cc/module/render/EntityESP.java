package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.Colour;
import com.kisman.cc.util.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class EntityESP extends Module{
    private final Setting range = new Setting("Range", this, 50, 0, 100, true);

    private final Setting players = new Setting("Players", this, "None", new ArrayList<>(Arrays.asList("None", "Box1", "Box2", "Glow")));
    private final Setting monsters = new Setting("Monsters", this, "None", new ArrayList<>(Arrays.asList("None", "Box1", "Box2", "Glow")));
    private final Setting items = new Setting("Items", this, "None", new ArrayList<>(Arrays.asList("None", "Box1", "Box2", "Glow")));
    private final Setting passive =  new Setting("Passive", this, "None", new ArrayList<>(Arrays.asList("None", "Box1", "Box2", "Glow")));
    private final Setting entities = new Setting("Entities", this, "None", new ArrayList<>(Arrays.asList("None", "Box1", "Box2", "Glow")));

    //colors
    private final Setting playerColor = new Setting("PlayerColor", this, "Players Color", new Colour(255, 255, 0, 255));
    private final Setting monstersColor = new Setting("MonstersColor", this, "Monsters Color", new Colour(255, 0, 255, 255));
    private final Setting itemsColor = new Setting("ItemsColor", this, "ItemsColor", new Colour(0, 0, 255, 255));
    private final Setting passiveColor = new Setting("PassiveColor", this, "Passives Color", new Colour(0, 255, 0, 255));
    private final Setting entityColor = new Setting("EntityColor", this, "Entities Color", new Colour(0, 255, 120, 255));

    private final ArrayList<Entity> glowings = new ArrayList<>();

    public EntityESP() {
        super("EntityESP", Category.RENDER);

        register(range);

        register(new Setting("PlayersLine", this, "Players"));
        register(players);
        register(playerColor);

        register(new Setting("MonstersLine", this, "Monsters"));
        register(monsters);
        register(monstersColor);

        register(new Setting("ItemsLine", this, "Items"));
        register(items);
        register(itemsColor);

        register(new Setting("Passive", this, "Passive"));
        register(passive);
        register(passiveColor);

        register(new Setting("EntityLine", this, "Entity"));
        register(entities);
        register(entityColor);
    }

    public void onDisable() {
        if (mc.player == null || mc.world == null) return;

        glowings.forEach(entity -> entity.setGlowing(false));
    }

    public void onEnable() {
        glowings.clear();
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        mc.world.loadedEntityList.stream().filter(this::isValid).forEach(entity -> {
            if (entity instanceof EntityPlayer) render(entity, players.getValString(), playerColor.getColour().r1, playerColor.getColour().g1, playerColor.getColour().b1, event.getPartialTicks());
            if (entity instanceof EntityMob) render(entity, monsters.getValString(), monstersColor.getColour().r1, monstersColor.getColour().g1, monstersColor.getColour().b1, event.getPartialTicks());
            if (entity instanceof EntityAnimal) render(entity, passive.getValString(), passiveColor.getColour().r1, passiveColor.getColour().g1, passiveColor.getColour().b1, event.getPartialTicks());
            if (entity instanceof EntityItem) render(entity, items.getValString(), itemsColor.getColour().r1, itemsColor.getColour().g1, itemsColor.getColour().b1, event.getPartialTicks());
        });
    }

    private void render(Entity entity, String mode, float red, float green, float blue, float ticks) {
        switch (mode) {
            case "None":
                entity.setGlowing(false);
                return;
            case "Box1":
                RenderUtil.drawESP(entity, red, green, blue, 1, ticks);
                entity.setGlowing(false);
                break;
            case "Box2":
                RenderUtil.drawBoxESP(entity.getEntityBoundingBox(), new Color(red, green, blue), 1f, true, true, 100, 255);
                entity.setGlowing(false);
                break;
            case "Glow":
                entity.setGlowing(true);
                if (!glowings.contains(entity)) glowings.add(entity);
                break;
        }
    }

    private boolean isValid(Entity entity) {
        if (entity == mc.player) return false;
        return (entity instanceof EntityPlayer && !players.getValString().equalsIgnoreCase("None")) || (entity instanceof EntityMob && !monsters.getValString().equalsIgnoreCase("None")) || (entity instanceof EntityAnimal && !passive.getValString().equalsIgnoreCase("None")) || (entity instanceof EntityItem && !items.getValString().equalsIgnoreCase("None") || (entity instanceof EntityXPOrb && !items.getValString().equalsIgnoreCase("None")));
    }
}
