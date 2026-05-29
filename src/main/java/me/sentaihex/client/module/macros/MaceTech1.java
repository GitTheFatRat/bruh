package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MaceTech1 extends ClientModule {

    private Robot robot;
    private int slotPearl      = KeyEvent.VK_R;
    private int slotWindCharge = KeyEvent.VK_ALT;
    private int delay1 = 150;
    private int delay2 = 100;

    public MaceTech1() {
        super("Mace Tech 1 (Pearl+Wind)", "Macro", -1);
        try { robot = new Robot(); } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void onEnable() {
        new Thread(() -> {
            try { execute(); }
            catch (InterruptedException ignored) {}
            finally { setEnabled(false); }
        }, "MaceTech1-Thread").start();
    }

    @Override public void onDisable() {}

    @Override
    public void execute() throws InterruptedException {
        if (robot == null) return;
        pressKey(slotPearl);      Thread.sleep(delay1);
        rightClick();              Thread.sleep(50);
        pressKey(slotWindCharge); Thread.sleep(delay2);
        rightClick();
    }

    private void pressKey(int key) { robot.keyPress(key); robot.keyRelease(key); }
    private void rightClick() {
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    public int getSlotPearl() { return slotPearl; }
    public void setSlotPearl(int k) { this.slotPearl = k; }
    public int getSlotWindCharge() { return slotWindCharge; }
    public void setSlotWindCharge(int k) { this.slotWindCharge = k; }
    public int getDelay1() { return delay1; }
    public void setDelay1(int d) { this.delay1 = d; }
    public int getDelay2() { return delay2; }
    public void setDelay2(int d) { this.delay2 = d; }
}