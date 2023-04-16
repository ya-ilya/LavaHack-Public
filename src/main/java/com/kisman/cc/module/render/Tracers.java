package com.kisman.cc.module.render;

import com.kisman.cc.friend.FriendManager;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Tracers extends Module {
    private final Setting players = new Setting("Players", this, false);
//    private final Setting playersColor = new Setting("PlayersColor", this, "Color", new float[] {1, 1, 1, 1});
    private final Setting playersAstolfo = new Setting("PlayersAstolfo", this, false);

    private final Setting friends = new Setting("Friends", this, false);
//    private final Setting friendsColor = new Setting("friendsColor", this, "Color", new float[] {1, 1, 1, 1});
    private final Setting friendsAstolfo = new Setting("FriendsAstolfo", this, true);

    private final Setting items = new Setting("Items", this, false);
//    private final Setting itemsColor = new Setting("ItemsColor", this, "Color", new float[] {1, 1, 1, 1});
    private final Setting itemsAstolfo = new Setting("ItemsAstolfo", this, true);

    public Tracers() {
        super("Tracers", "Tracers", Category.RENDER);

        register(players);
//        setmgr.rSetting(playersColor);
        register(playersAstolfo);
        register(friends);
//        setmgr.rSetting(friendsColor);
//        setmgr.rSetting(friendsAstolfo);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        for(Entity entity : mc.world.loadedEntityList) {
            if(entity == mc.player) continue;

            if(entity instanceof EntityPlayer) {
                if(FriendManager.instance.isFriend((EntityPlayer) entity)) {
                    if(friends.getValBoolean()) {
//                        RenderUtil.drawTracer(entity, friendsAstolfo.getValBoolean() ? new Colour((float) friendsColor.getR() / 255f, (float) friendsColor.getG() / 255f, (float) friendsColor.getB() / 255f, (float) friendsColor.getA() / 255f) : new Colour(ColorUtils.astolfoColors(100, 100)), event.getPartialTicks());
                    }
                } else if(players.getValBoolean()) {
//                    RenderUtil.drawTracer(entity, playersAstolfo.getValBoolean() ? new Colour((float) playersColor.getR() / 255f, (float) playersColor.getG() / 255f, (float) playersColor.getB() / 255f, (float) friendsColor.getA() / 255f) : new Colour(ColorUtils.astolfoColors(100, 100)), event.getPartialTicks());
                }
            } else if(entity instanceof EntityItem && items.getValBoolean()) {
//                RenderUtil.drawTracer(entity, itemsAstolfo.getValBoolean() ? new Colour((float) itemsColor.getR() / 255f, (float) itemsColor.getG() / 255f, (float) itemsColor.getB() / 255f, (float) itemsColor.getA() / 255f) : new Colour(ColorUtils.astolfoColors(100, 100)), event.getPartialTicks());
            }
        }
    }
}