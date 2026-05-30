package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import me.sentaihex.client.util.InputSimulator;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class MaceTech1 extends ClientModule {

    private int slotPearl      = 0x56; // V
    private int slotWindCharge = NativeKeyEvent.VC_ALT;

    private int delay1 = 150;
    private int delay2 = 100;

    public MaceTech1() {
        super("Mace Tech 1 (Pearl+Wind)", "Macro", -1);
    }

    @Override public void onEnable()  { System.out.println("[SentaiHex] MaceTech1 ON"); }
    @Override public void onDisable() { System.out.println("[SentaiHex] MaceTech1 OFF"); }

    @Override
    public void execute() throws InterruptedException {
        InputSimulator.pressKey(InputSimulator.nativeToWinVK(slotPearl));
        Thread.sleep(delay1);
        InputSimulator.rightClick();
        Thread.sleep(50);

        InputSimulator.pressKey(InputSimulator.nativeToWinVK(slotWindCharge));
        Thread.sleep(delay2);
        InputSimulator.rightClick();
    }

    public int getSlotPearl()            { return slotPearl; }
    public void setSlotPearl(int k)      { this.slotPearl = k; }
    public int getSlotWindCharge()       { return slotWindCharge; }
    public void setSlotWindCharge(int k) { this.slotWindCharge = k; }
    public int getDelay1() { return delay1; }
    public void setDelay1(int d) { this.delay1 = d; }
    public int getDelay2() { return delay2; }
    public void setDelay2(int d) { this.delay2 = d; }
}