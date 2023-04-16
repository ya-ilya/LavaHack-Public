package com.kisman.cc.module.chat;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;

public class Notification extends Module {
    public static Notification instance;

    public Notification() {
        super("Notification", Category.CHAT);

        instance = this;
    }
}
