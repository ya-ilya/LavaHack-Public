package com.kisman.cc.module.render;

import com.kisman.cc.gui.csgo.components.Slider;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.module.render.shader.shaders.ItemShader;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.Render2DUtil;
import com.kisman.cc.util.render.objects.AbstractGradient;
import com.kisman.cc.util.render.objects.Vec4d;
import i.gishreloaded.gishcode.utils.visual.ColorUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class FramebufferTest extends Module {
    private final Setting degrees = new Setting("Degrees", this, 200, 0, 360, true);
    private final Setting radius = new Setting("Radius", this, 2, 0.1f, 10, false);
    private final Setting mix = new Setting("Mix", this, 1, 0, 1, false);
    private final Setting red = new Setting("Red", this, 1, 0, 1, false);
    private final Setting green = new Setting("Green", this, 1, 0, 1, false);
    private final Setting blue = new Setting("Blue", this, 1, 0, 1, false);
    private final Setting rainbow = new Setting("RainBow", this, true);
    private final Setting delay = new Setting("Delay", this, 100, 1, 2000, true);
    private final Setting saturation = new Setting("Saturation", this, 36, 0, 100, Slider.NumberType.PERCENT);
    private final Setting brightness = new Setting("Brightness", this, 100, 0, 100, Slider.NumberType.PERCENT);

    private ItemShader shader;

    public FramebufferTest() {
        super("FramebufferTest", Category.RENDER);

        shader = ItemShader.ITEM_SHADER;

        register(degrees);
        register(radius);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        ScaledResolution sr = event.getResolution();
        Render2DUtil.drawProgressCircle(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 10, Color.GREEN, 3f, degrees.getValDouble(), (int) 360);

        Render2DUtil.drawProgressCircle(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 + 30, 10, Color.GREEN, 3f, degrees.getValDouble(), (int) 4);

        Render2DUtil.drawAbstract(new AbstractGradient(new Vec4d(new double[]{200, 200}, new double[]{300, 200}, new double[]{300, 300}, new double[]{200, 300}), Color.RED, new Color(0, 0, 0), false));

//        Render2DUtil.drawAbstract(new ObjectWithGlow(new Vec4d(new double[]{200, 200}, new double[]{300, 200}, new double[]{300, 300}, new double[]{200, 300}), Color.RED, radius.getValFloat()));

    }

    private Color getColor() {return rainbow.getValBoolean() ? ColorUtils.rainbowRGB(delay.getValInt(), saturation.getValFloat(), brightness.getValFloat()) : new Color(red.getValFloat(), green.getValFloat(), blue.getValFloat());}
}
