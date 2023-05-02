package com.kisman.cc.mixin.mixins.accessor;

import com.google.common.collect.Ordering;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiPlayerTabOverlay.class)
public interface AccessorGuiPlayerTabOverlay {
    @Accessor("ENTRY_ORDERING")
    static Ordering<NetworkPlayerInfo> getEntryOrdering() {
        throw new AssertionError();
    }
}
