package com.kisman.cc.gui.hud.hudmodule.render;

import com.kisman.cc.gui.hud.hudmodule.HudCategory;
import com.kisman.cc.gui.hud.hudmodule.HudModule;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NotificationsModule extends HudModule {
    public NotificationsModule() {
        super("Notifications", HudCategory.RENDER);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
//        Kisman.instance.notificationsManager.draw();
    }
}
