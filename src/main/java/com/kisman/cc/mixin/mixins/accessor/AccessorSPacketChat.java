package com.kisman.cc.mixin.mixins.accessor;

import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketChat.class)
public interface AccessorSPacketChat {
    @Accessor("chatComponent")
    void setChatComponent(ITextComponent chatComponent);
}
