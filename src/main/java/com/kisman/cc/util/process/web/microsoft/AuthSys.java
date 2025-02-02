package com.kisman.cc.util.process.web.microsoft;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kisman.cc.gui.alts.AltManager;
import com.kisman.cc.gui.alts.microsoft.MSAuthScreen;
import com.kisman.cc.util.process.web.util.HttpTools;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.lwjgl.Sys;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuthSys {
    private static final Gson gson;
    private static volatile HttpServer srv;
    
    public static void start(final MSAuthScreen gui) {
        String done = "<html><body><h1>You can close this window now.</h1></body></html>";
        new Thread(() -> {
            try {
                if (AuthSys.srv == null) {
                    gui.setState("Waiting for auth... (Check your browser)");
                    if (HttpTools.ping("http://minecraft.net")) {
                        (AuthSys.srv = HttpServer.create(new InetSocketAddress(59125), 0)).createContext("/", exchange -> {
                            try {
                                gui.setState("Processing token...");
                                byte[] b = done.getBytes(StandardCharsets.UTF_8);
                                exchange.getResponseHeaders().put("Content-Type", Collections.singletonList("text/html; charset=UTF-8"));
                                exchange.sendResponseHeaders(200, b.length);
                                OutputStream os = exchange.getResponseBody();
                                os.write(b);
                                os.flush();
                                os.close();
                                String s = exchange.getRequestURI().getQuery();
                                if (s == null) gui.error("query=null");
                                else if (s.startsWith("code=")) accessTokenStep(s.replace("code=", ""), gui);
                                else if (s.equals("error=access_denied&error_description=The user has denied access to the scope requested by the client application.")) gui.error("Authorization cancelled.");
                                else gui.error(s);
                            } catch (Throwable throwable) {
                                if (throwable instanceof MicrosoftAuthException) {
                                    gui.error(throwable.getLocalizedMessage());
                                } else {
                                    throwable.printStackTrace();
                                    gui.error("Unexpected error: " + throwable);
                                }
                            }
                            AuthSys.stop();
                        });
                        AuthSys.srv.start();
                        Sys.openURL("https://login.live.com/oauth20_authorize.srf?client_id=54fd49e4-2103-4044-9603-2b028c814ec3&response_type=code&scope=XboxLive.signin%20XboxLive.offline_access&redirect_uri=http://localhost:59125&prompt=consent");
                    }
                }
            }
            catch (Throwable t) {
                if (t instanceof MicrosoftAuthException) gui.error(t.getLocalizedMessage());
                else {
                    gui.error("Unexpected error: " + t);
                    t.printStackTrace();
                }
                stop();
            }
        }, "Auth Thread").start();
    }
    
    public static void stop() {
        try {
            if (AuthSys.srv != null) {
                AuthSys.srv.stop(0);
                AuthSys.srv = null;
            }
        } catch (Throwable ignored) {}
    }
    
    private static void accessTokenStep(final String code, final MSAuthScreen gui) throws Throwable {
        PostRequest pr = new PostRequest("https://login.live.com/oauth20_token.srf").header("Content-Type", "application/x-www-form-urlencoded");
        Map<Object, Object> data = new HashMap<>();
        data.put("client_id", "54fd49e4-2103-4044-9603-2b028c814ec3");
        data.put("code", code);
        data.put("grant_type", "authorization_code");
        data.put("redirect_uri", "http://localhost:59125");
        data.put("scope", "XboxLive.signin XboxLive.offline_access");
        pr.post(data);
        if (pr.response() != 200) {
            throw new MicrosoftAuthException("accessToken response: " + pr.response());
        }
        xblStep(AuthSys.gson.fromJson(pr.body(), JsonObject.class).get("access_token").getAsString(), gui);
    }
    
    private static void xblStep(final String token, final MSAuthScreen gui) throws Throwable {
        gui.setState("Logging in...");
        PostRequest pr = new PostRequest("https://user.auth.xboxlive.com/user/authenticate").header("Content-Type", "application/json").header("Accept", "application/json");
        HashMap<Object, Object> map = new HashMap<>();
        HashMap<Object, Object> sub = new HashMap<>();
        sub.put("AuthMethod", "RPS");
        sub.put("SiteName", "user.auth.xboxlive.com");
        sub.put("RpsTicket", "d=" + token);
        map.put("Properties", sub);
        map.put("RelyingParty", "http://auth.xboxlive.com");
        map.put("TokenType", "JWT");
        pr.post(AuthSys.gson.toJson(map));
        if (pr.response() != 200) {
            throw new MicrosoftAuthException("xbl response: " + pr.response());
        }
        xstsStep(AuthSys.gson.fromJson(pr.body(), JsonObject.class).get("Token").getAsString(), gui);
    }
    
    private static void xstsStep(final String xbl, final MSAuthScreen gui) throws Throwable {
        PostRequest pr = new PostRequest("https://xsts.auth.xboxlive.com/xsts/authorize").header("Content-Type", "application/json").header("Accept", "application/json");
        HashMap<Object, Object> map = new HashMap<>();
        HashMap<Object, Object> sub = new HashMap<>();
        sub.put("SandboxId", "RETAIL");
        sub.put("UserTokens", Collections.singletonList(xbl));
        map.put("Properties", sub);
        map.put("RelyingParty", "rp://api.minecraftservices.com/");
        map.put("TokenType", "JWT");
        pr.post(AuthSys.gson.toJson(map));
        if (pr.response() == 401) throw new MicrosoftAuthException("This account doesn't have Minecraft account linked.");
        if (pr.response() != 200) throw new MicrosoftAuthException("xsts response: " + pr.response());
        JsonObject jo = AuthSys.gson.fromJson(pr.body(), JsonObject.class);
        minecraftTokenStep(jo.getAsJsonObject("DisplayClaims").getAsJsonArray("xui").get(0).getAsJsonObject().get("uhs").getAsString(), jo.get("Token").getAsString(), gui);
    }
    
    private static void minecraftTokenStep(final String xbl, final String xsts, final MSAuthScreen gui) throws Throwable {
        PostRequest pr = new PostRequest("https://api.minecraftservices.com/authentication/login_with_xbox").header("Content-Type", "application/json").header("Accept", "application/json");
        Map<Object, Object> map = new HashMap<>();
        map.put("identityToken", "XBL3.0 x=" + xbl + ";" + xsts);
        pr.post(AuthSys.gson.toJson(map));
        if (pr.response() != 200) throw new MicrosoftAuthException("minecraftToken response: " + pr.response());
        minecraftStoreVerify(AuthSys.gson.fromJson(pr.body(), JsonObject.class).get("access_token").getAsString(), gui);
    }
    
    private static void minecraftStoreVerify(final String token, final MSAuthScreen gui) throws Throwable {
        gui.setState("Verifying...");
        GetRequest gr = new GetRequest("https://api.minecraftservices.com/entitlements/mcstore").header("Authorization", "Bearer " + token);
        gr.get();
        if (gr.response() != 200) throw new MicrosoftAuthException("minecraftStore response: " + gr.response());
        if (AuthSys.gson.fromJson(gr.body(), JsonObject.class).getAsJsonArray("items").size() == 0) throw new MicrosoftAuthException("This account doesn't own the game.");
        minecraftProfileVerify(token, gui);
    }
    
    private static void minecraftProfileVerify(final String token, final MSAuthScreen gui) throws Throwable {
        GetRequest gr = new GetRequest("https://api.minecraftservices.com/minecraft/profile").header("Authorization", "Bearer " + token);
        gr.get();
        if (gr.response() != 200) throw new MicrosoftAuthException("minecraftProfile response: " + gr.response());
        JsonObject jo = AuthSys.gson.fromJson(gr.body(), JsonObject.class);
        String name = jo.get("name").getAsString();
        String uuid = jo.get("id").getAsString();
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.addScheduledTask(() -> {
            if (minecraft.currentScreen == gui) {
                try {
                    AltManager.setSession(new Session(name, uuid, token, "mojang"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                minecraft.displayGuiScreen(null);
            }
        });
    }
    
    static {
        gson = new Gson();
    }
    
    public static class MicrosoftAuthException extends Exception {
        private static final long serialVersionUID = 1L;
        public MicrosoftAuthException(final String s) {
            super(s);
        }
    }
}
