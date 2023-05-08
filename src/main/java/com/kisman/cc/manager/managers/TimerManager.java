package com.kisman.cc.manager.managers;

import com.kisman.cc.manager.Manager;
import com.kisman.cc.module.Module;
import com.kisman.cc.util.TickRateUtil;
import com.kisman.cc.util.TimerUtil;

public class TimerManager implements Manager {
    private Module currentModule;
    private int priority;
    private float timerSpeed;
    private boolean active = false;
    private boolean tpsSync = false;

    public void updateTimer(Module module, int priority, float timerSpeed) {
        if (module == currentModule) {
            this.priority = priority;
            this.timerSpeed = timerSpeed;
            this.active = true;
        } else if (priority > this.priority || !this.active) {
            this.currentModule = module;
            this.priority = priority;
            this.timerSpeed = timerSpeed;
            this.active = true;
        }
    }

    public void resetTimer(Module module) {
        if (this.currentModule == module) active = false;
    }

    public void onUpdate() {
        if (mc.world == null || mc.player == null) {
            TimerUtil.setTickLength(50);
            return;
        }

        if (tpsSync && TickRateUtil.INSTANCE.getLatestTickRate() > 0.125D) { // 0.125D check is nessasary to avoid 0tps when joining server
            TimerUtil.setTickLength(Math.min(500, 50F * (20F / TickRateUtil.INSTANCE.getLatestTickRate())));
        } else {
            TimerUtil.setTickLength(active ? (50.0f / timerSpeed) : 50.0f);
        }
    }

    public boolean isTpsSync() {
        return tpsSync;
    }

    public void setTpsSync(boolean tpsSync) {
        this.tpsSync = tpsSync;
    }
}