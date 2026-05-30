package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MaceTech2 extends ClientModule {

    private Robot robot;
    private int slotAxe  = KeyEvent.VK_Q; // Mặc định phím Q
    private int slotMace = KeyEvent.VK_V; // Mặc định phím V

    private int delay1 = 100; //
    private int delay2 = 80;  //

    public MaceTech2() {
        super("Mace Tech 2 (Stun Slam)", "Macro", -1); //
        try {
            robot = new Robot(); //
        } catch (Exception e) {
            System.err.println("[SentaiHex] Không thể tạo Robot cho MaceTech2: " + e.getMessage());
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
                setEnabled(false); // Chạy hết combo tự động đưa trạng thái nút gạt về OFF để chống nhấp nháy liên tục
            }
        }, "MaceTech2-Thread").start(); //
    }

    @Override public void onDisable() {}

    @Override
    public void execute() throws InterruptedException {
        if (robot == null) return; //

        // Bước 1: Ấn phím Rìu (Axe) -> Click chuột trái (Tấn công phá khiên)
        pressKey(slotAxe);  Thread.sleep(delay1); //
        leftClick();         Thread.sleep(50); //

        // Bước 2: Ấn phím Chùy (Mace) -> Click chuột trái (Dứt điểm)
        pressKey(slotMace); Thread.sleep(delay2); //
        leftClick(); //
    }

    private void pressKey(int key) {
        try {
            robot.keyPress(key);
            robot.keyRelease(key); //
        } catch (Exception ignored) {}
    }

    private void leftClick() {
        try {
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); //
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); //
        } catch (Exception ignored) {}
    }

    // --- GETTERS & SETTERS (Kết nối đồng bộ với ClickGUI) ---
    public int getSlotAxe() { return slotAxe; } //
    public void setSlotAxe(int k) { this.slotAxe = k; } //
    public int getSlotMace() { return slotMace; } //
    public void setSlotMace(int k) { this.slotMace = k; } //
    public int getDelay1() { return delay1; } //
    public void setDelay1(int d) { this.delay1 = d; }
    public int getDelay2() { return delay2; } //
    public void setDelay2(int d) { this.delay2 = d; }
}