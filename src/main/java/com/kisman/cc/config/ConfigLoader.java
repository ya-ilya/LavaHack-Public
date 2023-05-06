package com.kisman.cc.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kisman.cc.Kisman;
import com.kisman.cc.gui.hud.hudmodule.HudModule;
import com.kisman.cc.manager.managers.FriendManager;
import com.kisman.cc.module.Module;
import com.kisman.cc.module.player.TeleportBack;
import com.kisman.cc.setting.Setting;
import com.kisman.cc.util.ColourUtil;
import org.lwjgl.input.Keyboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ConfigLoader {
    public static void init() {
        try {
            Kisman.initDirs();
            loadModules();
            loadEnabledModules();
            loadVisibledModules();
            loadEnabledHudModules();
            loadBindModes();
            loadFriends();
            loadHud();
        } catch (Exception ignored) {}
    }

    private static void loadFriends() throws IOException {
        Path friendsPath = Paths.get(Kisman.fileName + Kisman.miscName + "friends.txt");

        if (!Files.exists(friendsPath)) return;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(friendsPath)))) {
            ArrayList<String> friends = new ArrayList<>();
            String inputLine;
            while ((inputLine = br.readLine()) != null) friends.add(inputLine);
            FriendManager.instance.setFriendsList(friends);
        }
    }

    private static void loadModules() {
        for (Module module : Kisman.instance.moduleManager.modules) {
            boolean settings;

            try {
                if (Kisman.instance.settingManager.getSettingsByMod(module) == null) settings = false;
                else settings = !Kisman.instance.settingManager.getSettingsByMod(module).isEmpty();
                loadModuleDirect(module, settings);
            } catch (IOException e) {
                System.out.println(module.getName());
                e.printStackTrace();
            }
        }
    }

    private static void loadModuleDirect(Module module, boolean settings)  throws IOException {
        Path modulesPath = Paths.get(Kisman.fileName + Kisman.moduleName + module.getName() + ".json");

        if (!Files.exists(modulesPath)) return;

        InputStream inputStream = Files.newInputStream(modulesPath);
        JsonObject moduleObject;

        try {
            moduleObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
        } catch (java.lang.IllegalStateException e) {
            return;
        }

        if (moduleObject.get("Module") == null) return;

        JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();
        JsonElement keyObject = settingObject.get("key");

        if (settings) {
            for (Setting setting : Kisman.instance.settingManager.getSettingsByMod(module)) {
                JsonElement dataObject = settingObject.get(setting.getName());

                try {
                    if (dataObject != null && dataObject.isJsonPrimitive()) {
                        if (setting.isCheck()) setting.setValBoolean(dataObject.getAsBoolean());
                        if (setting.isCombo()) setting.setValString(dataObject.getAsString());
                        if (setting.isSlider()) setting.setValDouble(dataObject.getAsDouble());
                        if (setting.isColorPicker()) setting.setColour(ColourUtil.Companion.fromConfig(dataObject.getAsString(), setting.getColour()));
                        if (setting.isBind()) setting.setKey(dataObject.getAsInt());
                    }
                } catch (NumberFormatException e) {
                    System.out.println(setting.getName() + " " + module.getName());
                    System.out.println(dataObject);
                }
            }
        }

        if (keyObject != null && keyObject.isJsonPrimitive()) {
            module.setKey(Keyboard.getKeyIndex(keyObject.getAsString()));
        }

        inputStream.close();
    }

    private static void loadEnabledModules() throws IOException{
        Path enabledPath = Paths.get(Kisman.fileName + Kisman.mainName + "Toggle.json");

        if (!Files.exists(enabledPath)) return;

        InputStream inputStream = Files.newInputStream(enabledPath);
        JsonObject moduleObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

        if (moduleObject.get("Modules") == null) return;

        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();

        for (Module module : Kisman.instance.moduleManager.modules) {
            JsonElement dataObject = settingObject.get(module.getName());

            if (dataObject != null && dataObject.isJsonPrimitive()) {
                try {
                    if (!(module instanceof TeleportBack)) {
                        module.setToggled(dataObject.getAsBoolean());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        inputStream.close();
    }

    private static void loadVisibledModules() throws IOException {
        Path visiblePath = Paths.get(Kisman.fileName + Kisman.mainName + "Visible.json");

        if (!Files.exists(visiblePath)) return;

        InputStream inputStream = Files.newInputStream(visiblePath);
        JsonObject moduleObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

        if (moduleObject.get("Modules") == null) return;

        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();

        for (Module module : Kisman.instance.moduleManager.modules) {
            JsonElement dataObject = settingObject.get(module.getName());

            if (dataObject != null && dataObject.isJsonPrimitive()) {
                try {
                    module.visible = dataObject.getAsBoolean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        inputStream.close();
    }

    private static void loadEnabledHudModules() throws IOException {
        Path hudEnabledPath = Paths.get(Kisman.fileName + Kisman.mainName + "HudToggle.json");

        if (!Files.exists(hudEnabledPath)) return;

        InputStream inputStream = Files.newInputStream(hudEnabledPath);
        JsonObject moduleObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

        if (moduleObject.get("Modules") == null) return;

        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();

        for (HudModule module : Kisman.instance.hudModuleManager.modules) {
            JsonElement dataObject = settingObject.get(module.getName());

            if (dataObject != null && dataObject.isJsonPrimitive()) try {
                if (module.isToggled() != dataObject.getAsBoolean()) {
                    module.setToggled(dataObject.getAsBoolean());
                }
            } catch (Exception e) {e.printStackTrace();}
        }

        inputStream.close();
    }

    private static void loadBindModes() throws IOException {
        Path bindModes = Paths.get(Kisman.fileName + Kisman.mainName + "BindModes.json");

        if (!Files.exists(bindModes)) return;

        InputStream inputStream = Files.newInputStream(bindModes);
        JsonObject moduleObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

        if (moduleObject.get("Modules") == null) return;

        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();

        for (Module module : Kisman.instance.moduleManager.modules) {
            JsonElement dataObject = settingObject.get(module.getName());

            if (dataObject != null && dataObject.isJsonPrimitive()) {
                try {
                    module.hold = (dataObject.getAsBoolean());
                } catch (Exception e) {e.printStackTrace();}
            }
        }

        inputStream.close();
    }

    private static void loadHud() throws IOException {
        for (HudModule module : Kisman.instance.hudModuleManager.modules) loadHudDirect(module);
    }

    private static void loadHudDirect(HudModule module) throws IOException {
        Path hudModulePath = Paths.get(Kisman.fileName + Kisman.hudName + module.getName() + ".json");

        if (!Files.exists(hudModulePath)) return;

        InputStream inputStream = Files.newInputStream(hudModulePath);
        JsonObject moduleObject;

        try {
            moduleObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
        } catch (java.lang.IllegalStateException e) {
            return;
        }

        if (moduleObject.get("Pos") == null) return;

        JsonObject posObject = moduleObject.get("Pos").getAsJsonObject();
        module.setX(posObject.get("x").getAsDouble());
        module.setY(posObject.get("y").getAsDouble());

        inputStream.close();
    }
}
