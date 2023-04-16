package com.kisman.cc.module.render;

import com.kisman.cc.gui.csgo.components.Slider;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.setting.Setting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.awt.*;
import java.util.Arrays;

public class ItemCharms extends Module {
    public static ItemCharms instance;

    public final Setting glowESP = new Setting("GlowESP", this, "None", Arrays.asList("None", "Color", "Astral", "RainbowCube", "Gradient", "Aqua", "Circle", "Smoke"));

    public final Setting red = new Setting("ColorESPRed", this, 1, 0, 1, false);
    public final Setting green = new Setting("ColorESPGreen", this, 1, 0, 1, false);
    public final Setting blue = new Setting("ColorESPBlue", this, 1, 0, 1, false);
    public final Setting alpha = new Setting("ColorESPAlpha", this, 1, 0, 1, false);

    public final Setting radiusESP = new Setting("RadiusESP", this, 1, 0, 5, Slider.NumberType.DECIMAL);
    public final Setting qualityESP = new Setting("QualityESP", this, 1, 0, 20, Slider.NumberType.DECIMAL);
    public final Setting gradientAlpha = new Setting("GradientAlpha", this, false);
    public final Setting alphaOutline = new Setting("AlphaOutline", this, 255, 0, 255, Slider.NumberType.INTEGER).setVisible(() -> !gradientAlpha.getValBoolean());
    public final Setting piOutline = new Setting("PIOutline", this, 3.141592653, 0, 10, Slider.NumberType.DECIMAL).setVisible(() -> glowESP.getValString().equalsIgnoreCase("Circle"));
    public final Setting radOutline = new Setting("RADOutline", this, 0.75f, 0, 5, Slider.NumberType.DECIMAL).setVisible(() -> glowESP.getValString().equalsIgnoreCase("Circle"));


//    public final Setting exampleColor = new Setting("ExampleColor", this, red.getValFloat(), green.getValFloat(), blue.getValFloat(), alpha.getValFloat());

    public final Setting glintModify = new Setting("GlintModify", this, false);

    public Color color = new Color(red.getRed(), red.getGreen(), red.getBlue(), red.getAlpha());

    public ItemCharms() {
        super("ItemCharms", Category.RENDER);

        instance = this;

        register(red);
        register(green);
        register(blue);
        register(alpha);

        register(glintModify);
    }

    public void onDisable() {
        color = new Color(255, 255,255);
    }

    public void update() {
//        exampleColor.updateColor(red.getValFloat(), green.getValFloat(), blue.getValFloat(), alpha.getValFloat());
    }

//    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableAlpha();
/*        ITEM_SHADER.blur = module.blur.getValue();
        ITEM_SHADER.mix = module.mix.getValue();
        ITEM_SHADER.alpha = module.chamColor.getValue().getAlpha() / 255.0f;
        ITEM_SHADER.imageMix = module.imageMix.getValue();
        ITEM_SHADER.useImage = module.useImage.getValue();
        ITEM_SHADER.startDraw(mc.getRenderPartialTicks());
        module.forceRender = true;
        ((IEntityRenderer) mc.entityRenderer).invokeRenderHand(mc.getRenderPartialTicks(), 2);
        module.forceRender = false;
        ITEM_SHADER.stopDraw(module.chamColor.getValue(), module.radius.getValue(), 1.0f);*/
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}
