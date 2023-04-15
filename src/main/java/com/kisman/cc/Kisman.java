package com.kisman.cc;

import com.kisman.cc.api.cape.CapeAPI;
import com.kisman.cc.catlua.ScriptManager;
import com.kisman.cc.catlua.lua.utils.LuaRotation;
import com.kisman.cc.catlua.mapping.ExcludedList;
import com.kisman.cc.catlua.mapping.ForgeMappings;
import com.kisman.cc.catlua.mapping.Remapper3000;
import com.kisman.cc.command.CommandManager;
import com.kisman.cc.gui.console.GuiConsole;
import com.kisman.cc.gui.console.rewrite.ConsoleGui;
import com.kisman.cc.event.EventProcessor;
import com.kisman.cc.event.EventProcessorLua;
import com.kisman.cc.file.ConfigManager;
import com.kisman.cc.friend.FriendManager;
import com.kisman.cc.gui.MainGui;
import com.kisman.cc.gui.csgo.ClickGuiNew;
import com.kisman.cc.gui.halq.Frame;
import com.kisman.cc.gui.halq.HalqGui;
import com.kisman.cc.gui.mainmenu.sandbox.SandBoxShaders;
import com.kisman.cc.gui.vega.Gui;
import com.kisman.cc.hud.hudeditor.HudEditorGui;
import com.kisman.cc.hud.hudgui.HudGui;
import com.kisman.cc.hud.hudmodule.HudModule;
import com.kisman.cc.hud.hudmodule.HudModuleManager;
import com.kisman.cc.module.Module;
import com.kisman.cc.module.ModuleManager;
import com.kisman.cc.module.client.Config;
import com.kisman.cc.settings.SettingsManager;
import com.kisman.cc.util.RotationUtils;
import com.kisman.cc.util.ServerManager;
import com.kisman.cc.util.VectorUtils;
import com.kisman.cc.util.customfont.CustomFontRenderer;
import com.kisman.cc.util.glow.ShaderShell;
import com.kisman.cc.util.manager.Managers;
import com.kisman.cc.util.optimization.aiimpr.MainAiImpr;
import com.kisman.cc.util.shaders.Shaders;
import me.zero.alpine.bus.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Mod(modid = Kisman.MOD_ID, name = Kisman.NAME, version = Kisman.VERSION)
public class Kisman {
    public static final String NAME = "LavaHack Public";
    public static final String MOD_ID = "kisman";
    public static final String VERSION = "b0.1.4";
    public static final String fileName = "lavahack-public/";
    public static final String moduleName = "Modules/";
    public static final String hudName = "Hud/";
    public static final String mainName = "Main/";
    public static final String miscName = "Misc/";
    public static final String luaName = "Lua/";
    public static final String mappingName = "Mapping/";

    public static Kisman instance = new Kisman();
    public static final EventManager EVENT_BUS = new EventManager();
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public static boolean allowToConfiguredAnotherClients = true, remapped = false;
    public static boolean canUseImprAstolfo = false;
    public static boolean canInitializeCatLua = true;

    public boolean init = false;

    private static Minecraft mc;

    public VectorUtils vectorUtils;

    public ModuleManager moduleManager;
    public FriendManager friendManager;
    public HudModuleManager hudModuleManager;
    public SettingsManager settingsManager;
    public ClickGuiNew clickGuiNew;
    public GuiConsole guiConsole;
    public ConsoleGui consoleGui;
    public HudGui hudGui;
    public HudEditorGui hudEditorGui;
    public Gui gui;
    public HalqGui halqGui;
    public MainGui.SelectionBar selectionBar;
    public CustomFontRenderer customFontRenderer;
    public CustomFontRenderer customFontRenderer1;
    public CommandManager commandManager;
    public RPC discord;
    public RotationUtils rotationUtils;
    public EventProcessor eventProcessor;
    public ServerManager serverManager;
    public Shaders shaders;
    public SandBoxShaders sandBoxShaders;
    public Managers managers;
    public CapeAPI capeAPI;
    public MainAiImpr aiImpr;
    public EventProcessorLua eventProcessorLua;
    public ExcludedList excludedList;
    public Remapper3000 remapper3000;
    public ForgeMappings forgeMappings;
    public LuaRotation luaRotation;
    public ScriptManager scriptManager;
    public ConfigManager configManager;

    public void init() throws IOException, NoSuchFieldException, IllegalAccessException {
        try {
            Minecraft.class.getDeclaredField("player");
        } catch(Exception e) {
            remapped = true;
        }

        Display.setTitle(NAME + " | " + VERSION);
    	MinecraftForge.EVENT_BUS.register(this);

        aiImpr = new MainAiImpr();

        eventProcessor = new EventProcessor();

        mc = Minecraft.getMinecraft();

        managers = new Managers();
        managers.init();

        vectorUtils = new VectorUtils();

        friendManager = new FriendManager();
    	settingsManager = new SettingsManager();
    	moduleManager = new ModuleManager();
        hudModuleManager = new HudModuleManager();
        clickGuiNew = new ClickGuiNew();
        guiConsole = new GuiConsole();
        consoleGui = new ConsoleGui();
        customFontRenderer = new CustomFontRenderer(new Font("Verdana", Font.PLAIN, 18), true, true);
        customFontRenderer1 = new CustomFontRenderer(new Font("Verdana", Font.PLAIN, 15), true, true);
        commandManager = new CommandManager();
        discord = new RPC();
        rotationUtils = new RotationUtils();
        serverManager = new ServerManager();
        shaders = new Shaders();
        sandBoxShaders = new SandBoxShaders();
        capeAPI = new CapeAPI();

        configManager = new ConfigManager("config");
        configManager.getLoader().init();

        //load glow shader
        ShaderShell.init();

        //catlua
        eventProcessorLua = new EventProcessorLua();
        excludedList = new ExcludedList();
        remapper3000 = new Remapper3000();
        remapper3000.init();
        luaRotation = new LuaRotation();
        scriptManager = new ScriptManager();

        //gui's
        clickGuiNew = new ClickGuiNew();
        hudGui = new HudGui();
        hudEditorGui = new HudEditorGui();
        gui = new Gui();
        halqGui = new HalqGui();

        selectionBar = new MainGui.SelectionBar(MainGui.Guis.ClickGui);

        init = true;
    }
    
    @SubscribeEvent
    public void key(KeyInputEvent e) {
    	if (mc.world == null || mc.player == null) return;
    	try {
            if (Keyboard.isCreated()) {
                if (Keyboard.getEventKeyState()) {
                    int keyCode = Keyboard.getEventKey();
                    if (keyCode <= 1) return;
                    for (Module m : moduleManager.modules) if (m.getKey() == keyCode) m.toggle();
                    for (HudModule m : hudModuleManager.modules) if (m.getKey() == keyCode) m.toggle();
                } else if(Keyboard.getEventKey() > 1) onRelease(Keyboard.getEventKey());
            }
        } catch (Exception ignored) {}
    }

    private void onRelease(int key) {
        for(Module m : moduleManager.modules) if(m.getKey() == key) if(m.hold) m.toggle();
    }

    public static String getName() {
        return instance.name();
    }

    public String name() {
        if(init) {
            switch (Config.instance.nameMode.getValString()) {
                case "kismancc": return NAME;
                case "LavaHack": return "LavaHack";
                case "TheKisDevs": return "TheKisDevs";
                case "kidman": return "kidman.club";
                case "TheClient": return "TheClient";
                case "BloomWare": return "BloomWare";
                case "custom": return Config.instance.customName.getValString();
            }
        }
        return NAME;
    }

    public static String getVersion() {
        return VERSION;
    }

    public static void initDirs() throws IOException {
        if (!Files.exists(Paths.get(fileName))) {
            Files.createDirectories(Paths.get(fileName));
            LOGGER.info("Root dir created");
        }
        if (!Files.exists(Paths.get(fileName + luaName))) {
            Files.createDirectories(Paths.get(fileName + luaName));
            LOGGER.info("Lua dir created");
        }
        if (!Files.exists(Paths.get(fileName + mappingName))) {
            Files.createDirectories(Paths.get(fileName + mappingName));
            LOGGER.info("Mapping dir created");
        }
    }

    public static void openLink(String link) {
        try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) desktop.browse(new URI(link));
        } catch (IOException | URISyntaxException e) {e.printStackTrace();}
    }

    //lua
    public static void reloadGUIs() {
        if(mc.player != null || mc.world != null) mc.displayGuiScreen(null);
        instance.halqGui.frames.forEach(Frame::reload);
        instance.clickGuiNew = new ClickGuiNew();
    }

    //lua
    public static void reloadHudGUIs() {
        if(mc.player != null || mc.world != null) mc.displayGuiScreen(null);
        instance.hudGui = new HudGui();
        instance.hudEditorGui = new HudEditorGui();
    }
}
