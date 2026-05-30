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

    /**
     * Hàm lưu cấu hình: Tự động dùng Reflection để bóc tách thêm các biến delay1, 2, 3
     * của từng module riêng biệt và lưu vào file JSON cùng hệ thống.
     */
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

                // Tự động quét tìm hàm Getter của delay1, delay2, delay3 để thêm dữ liệu JSON
                try {
                    java.lang.reflect.Method getD1 = m.getClass().getMethod("getDelay1");
                    obj.addProperty("delay1", (Integer) getD1.invoke(m));
                } catch (Exception ignored) {}

                try {
                    java.lang.reflect.Method getD2 = m.getClass().getMethod("getDelay2");
                    obj.addProperty("delay2", (Integer) getD2.invoke(m));
                } catch (Exception ignored) {}

                try {
                    java.lang.reflect.Method getD3 = m.getClass().getMethod("getDelay3");
                    obj.addProperty("delay3", (Integer) getD3.invoke(m));
                } catch (Exception ignored) {}

                modulesArr.add(obj);
            }

            root.add("modules", modulesArr);
            Files.writeString(Paths.get(CONFIG_FILE), gson.toJson(root));
            System.out.println("[SentaiHex] Config và các mốc MS đã được lưu thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hàm tải cấu hình: Đọc dữ liệu JSON cũ lên và ép ngược các giá trị delay1, 2, 3
     * vào chính xác các Class module tương ứng.
     */
    public void load() {
        try {
            if (!Files.exists(Paths.get(CONFIG_FILE))) {
                System.out.println("[SentaiHex] Không tìm thấy tệp config cũ, sử dụng mặc định.");
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

                // Nếu trong file cấu hình JSON có trường delay1 thì tự động Set ngược lại cho Module
                if (obj.has("delay1")) {
                    try {
                        java.lang.reflect.Method setD1 = m.getClass().getMethod("setDelay1", int.class);
                        setD1.invoke(m, obj.get("delay1").getAsInt());
                    } catch (Exception ignored) {}
                }

                // Set ngược lại cho delay2
                if (obj.has("delay2")) {
                    try {
                        java.lang.reflect.Method setD2 = m.getClass().getMethod("setDelay2", int.class);
                        setD2.invoke(m, obj.get("delay2").getAsInt());
                    } catch (Exception ignored) {}
                }

                // Set ngược lại cho delay3
                if (obj.has("delay3")) {
                    try {
                        java.lang.reflect.Method setD3 = m.getClass().getMethod("setDelay3", int.class);
                        setD3.invoke(m, obj.get("delay3").getAsInt());
                    } catch (Exception ignored) {}
                }
            }
            System.out.println("[SentaiHex] Config và đồng bộ MS được tải lên thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}