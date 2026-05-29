package me.sentaihex.client.module;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public abstract class ClientModule {

    private String name;
    private String category;
    private boolean enabled = false;
    private int keybind = -1;
    private int globalDelay = 100;
    private int[] stepDelays;

    public ClientModule(String name, String category, int defaultKey) {
        this.name = name;
        this.category = category;
        this.keybind = defaultKey;
    }

    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void execute() throws InterruptedException;

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public void onKeyPress(int keyCode) {
        if (keyCode == keybind) toggle();
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getKeybind() { return keybind; }
    public void setKeybind(int keybind) { this.keybind = keybind; }
    public int getGlobalDelay() { return globalDelay; }
    public void setGlobalDelay(int d) { this.globalDelay = d; }
    public int[] getStepDelays() { return stepDelays; }
    public void setStepDelays(int[] s) { this.stepDelays = s; }

    public int getStepDelay(int step) {
        if (stepDelays != null && step < stepDelays.length) return stepDelays[step];
        return globalDelay;
    }

    public String getKeybindName() {
        if (keybind == -1) return "NONE";
        return NativeKeyEvent.getKeyText(keybind);
    }
}