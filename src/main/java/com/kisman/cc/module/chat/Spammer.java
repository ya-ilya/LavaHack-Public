package com.kisman.cc.module.chat;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import i.gishreloaded.gishcode.utils.TimerUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Spammer extends Module {
    private final Setting customMsg = new Setting("CustomMessage", this, false);
    private final Setting customMessage = new Setting("CustomMessage", this, "_kisman_ on top!", "_kisman_ on top!", true);
    private final List<String> messages = Arrays.asList("L3g3ndry on top!",
            "_kisman_ on top!",
            "kisman.cc on top!",
            "DenYoyo on top!",
            "Buy RusherHack with code \"Robertoss\"!",
            "kisman.cc owned me((",
            "Robertoss on top!",
            "TheKisDevs owns all",
            "Gentleman is cute",
            "Dallas so better",
            "FakePearl enjoyers on tope",
            "https://github.com/TheKisDevs/LavaHack");

    private final TimerUtils timer = new TimerUtils();

    public Spammer() {
        super("Spammer", "chat spammer", Category.CHAT);

        register(new Setting("GlobalMode", this, false));
        register(new Setting("Delay", this, 5000, 0, 10000, true));
        register(customMsg);
        register(customMessage);
    }

    public void update() {
        if(mc.player == null || mc.world == null) return;

        boolean globalMode = settingManager.getSettingByName(this, "GlobalMode").getValBoolean();
        long delay = (int) settingManager.getSettingByName(this, "Delay").getValDouble();

        if (timer.passedMillis(delay)) {
            if(customMsg.getValBoolean()) {
                String message = customMessage.getValString();
                mc.player.sendChatMessage(globalMode ? "!" + message : message);
            } else {
                Random r = new Random();
                int index = r.nextInt(messages.size());
                String message = messages.get(index);
                mc.player.sendChatMessage(globalMode ? "!" + message : message);
            }

            timer.reset();
        }
    }
}
