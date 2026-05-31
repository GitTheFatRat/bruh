package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import me.sentaihex.client.util.InputSimulator;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class MaceTech2 extends ClientModule {

    // slot1 = Axe (đánh trước), slot2 = Mace (follow-up)
    private int slotAxe  = NativeKeyEvent.VC_Q;
    private int slotMace = NativeKeyEvent.VC_V;

    private int delay1 = 30;  // giảm từ 100 → 30ms
    private int delay2 = 25;  // giảm từ 80  → 25ms

    public MaceTech2() {
        super("Mace Tech 2 (Stun Slam)", "Macro", -1);
    }

    @Override public void onEnable()  { System.out.println("[SentaiHex] MaceTech2 ON"); }
    @Override public void onDisable() { System.out.println("[SentaiHex] MaceTech2 OFF"); }

    @Override
    public void execute() throws InterruptedException {
        // Axe trước
        InputSimulator.keyDown(InputSimulator.nativeToWinVK(slotAxe));
        Thread.sleep(delay1);
        InputSimulator.leftClick();
        InputSimulator.keyUp(InputSimulator.nativeToWinVK(slotAxe));
        Thread.sleep(delay1);

        // Mace follow-up
        InputSimulator.keyDown(InputSimulator.nativeToWinVK(slotMace));
        Thread.sleep(delay2);
        InputSimulator.leftClick();
        InputSimulator.keyUp(InputSimulator.nativeToWinVK(slotMace));
    }

    public int getSlotAxe()        { return slotAxe; }
    public void setSlotAxe(int k)  { this.slotAxe = k; }
    public int getSlotMace()       { return slotMace; }
    public void setSlotMace(int k) { this.slotMace = k; }
    public int getDelay1() { return delay1; }
    public void setDelay1(int d) { this.delay1 = d; }
    public int getDelay2() { return delay2; }
    public void setDelay2(int d) { this.delay2 = d; }
}