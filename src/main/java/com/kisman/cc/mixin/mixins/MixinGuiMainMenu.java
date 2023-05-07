package com.kisman.cc.mixin.mixins;

import com.kisman.cc.Kisman;
import com.kisman.cc.gui.mainmenu.gui.KismanMainMenuGui;
import com.kisman.cc.gui.particle.ParticleSystem;
import com.kisman.cc.module.client.CustomMainMenu;
import com.kisman.cc.util.customfont.CustomFontUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = GuiMainMenu.class, priority = 10000)
public class MixinGuiMainMenu extends GuiScreen {
    private ParticleSystem particleSystem;
    private String customSplashSrt = "";

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGuiHook(CallbackInfo ci) {
        int j = this.height / 4 + 48;
        buttonList.add(new GuiButton(893, width / 2 - 100, j + 72 + 12 + 24, "LavaHack Public"));
        particleSystem = new ParticleSystem(300);
        customSplashSrt = getRandomCustomSplash();
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    public void actionPerformedHook(GuiButton p_actionPerformed_1_, CallbackInfo ci) {
        if (p_actionPerformed_1_.id == 893) mc.displayGuiScreen(new KismanMainMenuGui(this));
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void drawStringHook(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (CustomMainMenu.instance != null && CustomMainMenu.instance.toggled) {
            if (CustomMainMenu.instance.watermark.getValBoolean()) {
                CustomFontUtil.drawStringWithShadow(TextFormatting.WHITE + Kisman.getName() + " " + TextFormatting.GRAY + Kisman.getVersion(), 1, 1, -1);
                CustomFontUtil.drawStringWithShadow(TextFormatting.WHITE + "made by " + TextFormatting.GRAY + "_kisman_#5039", 1, CustomFontUtil.getFontHeight() + 2, -1);
            }
            if (CustomMainMenu.instance.particles.getValBoolean()) {
                particleSystem.tick(10);
                particleSystem.render();
            }
        }
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawCenteredString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"))
    private void drawScreenSplashHook(GuiMainMenu instance, FontRenderer fontRenderer, String s, int x, int y, int color) {
        if (CustomMainMenu.instance != null && CustomMainMenu.instance.toggled) {
            String customSplash = CustomMainMenu.instance.customSplashText.getValBoolean() ? customSplashSrt : s;
            if (CustomMainMenu.instance.customSplashFont.getValBoolean()) CustomFontUtil.drawCenteredStringWithShadow(customSplash, x, y, color);
            else instance.drawCenteredString(fontRenderer, customSplash, x, y, color);
        }
    }

    private static final String[] splashes = new String[] {
            "TheKisDevs on tope",
            "meowubic",
            "kisman.cc",
            "kisman.cc+",
            "kidman.club",
            "kisman.cc b0.1.6.1",
            "All of the best client lmao",
            "TheKisDevs inc",
            "lava_hack",
            "water??",
            "kidman own everyone",
            "u got token logget))",
            "sus user",
            "kisman > you",
            "ddev moment",
            "made by _kisman_#5039",
            "Where XuluPlus shaders??",
            "Future? No."
    };

    private static String getRandomCustomSplash() {
        Random rand = new Random();
        int i = (int) (splashes.length * rand.nextFloat());
        return splashes[i == splashes.length ? i - 1 : i];
    }
}
