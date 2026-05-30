package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class TNTCartMacro extends ClientModule {

    private Robot robot;
    private int slotRail      = KeyEvent.VK_F12; // Mặc định phím F12
    private int slotCart      = KeyEvent.VK_F9;  // Mặc định phím F9
    private int slotCrossbow  = KeyEvent.VK_8;   // Mặc định phím 8

    private int delay1 = 100; //
    private int delay2 = 100; //
    private int delay3 = 100; //

    public TNTCartMacro() {
        super("TNT Cart", "Macro", -1); //
        try {
            robot = new Robot(); //
        } catch (Exception e) {
            System.err.println("[SentaiHex] Không thể tạo Robot cho TNT Cart: " + e.getMessage());
        }
    }

    @Override
    public void onEnable() {
        new Thread(() -> {
            try {
                execute(); //
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            } finally {
                setEnabled(false); // Chạy xong combo 1 chu kỳ tự động gạt nút OFF
            }
        }, "TNTCart-Thread").start(); //
    }

    @Override public void onDisable() {}

    @Override
    public void execute() throws InterruptedException {
        if (robot == null) return; //

        // Bước 1: Ấn phím Rail -> Click chuột phải
        pressKey(slotRail);     Thread.sleep(delay1); //
        rightClick();            Thread.sleep(50); //

        // Bước 2: Ấn phím Cart -> Click chuột phải
        pressKey(slotCart);     Thread.sleep(delay2); //
        rightClick();            Thread.sleep(50); //

        // Bước 3: Ấn phím Crossbow -> Click chuột phải
        pressKey(slotCrossbow); Thread.sleep(delay3); //
        rightClick(); //
    }

    private void pressKey(int key) {
        try {
            robot.keyPress(key);
            robot.keyRelease(key);
        } catch (Exception ignored) {}
    }

    private void rightClick() {
        try {
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK); //
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); //
        } catch (Exception ignored) {}
    }

    // --- GETTERS & SETTERS (Kết nối đồng bộ với ClickGUI) ---
    public int getSlotRail() { return slotRail; } //
    public void setSlotRail(int k) { this.slotRail = k; } //
    public int getSlotCart() { return slotCart; } //
    public void setSlotCart(int k) { this.slotCart = k; } //
    public int getSlotCrossbow() { return slotCrossbow; } //
    public void setSlotCrossbow(int k) { this.slotCrossbow = k; } //
    public int getDelay1() { return delay1; } //
    public void setDelay1(int d) { this.delay1 = d; }
    public int getDelay2() { return delay2; } //
    public void setDelay2(int d) { this.delay2 = d; }
    public int getDelay3() { return delay3; } //
    public void setDelay3(int d) { this.delay3 = d; }
}