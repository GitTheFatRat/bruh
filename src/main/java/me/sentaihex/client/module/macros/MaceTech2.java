package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MaceTech2 extends ClientModule {

    private Robot robot;
    private int slotAxe  = KeyEvent.VK_Q;
    private int slotMace = KeyEvent.VK_V;
    private int delay1 = 100;
    private int delay2 = 80;

    public MaceTech2() {
        super("Mace Tech 2 (Stun Slam)", "Macro", -1);
        try { robot = new Robot(); } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void onEnable() {
        new Thread(() -> {
            try { execute(); }
            catch (InterruptedException ignored) {}
            finally { setEnabled(false); }
        }, "MaceTech2-Thread").start();
    }

    @Override public void onDisable() {}

    @Override
    public void execute() throws InterruptedException {
        if (robot == null) return;
        pressKey(slotAxe);  Thread.sleep(delay1);
        leftClick();         Thread.sleep(50);
        pressKey(slotMace); Thread.sleep(delay2);
        leftClick();
    }

    private void pressKey(int key) { robot.keyPress(key); robot.keyRelease(key); }
    private void leftClick() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public int getSlotAxe() { return slotAxe; }
    public void setSlotAxe(int k) { this.slotAxe = k; }
    public int getSlotMace() { return slotMace; }
    public void setSlotMace(int k) { this.slotMace = k; }
    public int getDelay1() { return delay1; }
    public void setDelay1(int d) { this.delay1 = d; }
    public int getDelay2() { return delay2; }
    public void setDelay2(int d) { this.delay2 = d; }
}