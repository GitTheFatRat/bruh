package me.sentaihex.client.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MinecraftAccess {

    private static Object  mcInstance     = null;
    private static Field   hitResultField = null;
    private static boolean initialized   = false;
    private static boolean initFailed    = false;

    public static boolean init() {
        if (initialized) return true;
        if (initFailed)  return false;
        try {
            Class<?> mcClass = null;
            for (String name : new String[]{
                    "net.minecraft.client.MinecraftClient",
                    "net.minecraft.client.Minecraft"}) {
                try { mcClass = Class.forName(name, true,
                        Thread.currentThread().getContextClassLoader()); break; }
                catch (ClassNotFoundException ignored) {}
            }
            if (mcClass == null) { initFailed = true; return false; }

            // Lấy singleton instance
            for (Field f : mcClass.getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getType().equals(mcClass) &&
                        java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                    mcInstance = f.get(null); break;
                }
            }
            if (mcInstance == null) { initFailed = true; return false; }

            // Tìm crosshairTarget / hitResult field
            for (Field f : mcClass.getDeclaredFields()) {
                f.setAccessible(true);
                String t = f.getType().getName();
                if (t.contains("HitResult") || t.contains("RaycastResult")) {
                    hitResultField = f; break;
                }
            }
            if (hitResultField == null) { initFailed = true; return false; }

            System.out.println("[SentaiHex] MinecraftAccess OK - hitResult: "
                    + hitResultField.getName());
            initialized = true;
            return true;
        } catch (Exception e) {
            System.err.println("[SentaiHex] MinecraftAccess init error: " + e.getMessage());
            initFailed = true;
            return false;
        }
    }

    /** True nếu crosshair đang nhắm vào entity */
    public static boolean isLookingAtEntity() {
        if (!init()) return false;
        try {
            Object hr = hitResultField.get(mcInstance);
            if (hr == null) return false;
            // Check type enum field
            for (Field f : hr.getClass().getSuperclass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getType().isEnum()) {
                    return f.get(hr).toString().contains("ENTITY");
                }
            }
            return hr.getClass().getName().toLowerCase().contains("entity");
        } catch (Exception e) { return false; }
    }

    /** Trả về entity đang bị nhắm, null nếu không có */
    public static Object getTargetEntity() {
        if (!init()) return null;
        try {
            Object hr = hitResultField.get(mcInstance);
            if (hr == null) return null;
            if (!hr.getClass().getName().toLowerCase().contains("entity")) return null;
            // Tìm field kiểu Entity trong EntityHitResult
            for (Field f : hr.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getType().getName().toLowerCase().contains("entity")) return f.get(hr);
            }
        } catch (Exception ignored) {}
        return null;
    }

    /** True nếu entity đang hold shield */
    public static boolean isEntityBlocking(Object entity) {
        if (entity == null) return false;
        try {
            for (Method m : entity.getClass().getMethods()) {
                if (m.getName().toLowerCase().contains("blocking")
                        && m.getParameterCount() == 0
                        && m.getReturnType() == boolean.class)
                    return (boolean) m.invoke(entity);
            }
        } catch (Exception ignored) {}
        return false;
    }
}