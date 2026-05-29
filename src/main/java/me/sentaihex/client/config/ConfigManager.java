package me.sentaihex.client.config;

import com.google.gson.*;
import me.sentaihex.client.SentaiHex;
import me.sentaihex.client.module.ClientModule;

import java.io.*;
import java.nio.file.*;

public class ConfigManager {

    private static final String CONFIG_DIR = System.getProperty("user.home") + "/.sentaihex/";
    private static final String CONFIG_FILE = CONFIG_DIR + "config.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void save() {
        try {
            Files.createDirectories(Paths.get(CONFIG_DIR));
            JsonObject root = new JsonObject();
            JsonArray modulesArr = new JsonArray();

            for (ClientModule m : SentaiHex.INSTANCE.moduleManager.getModules()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("name", m.getName());
                obj.addProperty("enabled", m.isEnabled());
                obj.addProperty("keybind", m.getKeybind());
                obj.addProperty("globalDelay", m.getGlobalDelay());
                modulesArr.add(obj);
            }

            root.add("modules", modulesArr);
            Files.writeString(Paths.get(CONFIG_FILE), gson.toJson(root));
            System.out.println("[SentaiHex] Config saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            if (!Files.exists(Paths.get(CONFIG_FILE))) {
                System.out.println("[SentaiHex] No config found, using defaults.");
                return;
            }

            String json = Files.readString(Paths.get(CONFIG_FILE));
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonArray modulesArr = root.getAsJsonArray("modules");

            for (JsonElement el : modulesArr) {
                JsonObject obj = el.getAsJsonObject();
                String name = obj.get("name").getAsString();
                ClientModule m = SentaiHex.INSTANCE.moduleManager.getByName(name);
                if (m == null) continue;
                m.setEnabled(obj.get("enabled").getAsBoolean());
                m.setKeybind(obj.get("keybind").getAsInt());
                m.setGlobalDelay(obj.get("globalDelay").getAsInt());
            }
            System.out.println("[SentaiHex] Config loaded!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}