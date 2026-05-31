package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import me.sentaihex.client.util.InputSimulator;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class AnchorMacro extends ClientModule {

    private int slotAnchor    = NativeKeyEvent.VC_X;
    private int slotGlowstone = NativeKeyEvent.VC_C;
    private int slotTotem     = NativeKeyEvent.VC_TAB;

    private int delay1 = 45;
    private int delay2 = 60;
    private int delay3 = 40;

    public AnchorMacro() {
        super("Anchor Bomb", "Macro", NativeKeyEvent.VC_G); // Mặc định phím G
    }

    @Override
    public void onEnable()  {
        System.out.println("[SentaiHex] AnchorMacro ON - Keybind: " + getKeybindName());
    }

    @Override
    public void onDisable() {
        System.out.println("[SentaiHex] AnchorMacro OFF");
    }

    @Override
    public void execute() throws InterruptedException {
        System.out.println("[AnchorMacro] === EXECUTE STARTED ==="); // Debug

        try {
            InputSimulator.pressKey(InputSimulator.nativeToWinVK(slotAnchor));
            Thread.sleep(delay1);
            InputSimulator.rightClick();
            Thread.sleep(delay1);

            InputSimulator.pressKey(InputSimulator.nativeToWinVK(slotGlowstone));
            Thread.sleep(delay2);
            InputSimulator.rightClick();
            Thread.sleep(delay2);

            InputSimulator.pressKey(InputSimulator.nativeToWinVK(slotTotem));
            Thread.sleep(delay3);
            InputSimulator.rightClick();
            Thread.sleep(delay3);

            System.out.println("[AnchorMacro] === EXECUTE FINISHED ===");
        } catch (Exception e) {
            System.err.println("[AnchorMacro] Lỗi: " + e.getMessage());
        }
    }

    public void setDelay(int ms) {
        setGlobalDelay(ms);
        delay1 = (int)(ms * 0.9);
        delay2 = (int)(ms * 1.25);
        delay3 = (int)(ms * 0.85);
        System.out.println("[SentaiHex] Anchor delay set to " + ms + "ms");
    }

    // Giữ getter setter cũ
    public int getDelay1() { return delay1; }
    public void setDelay1(int d) { this.delay1 = Math.max(20, d); }
    // ... (các getter setter khác giữ nguyên)
}