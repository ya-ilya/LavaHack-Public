package com.kisman.cc.mixin.mixins;

import com.kisman.cc.module.misc.BetterScreenshot;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

@Mixin(ScreenShotHelper.class)
public class MixinScreenshotHelper {
    @Shadow
    public static ITextComponent saveScreenshot(File gameDirectory, @Nullable String screenshotName, int width, int height, Framebuffer buffer) {return null;}

    /**
     * @author BloomWareClient
     * @reason .
     */
    @Overwrite
    public static ITextComponent saveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer) {
        ITextComponent screenshot = saveScreenshot(gameDirectory, null, width, height, buffer);
        if (BetterScreenshot.instance != null && BetterScreenshot.instance.isToggled()) {
            try {
                BetterScreenshot.copyToClipboard(BetterScreenshot.getLatestScreenshot());
            } catch (IOException ignored) {}
        }
        return screenshot;
    }
}
