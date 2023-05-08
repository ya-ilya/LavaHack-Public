package com.kisman.cc.mixin.mixins.accessor;

import net.minecraft.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Session.class)
public interface AccessorSession {
	@Accessor("username")
	void setUsername(String username);
}
