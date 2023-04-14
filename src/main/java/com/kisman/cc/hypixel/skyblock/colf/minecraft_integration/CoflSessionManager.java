package com.kisman.cc.hypixel.skyblock.colf.minecraft_integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraftforge.fml.common.Loader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class CoflSessionManager {
	public static Gson gson = new GsonBuilder()  .registerTypeAdapter(ZonedDateTime.class, new TypeAdapter<ZonedDateTime>() {
        @Override
        public void write(JsonWriter out, ZonedDateTime value) throws IOException {
            out.value(value.toString());
        }

        @Override
        public ZonedDateTime read(JsonReader in) throws IOException {
            return ZonedDateTime.parse(in.nextString());
        }
    })
    .enableComplexMapKeySerialization().create();
	public static class CoflSession {
		
		public String SessionUUID;
		public ZonedDateTime timestampCreated;
		public CoflSession() {}
		public CoflSession(String sessionUUID, ZonedDateTime timestampCreated) {
			super();
			SessionUUID = sessionUUID;
			this.timestampCreated = timestampCreated;
		}
		
	}
	
	public static void UpdateCoflSessions() throws IOException {
		Map<String, CoflSession> sessions = GetCoflSessions();
		
		for (String username : sessions.keySet()) {
			if(!isValidSession(sessions.get(username))) {
				DeleteCoflSession(username);
			}
		}
	}
	
	public static Path GetTempFileFolder() {
		
		Path dataPath = Paths.get(Loader.instance().getConfigDir().getPath(), "CoflSky", "sessions");
		dataPath.toFile().mkdirs();
		
		return dataPath;
	}
	
	public static Map<String, CoflSession> GetCoflSessions() throws IOException{
		
		File[] sessions = GetTempFileFolder().toFile().listFiles();
		
		Map<String, CoflSession> map = new HashMap<>();

		for (File session : sessions) {
			map.put(session.getName(), GetCoflSession(session.getName()));
		}
		
		return map;
	}
	
	public static boolean isValidSession(CoflSession session) {
		return session.timestampCreated.plus(Duration.ofDays(7)).isAfter(ZonedDateTime.now());
	}
	
	private static Path GetUserPath(String username) {
		return Paths.get(GetTempFileFolder() + "/" + username);
	}
	public static void DeleteCoflSession(String username) {
		Path path =GetUserPath(username);
		path.toFile().delete();
	}
	public static void DeleteAllCoflSessions() {
		Path path =GetTempFileFolder();
		File[] sessions = path.toFile().listFiles();
		for(File f : sessions) {
			f.delete();
		}
	}
	
	public static CoflSession GetCoflSession(String username) throws IOException {
		Path path = GetUserPath(username);
		File file = path.toFile();
		
		if(!file.exists()) {
			CoflSession session = new CoflSession(UUID.randomUUID().toString(), ZonedDateTime.now());
			OverwriteCoflSession(username, session);
			return session;
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));
		String raw = reader.lines().collect(Collectors.joining("\n"));
		
		reader.close();
		return gson.fromJson(raw, CoflSession.class);
	}
	
	public static boolean OverwriteCoflSession(String username, CoflSession session) throws IOException {
		
		
		Path path = GetUserPath(username);
		File file = path.toFile();
		file.createNewFile();
		
		String data = gson.toJson(session);
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath())));
		bw.append(data);
		bw.flush();
		bw.close();
		
		return true;
	}
}
