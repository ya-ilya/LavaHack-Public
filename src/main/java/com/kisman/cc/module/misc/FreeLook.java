package com.kisman.cc.module.misc;

import com.kisman.cc.event.events.PlayerTurnEvent;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import me.zero.alpine.listener.Listener;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FreeLook extends Module {
    private final Setting autoThirdPerson = new Setting("Auto Third Person", this, false);

    private float dYaw, dPitch;

    public FreeLook() {
        super("FreeLook", Category.MISC);

        register(autoThirdPerson);
    }

    public void onEnable() {
        dYaw = dPitch = 0;

        if (mc.player == null || mc.world == null) return;
        if (autoThirdPerson.getValBoolean()) mc.gameSettings.thirdPersonView = 1;
    }

    public void onDisable() {
        if (mc.player == null || mc.world == null) return;
        if (autoThirdPerson.getValBoolean()) mc.gameSettings.thirdPersonView = 0;
    }

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if (mc.gameSettings.thirdPersonView > 0) {
            event.setYaw(event.getYaw() + dYaw);
            event.setPitch(event.getPitch() + dPitch);
        }
    }

    @SuppressWarnings("unused")
    private final Listener<PlayerTurnEvent> turnListener = listener(event -> {
        if (mc.gameSettings.thirdPersonView > 0) {
            dYaw = (float) ((double) dYaw + (double) event.getYaw() * 0.15D);
            dPitch = (float) ((double) dPitch - (double) event.getPitch() * 0.15D);
            dPitch = MathHelper.clamp(dPitch, -90.0F, 90.0F);
            event.cancel();
        }
    });
}
