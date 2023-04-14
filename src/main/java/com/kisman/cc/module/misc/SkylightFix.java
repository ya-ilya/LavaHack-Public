package com.kisman.cc.module.misc;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;

public class SkylightFix extends Module {
    public static SkylightFix instance;

    public SkylightFix() {
        super("SkylightFix", Category.MISC);

        instance = this;
    }
}
