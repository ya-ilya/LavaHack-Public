package com.kisman.cc.module.render.shader.shaders;

import com.kisman.cc.module.render.shader.FramebufferShader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

import java.util.HashMap;
import java.util.Map;

public class ResolutionAndTimeShader extends FramebufferShader {
    public static Map<String, ResolutionAndTimeShader> SHADERS = new HashMap<>();

    public static void initShaders() {
        shader("aqua", 0.05f);
        shader("blueflames", 0.01f);
        shader("codex", 0.01f);
        shader("crazy", 0.01f);
        shader("flow", 0.05f);
        shader("gamer", 0.03f);
        shader("golden", 0.01f);
        shader("hidef", 0.05f);
        shader("holyfuck", 0.01f);
        shader("hotshit", 0.005f);
        shader("kfc", 0.01f);
        shader("purple", 0.05f);
        shader("red", 0.05f);
        shader("sheldon", 0.001f);
        shader("smoke", 0.05f);
        shader("smoky", 0.001f);
        shader("snow", 0.01f);
        shader("techno", 0.01f);
        shader("unu", 0.05f);
    }

    private static void shader(String name, float timeMult) {
        SHADERS.put(name, new ResolutionAndTimeShader(name.toLowerCase() + ".frag", timeMult));
    }

    public float time;
    public float timeMult;

    public ResolutionAndTimeShader(String fragmentShader, float timeMult) {
        super(fragmentShader);

        this.timeMult = timeMult;
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform2f(this.getUniform("resolution"), (float)new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth(), (float)new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight());
        GL20.glUniform1f(this.getUniform("time"), this.time);
        time += timeMult * animationSpeed;
    }
}
