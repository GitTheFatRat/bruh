package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class TNTCartMacro extends ClientModule {

    private Robot robot;
    private int slotRail      = KeyEvent.VK_X;
    private int slotCart      = KeyEvent.VK_C;
    private int slotCrossbow  = KeyEvent.VK_TAB;
    private int delay1 = 100;
    private int delay2 = 100;
    private int delay3 = 100;

    public TNTCartMacro() {
        super("TNT Cart", "Macro", -1);
        try { robot = new Robot(); } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void onEnable() {
        new Thread(() -> {
            try { execute(); }
            catch (InterruptedException ignored) {}
            finally { setEnabled(false); }
        }, "TNTCart-Thread").start();
    }

    @Override public void onDisable() {}

    @Override
    public void execute() throws InterruptedException {
        if (robot == null) return;
        pressKey(slotRail);     Thread.sleep(delay1);
        rightClick();            Thread.sleep(50);
        pressKey(slotCart);     Thread.sleep(delay2);
        rightClick();            Thread.sleep(50);
        pressKey(slotCrossbow); Thread.sleep(delay3);
        rightClick();
    }

    private void pressKey(int key) { robot.keyPress(key); robot.keyRelease(key); }
    private void rightClick() {
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    public int getSlotRail() { return slotRail; }
    public void setSlotRail(int k) { this.slotRail = k; }
    public int getSlotCart() { return slotCart; }
    public void setSlotCart(int k) { this.slotCart = k; }
    public int getSlotCrossbow() { return slotCrossbow; }
    public void setSlotCrossbow(int k) { this.slotCrossbow = k; }
    public int getDelay1() { return delay1; }
    public void setDelay1(int d) { this.delay1 = d; }
    public int getDelay2() { return delay2; }
    public void setDelay2(int d) { this.delay2 = d; }
    public int getDelay3() { return delay3; }
    public void setDelay3(int d) { this.delay3 = d; }
}