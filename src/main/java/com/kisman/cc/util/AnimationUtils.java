package com.kisman.cc.util;

public class AnimationUtils {
    public float value;
    public long lastTime;
    public float changePerMillisecond;
    public float start;
    public float end;
    public boolean increasing;

    public AnimationUtils(long duration, float start, float end) {
        this.value = start;
        this.end = end;
        this.start = start;
        this.increasing = end > start;
        float difference = Math.abs(start - end);
        this.changePerMillisecond = difference / (float)duration;
        this.lastTime = System.currentTimeMillis();
    }

    public void reset() {
        this.value = this.start;
        this.lastTime = System.currentTimeMillis();
    }

    public float getValue() {
        if (this.value == this.end) return this.value;
        if (this.increasing) {
            if (this.value >= this.end) {
                this.value = this.end;
                return this.value;
            }
            this.value += this.changePerMillisecond * (float)(System.currentTimeMillis() - this.lastTime);
            if (this.value > this.end) this.value = this.end;
            this.lastTime = System.currentTimeMillis();
            return this.value;
        }
        if (this.value <= this.end) {
            this.value = this.end;
            return this.value;
        }
        this.value -= this.changePerMillisecond * (float)(System.currentTimeMillis() - this.lastTime);
        if (this.value < this.end) this.value = this.end;
        this.lastTime = System.currentTimeMillis();
        return this.value;
    }

    public static double animate(double target, double current, double speed) {
        if (speed < 0.0) speed = 0.0;
        else if (speed > 1.0) speed = 1.0;

        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        return  target > current ? current + factor : current - factor;
    }
}

