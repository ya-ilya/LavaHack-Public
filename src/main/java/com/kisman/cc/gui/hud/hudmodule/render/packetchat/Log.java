package com.kisman.cc.gui.hud.hudmodule.render.packetchat;

import java.util.ArrayList;
import java.util.Iterator;

public class Log {

    public ArrayList<Message> activeMessages = new ArrayList<>();
    public ArrayList<Message> passiveMessages = new ArrayList<>();
    public Iterator<Message> messageIterator;
}
