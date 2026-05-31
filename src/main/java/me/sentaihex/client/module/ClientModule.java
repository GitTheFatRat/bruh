package me.sentaihex.client.module;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class ClientModule {

    private String name;
    private String category;
    private boolean enabled = false;
    private int keybind = -1;

    private int globalDelay = 100;        // Delay mặc định
    private int[] stepDelays;

    // Chống chạy đè
    private volatile boolean running = false;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public ClientModule(String name, String category, int defaultKey) {
        this.name = name;
        this.category = category;
        this.keybind = defaultKey;
    }

    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void execute() throws InterruptedException;

    public void toggle() {
        setEnabled(!enabled);
        if (enabled) onEnable();
        else onDisable();
    }

    public void triggerIfEnabled(int keyCode) {
        if (!enabled) return;
        if (keybind == -1 || keyCode != keybind) return;
        if (running) return;

        running = true;
        new Thread(() -> {
            try {
                execute();
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("[SentaiHex] Lỗi execute " + name + ": " + e.getMessage());
            } finally {
                running = false;
            }
        }, name + "-Thread").start();
    }

    public boolean isRunning() {
        return running;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean newValue) {
        boolean old = this.enabled;
        this.enabled = newValue;
        pcs.firePropertyChange("enabled", old, newValue);
    }

    public int getKeybind() { return keybind; }
    public void setKeybind(int keybind) { this.keybind = keybind; }

    // ====================== DELAY FUNCTIONS (MỚI THÊM) ======================

    public int getGlobalDelay() {
        return globalDelay;
    }

    public void setGlobalDelay(int delay) {
        this.globalDelay = Math.max(1, delay); // Không cho delay < 1ms
    }

    /**
     * Hàm chính bạn muốn: set delay cho macro
     */
    public void setDelay(int ms) {
        setGlobalDelay(ms);
        System.out.println("[SentaiHex] " + name + " delay đã được đặt thành " + ms + "ms");
    }

    public int[] getStepDelays() {
        return stepDelays;
    }

    public void setStepDelays(int[] s) {
        this.stepDelays = s;
    }

    public int getStepDelay(int step) {
        if (stepDelays != null && step < stepDelays.length)
            return stepDelays[step];
        return globalDelay;
    }

    public String getKeybindName() {
        if (keybind == -1) return "NONE";
        return NativeKeyEvent.getKeyText(keybind);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
}