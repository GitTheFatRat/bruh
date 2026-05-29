package me.sentaihex.client.module;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import me.sentaihex.client.module.macros.AnchorMacro;
import me.sentaihex.client.module.macros.MaceTech1;
import me.sentaihex.client.module.macros.MaceTech2;
import me.sentaihex.client.module.macros.TNTCartMacro;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager implements NativeKeyListener {

    private final List<ClientModule> modules = new ArrayList<>();

    public ModuleManager() {
        register(new AnchorMacro());
        register(new TNTCartMacro());
        register(new MaceTech1());
        register(new MaceTech2());

        GlobalScreen.addNativeKeyListener(this);
        System.out.println("[SentaiHex] ModuleManager loaded " + modules.size() + " modules");
    }

    private void register(ClientModule module) {
        modules.add(module);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        for (ClientModule m : modules) {
            m.onKeyPress(e.getKeyCode());
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {}

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {}

    public List<ClientModule> getModules() { return modules; }

    public List<ClientModule> getByCategory(String category) {
        List<ClientModule> result = new ArrayList<>();
        for (ClientModule m : modules) {
            if (m.getCategory().equals(category)) result.add(m);
        }
        return result;
    }

    public ClientModule getByName(String name) {
        for (ClientModule m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }
}