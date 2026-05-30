package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import me.sentaihex.client.util.InputSimulator;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class AnchorMacro extends ClientModule {

    // Giá trị mặc định dùng raw VC code:
    // Chữ cái: VC = ASCII uppercase (X=0x58, C=0x43)
    // TAB: dùng NativeKeyEvent.VC_TAB (constant này có trong 2.2)
    private int slotAnchor    = 0x58; // X
    private int slotGlowstone = 0x43; // C
    private int slotTotem     = NativeKeyEvent.VC_TAB;

    private int delay1 = 20;
    private int delay2 = 20;
    private int delay3 = 20;

    public AnchorMacro() {
        super("Anchor Bomb", "Macro", -1);
    }

    @Override public void onEnable()  { System.out.println("[SentaiHex] AnchorMacro ON"); }
    @Override public void onDisable() { System.out.println("[SentaiHex] AnchorMacro OFF"); }

    @Override
    public void execute() throws InterruptedException {
        // Bước 1: ấn phím Anchor slot → click phải đặt xuống
        InputSimulator.pressKey(InputSimulator.nativeToWinVK(slotAnchor));
        if (delay1 > 0) Thread.sleep(delay1);
        InputSimulator.rightClick();
        if (delay1 > 0) Thread.sleep(delay1);

        // Bước 2: ấn phím Glowstone slot → click phải nạp vào anchor
        InputSimulator.pressKey(InputSimulator.nativeToWinVK(slotGlowstone));
        if (delay2 > 0) Thread.sleep(delay2);
        InputSimulator.rightClick();
        if (delay2 > 0) Thread.sleep(delay2);

        // Bước 3: ấn phím Totem slot → click phải kích nổ
        InputSimulator.pressKey(InputSimulator.nativeToWinVK(slotTotem));
        if (delay3 > 0) Thread.sleep(delay3);
        InputSimulator.rightClick();
    }

    public int getSlotAnchor()          { return slotAnchor; }
    public void setSlotAnchor(int k)    { this.slotAnchor = k; }
    public int getSlotGlowstone()       { return slotGlowstone; }
    public void setSlotGlowstone(int k) { this.slotGlowstone = k; }
    public int getSlotTotem()           { return slotTotem; }
    public void setSlotTotem(int k)     { this.slotTotem = k; }
    public int getDelay1() { return delay1; }
    public void setDelay1(int d) { this.delay1 = d; }
    public int getDelay2() { return delay2; }
    public void setDelay2(int d) { this.delay2 = d; }
    public int getDelay3() { return delay3; }
    public void setDelay3(int d) { this.delay3 = d; }
}