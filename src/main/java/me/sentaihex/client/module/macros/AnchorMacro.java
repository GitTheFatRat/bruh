package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import me.sentaihex.client.util.InputSimulator;
import java.awt.event.KeyEvent;

public class AnchorMacro extends ClientModule {

    private int slotAnchor    = KeyEvent.VK_X;
    private int slotGlowstone = KeyEvent.VK_C;
    private int slotTotem     = KeyEvent.VK_TAB;

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
        // Bước 1: chuyển sang slot Anchor → click phải đặt xuống
        InputSimulator.pressKey(InputSimulator.toWinVK(slotAnchor));
        if (delay1 > 0) Thread.sleep(delay1);
        InputSimulator.rightClick();
        if (delay1 > 0) Thread.sleep(delay1);

        // Bước 2: chuyển sang slot Glowstone → click phải nạp vào anchor
        InputSimulator.pressKey(InputSimulator.toWinVK(slotGlowstone));
        if (delay2 > 0) Thread.sleep(delay2);
        InputSimulator.rightClick();
        if (delay2 > 0) Thread.sleep(delay2);

        // Bước 3: chuyển sang slot Totem → click phải kích nổ
        InputSimulator.pressKey(InputSimulator.toWinVK(slotTotem));
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
