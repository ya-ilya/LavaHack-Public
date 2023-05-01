package com.kisman.cc.util.enums;

import com.kisman.cc.Kisman;
import com.kisman.cc.util.Colour;
import com.kisman.cc.util.gish.ColorUtil;

import java.awt.*;

public enum ColourEnum {
        Rainbow(Colour.COLOR_RAINBOW),
        Astolfo(Colour.COLOR_ASTOLFO),
        Static(Colour.COLOR_PRIMARY);

        public final int mode;
        public int delay = 1;
        public int offset = 100;
        public final Colour primary = new Colour(Color.RED);
        
        ColourEnum(int mode) {
            this.mode = mode;
        }

        public Colour getColour() {
                if(mode == Colour.COLOR_RAINBOW) return new Colour(ColorUtil.rainbow(delay, offset));
                if(mode == Colour.COLOR_ASTOLFO) return Kisman.canUseImplAstolfo() ? new Colour(ColorUtil.getAstolfoRainbow(offset)) : new Colour(ColorUtil.astolfoColors(offset, offset));
                return primary;
        }
}