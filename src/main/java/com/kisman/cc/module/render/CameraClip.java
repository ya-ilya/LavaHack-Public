package com.kisman.cc.module.render;

import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;

public class CameraClip extends Module {
    public static CameraClip instance;

    public CameraClip() {
        super("CameraClip", Category.RENDER);

        instance = this;
    }
}
