package com.kisman.cc.module.misc;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;

public class ItemScroller extends Module {
    public static ItemScroller instance;

    public final Setting scrollSpeed = new Setting("ScrollSpeed", this, 20, 1, 100, true);

    public ItemScroller() {
        super("ItemScroller", "", Category.MISC);

        instance = this;

        register(scrollSpeed);
    }
}
