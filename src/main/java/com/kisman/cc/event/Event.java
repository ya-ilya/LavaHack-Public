package com.kisman.cc.event;

import com.kisman.cc.Kisman;
import me.zero.alpine.event.type.Cancellable;

public class Event extends Cancellable {
    private Era era;
    public Event() {}
    public Event(Era era) {this.era = era;}
    public Era getEra() {return era;}
    public void setEra(Era era) {this.era = era;}

    public enum Era {
        PRE,
        POST,
        PERI
    }

    public String getName() {return "other_event";}
    public boolean isPre() {return era.equals(Era.PRE);}
    public boolean isPost() {return era.equals(Era.POST);}
    public void post() {Kisman.EVENT_BUS.post(this);}
}
