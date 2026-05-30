package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import me.sentaihex.client.util.InputSimulator;
import java.awt.event.KeyEvent;

public class MaceTech2 extends ClientModule {

    private int slotMace = KeyEvent.VK_V;
    private int slotAxe  = KeyEvent.VK_Q;

    private int delay1 = 100;
    private int delay2 = 80;

    public MaceTech2() {
        super("Mace Tech 2 (Stun Slam)", "Macro", -1);
    }

    @Override public void onEnable()  { System.out.println("[SentaiHex] MaceTech2 ON"); }
    @Override public void onDisable() { System.out.println("[SentaiHex] MaceTech2 OFF"); }

    @Override
    public void execute() throws InterruptedException {
        InputSimulator.pressKey(InputSimulator.toWinVK(slotAxe));
        Thread.sleep(delay1);
        InputSimulator.leftClick();
        Thread.sleep(50);

        InputSimulator.pressKey(InputSimulator.toWinVK(slotMace));
        Thread.sleep(delay2);
        InputSimulator.leftClick();
    }

    public int getSlotMace()       { return slotMace; }
    public void setSlotMace(int k) { this.slotMace = k; }
    public int getSlotAxe()        { return slotAxe; }
    public void setSlotAxe(int k)  { this.slotAxe = k; }
    public int getDelay1() { return delay1; }
    public void setDelay1(int d) { this.delay1 = d; }
    public int getDelay2() { return delay2; }
    public void setDelay2(int d) { this.delay2 = d; }
}
