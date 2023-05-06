package com.kisman.cc.module.render;

import com.kisman.cc.event.events.RenderEntityEvent;
import com.kisman.cc.gui.csgo.components.Slider;
import com.kisman.cc.manager.managers.FriendManager;
import com.kisman.cc.mixin.mixins.accessor.AccessorShaderGroup;
import com.kisman.cc.module.Category;
import com.kisman.cc.module.Module;
import com.kisman.cc.module.client.Config;
import com.kisman.cc.module.render.shader.FramebufferShader;
import com.kisman.cc.module.render.shader.ShaderUtil;
import com.kisman.cc.module.render.shader.shaders.*;
import com.kisman.cc.module.render.shader.shaders.troll.ShaderHelper;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.ChatUtil;
import com.kisman.cc.util.MathUtil;
import com.kisman.cc.util.enums.ShaderModes;
import com.kisman.cc.util.gish.ColorUtil;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Objects;

public class ShaderCharms extends Module {
    private final Setting range = new Setting("Range", this, 32, 8, 64, true);
    public final Setting mode = new Setting("Mode", this, ShaderModes.SMOKE);
    private final Setting crystals = new Setting("Crystals", this, true);
    private final Setting players = new Setting("Players", this, false);
    private final Setting friends = new Setting("Friends", this, true);
    private final Setting mobs = new Setting("Mobs", this, false);
    private final Setting animals = new Setting("Animals", this, false);
    private final Setting enderPearls = new Setting("Ender Pearls", this, false);
    private final Setting itemsEntity = new Setting("Items(Entity)", this, false);
    public final Setting items = new Setting("Items", this, true);
    private final Setting itemsFix = new Setting("Items Fix", this, false).setVisible(items::getValBoolean);
//    private final Setting storages = new Setting("Storages(cfg from StorageEsp)", this, false);

    private final Setting animationSpeed = new Setting("Animation Speed", this, 0, 1, 10, true).setVisible(() -> !mode.checkValString("GRADIENT"));

    private final Setting blur = new Setting("Blur", this, true).setVisible(() -> mode.checkValString("ITEMGLOW"));
    private final Setting radius = new Setting("Radius", this, 2, 0.1f, 10, false).setVisible(() -> mode.checkValString("ITEMGLOW") || mode.checkValString("GLOW") || mode.checkValString("OUTLINE") || mode.checkValString("GRADIENT") || mode.checkValString("Outline2"));
    private final Setting mix = new Setting("Mix", this, 1, 0, 1, false).setVisible(() -> mode.checkValString("ITEMGLOW") || mode.checkValString("Outline2"));
    private final Setting red = new Setting("Red", this, 1, 0, 1, false).setVisible(() -> mode.checkValString("ITEMGLOW") || mode.checkValString("GLOW") || mode.checkValString("OUTLINE") || mode.checkValString("Outline2"));
    private final Setting green = new Setting("Green", this, 1, 0, 1, false).setVisible(() -> mode.checkValString("ITEMGLOW") || mode.checkValString("GLOW") || mode.checkValString("OUTLINE") || mode.checkValString("Outline2"));
    private final Setting blue = new Setting("Blue", this, 1, 0, 1, false).setVisible(() -> mode.checkValString("ITEMGLOW") || mode.checkValString("GLOW") || mode.checkValString("OUTLINE") || mode.checkValString("Outline2"));
    private final Setting rainbow = new Setting("RainBow", this, true).setVisible(() -> mode.checkValString("ITEMGLOW") || mode.checkValString("GLOW") || mode.checkValString("OUTLINE") || mode.checkValString("Outline2"));
    private final Setting delay = new Setting("Delay", this, 100, 1, 2000, true);
    private final Setting saturation = new Setting("Saturation", this, 36, 0, 100, Slider.NumberType.PERCENT);
    private final Setting brightness = new Setting("Brightness", this, 100, 0, 100, Slider.NumberType.PERCENT);

    private final Setting quality = new Setting("Quality", this, 1, 0, 20, false).setVisible(() -> mode.checkValString("GRADIENT") || mode.checkValString("ITEMGLOW") || mode.checkValString("GLOW") || mode.checkValString("OUTLINE"));
    private final Setting gradientAlpha = new Setting("Gradient Alpha", this, false).setVisible(() -> mode.checkValString("GRADIENT"));
    private final Setting alphaGradient = new Setting("Alpha Gradient Value", this, 255, 0, 255, true).setVisible(() -> mode.checkValString("GRADIENT"));
    private final Setting duplicateOutline = new Setting("Duplicate Outline", this, 1, 0, 20, false).setVisible(() -> mode.checkValString("GRADIENT"));
    private final Setting moreGradientOutline = new Setting("More Gradient", this, 1, 0, 10, false).setVisible(() -> mode.checkValString("GRADIENT"));
    private final Setting creepyOutline = new Setting("Creepy", this, 1, 0, 20, false).setVisible(() -> mode.checkValString("GRADIENT"));
    private final Setting alpha = new Setting("Alpha", this, 1, 0, 1, false).setVisible(() -> mode.checkValString("GRADIENT"));
    private final Setting numOctavesOutline = new Setting("Num Octaves", this, 5, 1, 30, true).setVisible(() -> mode.checkValString("GRADIENT"));
    private final Setting speedOutline = new Setting("Speed", this, 0.1, 0.001, 0.1, false).setVisible(() -> mode.checkValString("GRADIENT"));

    private final Setting hideOriginal = new Setting("Hide Original", this, false).setVisible(() -> mode.checkValString("Outline2"));
    private final Setting outlineAlpha = new Setting("Outline Alpha", this, 1, 0, 1, false).setVisible(() -> mode.checkValString("Outline2"));
    private final Setting filledAlpha = new Setting("Filled Alpha", this, (63f / 255f), 0, 1, false).setVisible(() -> mode.checkValString("Outline2"));
    private final Setting width = new Setting("Width", this, 2, 1, 8, false).setVisible(() -> mode.checkValString("Outline2"));
    private final Setting ratio = new Setting("Ratio", this, 0.5, 0,1, false).setVisible(() -> mode.checkValString("Outline2"));

    public static ShaderCharms instance;

    private boolean criticalSection = false;

    private final ShaderHelper shaderHelper = new ShaderHelper(new ResourceLocation("shaders/post/esp_outline.json"));
    private final Framebuffer frameBufferFinal = shaderHelper.getFrameBuffer("final");

    public ShaderCharms() {
        super("ShaderCharms", Category.RENDER);

        instance = this;

        register(range);

        register(mode);
        register(crystals);
        register(players);
        register(friends);
        register(mobs);
        register(animals);
        register(enderPearls);
        register(itemsEntity);
        register(items);
        register(itemsFix);
//      register(storages);

        register(animationSpeed);

        register(blur);
        register(radius);
        register(mix);
        register(red);
        register(green);
        register(blue);
        register(rainbow);
        register(delay);
        register(saturation);
        register(brightness);

        register(quality);
        register(gradientAlpha);
        register(alphaGradient);
        register(duplicateOutline);
        register(moreGradientOutline);
        register(creepyOutline);
        register(alpha);
        register(numOctavesOutline);
        register(speedOutline);

        register(hideOriginal);
        register(outlineAlpha);
        register(filledAlpha);
        register(width);
        register(ratio);
    }

    public void update() {
        super.setDisplayInfo("[" + mode.getValString() + "]");
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        if (items.getValBoolean() && itemsFix.getValBoolean() && (!criticalSection || (mode.checkValString("Outline2") && hideOriginal.getValBoolean()))) event.setCanceled(true);
    }

    @SubscribeEvent
    public void onFog(EntityViewRenderEvent.FogColors event) {
        ((AccessorShaderGroup) Objects.requireNonNull(shaderHelper.getShader())).getListFramebuffers().forEach(framebuffer -> framebuffer.setFramebufferColor(event.getRed(), event.getGreen(), event.getBlue(), 0));
    }

    @EventHandler
    @SuppressWarnings("unused")
    private final Listener<RenderEntityEvent.All.Pre> renderEntityListener = listener(event -> {
        if (mode.checkValString("Outline2") && !mc.renderManager.renderOutlines && hideOriginal.getValBoolean() && mc.player.getDistance(event.getEntity()) <= range.getValFloat() && entityTypeCheck(event.getEntity())) event.cancel();
    });

    public boolean entityTypeCheck(Entity entity) {
        return entity != mc.player && ((entity instanceof EntityPlayer && players.getValBoolean())
                || (entity instanceof EntityPlayer && friends.getValBoolean() && FriendManager.instance.isFriend(entity.getName()))
                || (entity instanceof EntityEnderCrystal && crystals.getValBoolean())
                || ((entity instanceof EntityMob || entity instanceof EntitySlime) && mobs.getValBoolean())
                || ((entity instanceof EntityEnderPearl) && enderPearls.getValBoolean())
                || ((entity instanceof EntityItem) && itemsEntity.getValBoolean())
                || (entity instanceof EntityAnimal && animals.getValBoolean()));
    }

    private void outline2Shader(float particalTicks) {
        if (frameBufferFinal == null) return;
        Outline2Shader.INSTANCE.setupUniforms(outlineAlpha.getValFloat(), filledAlpha.getValFloat(), width.getValFloat(), (float) ((width.getAlpha() - 1.0f) * (Math.pow(ratio.getValFloat(), 3)) + 1.0f));
        Outline2Shader.INSTANCE.updateUniforms(shaderHelper);
        ShaderUtil.Companion.clearFrameBuffer(frameBufferFinal);
        Outline2Shader.INSTANCE.drawEntities(particalTicks, range.getValFloat());
        Outline2Shader.INSTANCE.drawShader(shaderHelper, frameBufferFinal, particalTicks);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        try {
            if (mode.checkValString("Outline2")) outline2Shader(event.getPartialTicks());
            else {
                FramebufferShader framebufferShader = FramebufferShader.SHADERS.get(mode.getValString().toLowerCase());

                if (framebufferShader == null) return;

                framebufferShader.animationSpeed = animationSpeed.getValInt();

                GlStateManager.matrixMode(5889);
                GlStateManager.pushMatrix();
                GlStateManager.matrixMode(5888);
                GlStateManager.pushMatrix();
                
                if (framebufferShader instanceof ItemShader) {
                    ItemShader itemShader = (ItemShader) framebufferShader;
                    itemShader.red = getColor().getRed() / 255f;
                    itemShader.green = getColor().getGreen() / 255f;
                    itemShader.blue = getColor().getBlue() / 255f;
                    itemShader.radius = radius.getValFloat();
                    itemShader.quality = quality.getValFloat();
                    itemShader.blur = blur.getValBoolean();
                    itemShader.mix = mix.getValFloat();
                    itemShader.alpha = 1f;
                    itemShader.useImage = false;
                } else if (framebufferShader instanceof GradientOutlineShader) {
                    GradientOutlineShader gradientShader = (GradientOutlineShader) framebufferShader;
                    gradientShader.color = getColor();
                    gradientShader.radius = radius.getValFloat();
                    gradientShader.quality = quality.getValFloat();
                    gradientShader.gradientAlpha = gradientAlpha.getValBoolean();
                    gradientShader.alphaOutline = alphaGradient.getValInt();
                    gradientShader.duplicate = duplicateOutline.getValFloat();
                    gradientShader.moreGradient = moreGradientOutline.getValFloat();
                    gradientShader.creepy = creepyOutline.getValFloat();
                    gradientShader.alpha = alpha.getValFloat();
                    gradientShader.numOctaves = numOctavesOutline.getValInt();
                } else if (framebufferShader instanceof GlowShader) {
                    GlowShader glowShader = (GlowShader) framebufferShader;
                    glowShader.red = getColor().getRed() / 255f;
                    glowShader.green = getColor().getGreen() / 255f;
                    glowShader.blue = getColor().getBlue() / 255f;
                    glowShader.radius = radius.getValFloat();
                    glowShader.quality = quality.getValFloat();
                } else if (framebufferShader instanceof OutlineShader) {
                    OutlineShader outlineShader = (OutlineShader) framebufferShader;
                    outlineShader.red = getColor().getRed() / 255f;
                    outlineShader.green = getColor().getGreen() / 255f;
                    outlineShader.blue = getColor().getBlue() / 255f;
                    outlineShader.radius = radius.getValFloat();
                    outlineShader.quality = quality.getValFloat();
                }

                criticalSection = true;
                framebufferShader.startDraw(event.getPartialTicks());
                for (Entity entity : mc.world.loadedEntityList) {
                    if (entity == mc.player || entity == mc.getRenderViewEntity()) continue;
                    if (!((entity instanceof EntityPlayer && players.getValBoolean())
                            || (entity instanceof EntityPlayer && friends.getValBoolean() && FriendManager.instance.isFriend(entity.getName()))
                            || (entity instanceof EntityEnderCrystal && crystals.getValBoolean())
                            || ((entity instanceof EntityMob || entity instanceof EntitySlime) && mobs.getValBoolean())
                            || ((entity instanceof EntityEnderPearl) && enderPearls.getValBoolean())
                            || ((entity instanceof EntityItem) && itemsEntity.getValBoolean())
                            || (entity instanceof EntityAnimal && animals.getValBoolean()))) continue;
                    Vec3d vector = MathUtil.getInterpolatedRenderPos(entity, event.getPartialTicks());
                    Objects.requireNonNull(mc.getRenderManager().getEntityRenderObject(entity)).doRender(entity, vector.x, vector.y, vector.z, entity.rotationYaw, event.getPartialTicks());
                }
                mc.entityRenderer.renderHand(event.getPartialTicks(), 2);
                framebufferShader.stopDraw();
                criticalSection = false;
                if (framebufferShader instanceof GradientOutlineShader) {
                    ((GradientOutlineShader) framebufferShader).update(speedOutline.getValDouble());
                }
                GlStateManager.color(1f, 1f, 1f);
                GlStateManager.matrixMode(5889);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
                GlStateManager.popMatrix();
            }
        } catch (Exception ignored) {
            if (Config.instance.antiOpenGLCrash.getValBoolean()) {
                super.setToggled(false);
                ChatUtil.error("[ShaderCharms] Error, Config -> AntiOpenGLCrash disabled ShaderCharms");
            }
        }
    }

    public Color getColor() {
        return rainbow.getValBoolean() ? ColorUtil.rainbowRGB(delay.getValInt(), saturation.getValFloat(), brightness.getValFloat()) : new Color(red.getValFloat(), green.getValFloat(), blue.getValFloat());
    }
}
