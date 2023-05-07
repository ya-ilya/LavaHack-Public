package com.kisman.cc.manager.managers;

import com.kisman.cc.Kisman;
import com.kisman.cc.event.events.PacketEvent;
import com.kisman.cc.manager.Manager;
import com.kisman.cc.util.TimerUtil;
import me.zero.alpine.listener.Listener;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;

public class CPSManager implements Manager {
    private final TimerUtil timer = new TimerUtil();
    private int usage = 0;
    private int cps = 0;

    public CPSManager() {
        Kisman.EVENT_BUS.subscribe(new Listener<PacketEvent.Send>(event -> {
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                    usage++;
                }
            }
        }));
    }

    public int getCPS() {
        if (timer.passedMillis(1000)) {
            if (usage == 0) {
                cps = 0;
                timer.reset();
            } else {
                cps = usage;
                timer.reset();
                usage = 0;
            }
        }
        return cps;
    }
}
