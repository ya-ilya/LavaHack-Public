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
import com.kisman.cc.util.ColourUtilKt;
import org.lwjgl.input.Keyboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
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
        } catch (Exception ignored) {}
    }

    private static void loadFriends() throws IOException {
        if (!Files.exists(Paths.get(Kisman.fileName + Kisman.miscName + "friends.txt"))) return;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(Kisman.fileName + Kisman.miscName + "friends.txt"))))) {
            ArrayList<String> friends = new ArrayList<>();
            String inputLine;
            while ((inputLine = br.readLine()) != null) friends.add(inputLine);
            FriendManager.instance.setFriendsList(friends);
        }
    }

    private static void loadModules() throws IOException {
        for (Module module : Kisman.instance.moduleManager.modules) {
            boolean settings;

            try {
                if(Kisman.instance.settingManager.getSettingsByMod(module) == null) settings = false;
                else settings = !Kisman.instance.settingManager.getSettingsByMod(module).isEmpty();
                loadModuleDirect(Kisman.fileName + Kisman.moduleName, module, settings);
            } catch (IOException e) {
                System.out.println(module.getName());
                e.printStackTrace();
            }
        }
    }

    private static void loadModuleDirect(String moduleLocation, Module module, boolean settings)  throws IOException {
        if (!Files.exists(Paths.get(moduleLocation + module.getName() + ".json"))) return;

        InputStream inputStream = Files.newInputStream(Paths.get(moduleLocation + module.getName() + ".json"));
        JsonObject moduleObject;

        try {
            moduleObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
        } catch (java.lang.IllegalStateException e) {
            return;
        }

        if (moduleObject.get("Module") == null) return;

        JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();
        JsonElement keyObject = settingObject.get("key");

        if(settings) {
            for (Setting setting : Kisman.instance.settingManager.getSettingsByMod(module)) {
                JsonElement dataObject = settingObject.get(setting.getName());

                try {
                    if (dataObject != null && dataObject.isJsonPrimitive()) {
                        if (setting.isCheck()) setting.setValBoolean(dataObject.getAsBoolean());
                        if (setting.isCombo()) setting.setValString(dataObject.getAsString());
                        if (setting.isSlider()) setting.setValDouble(dataObject.getAsDouble());
                        if(setting.isColorPicker()) setting.setColour(ColourUtilKt.Companion.fromConfig(dataObject.getAsString(), setting.getColour()));
                        if(setting.isBind()) setting.setKey(dataObject.getAsInt());
                    }
                } catch (NumberFormatException e) {
                    System.out.println(setting.getName() + " " + module.getName());
                    System.out.println(dataObject);
                }
            }
        }

        if(keyObject != null && keyObject.isJsonPrimitive()) {
            module.setKey(Keyboard.getKeyIndex(keyObject.getAsString()));
        }

        inputStream.close();
    }

    private static void loadEnabledModules() throws IOException{
        String enabledLocation = Kisman.fileName + Kisman.mainName;

        if (!Files.exists(Paths.get(enabledLocation + "Toggle.json"))) return;

        InputStream inputStream = Files.newInputStream(Paths.get(enabledLocation + "Toggle.json"));
        JsonObject moduleObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

        if (moduleObject.get("Modules") == null) return;

        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();

        for(Module module : Kisman.instance.moduleManager.modules) {
            JsonElement dataObject = settingObject.get(module.getName());

            if(dataObject != null && dataObject.isJsonPrimitive()) {
                try {
                    if(!(module instanceof TeleportBack)) {
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
        String enabledLocation = Kisman.fileName + Kisman.mainName;

        if (!Files.exists(Paths.get(enabledLocation + "Visible" + ".json"))) return;

        InputStream inputStream = Files.newInputStream(Paths.get(enabledLocation + "Visible" + ".json"));
        JsonObject moduleObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

        if (moduleObject.get("Modules") == null) return;

        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();

        for(Module module : Kisman.instance.moduleManager.modules) {
            JsonElement dataObject = settingObject.get(module.getName());

            if(dataObject != null && dataObject.isJsonPrimitive()) {
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
        String enabledLocation = Kisman.fileName + Kisman.mainName;

        if (!Files.exists(Paths.get(enabledLocation + "HudToggle" + ".json"))) return;

        InputStream inputStream = Files.newInputStream(Paths.get(enabledLocation + "HudToggle" + ".json"));
        JsonObject moduleObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

        if (moduleObject.get("Modules") == null) return;

        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();

        for(HudModule module : Kisman.instance.hudModuleManager.modules) {
            JsonElement dataObject = settingObject.get(module.getName());

            if(dataObject != null && dataObject.isJsonPrimitive()) try {
                if(module.isToggled() != dataObject.getAsBoolean()) {
                    module.setToggled(dataObject.getAsBoolean());
                }
            } catch (Exception e) {e.printStackTrace();}
        }

        inputStream.close();
    }

    private static void loadBindModes() throws IOException {
        String enabledLocation = Kisman.fileName + Kisman.mainName;

        if (!Files.exists(Paths.get(enabledLocation + "BindModes" + ".json"))) return;

        InputStream inputStream = Files.newInputStream(Paths.get(enabledLocation + "BindModes" + ".json"));
        JsonObject moduleObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

        if (moduleObject.get("Modules") == null) return;

        JsonObject settingObject = moduleObject.get("Modules").getAsJsonObject();

        for(Module module : Kisman.instance.moduleManager.modules) {
            JsonElement dataObject = settingObject.get(module.getName());

            if(dataObject != null && dataObject.isJsonPrimitive()) {
                try {
                    module.hold = (dataObject.getAsBoolean());
                } catch (Exception e) {e.printStackTrace();}
            }
        }

        inputStream.close();
    }

    private static void loadHud() throws IOException {
        for(HudModule module : Kisman.instance.hudModuleManager.modules) loadHudDirect(module);
    }

    private static void loadHudDirect(HudModule module) throws IOException {
        if (!Files.exists(Paths.get(Kisman.fileName + Kisman.hudName + module.getName() + ".json"))) return;

        InputStream inputStream = Files.newInputStream(Paths.get(Kisman.fileName + Kisman.hudName + module.getName() + ".json"));
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
