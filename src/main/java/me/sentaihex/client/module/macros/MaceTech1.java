package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MaceTech1 extends ClientModule {

    private Robot robot;
    private int slotPearl      = KeyEvent.VK_V;   // Mặc định phím V
    private int slotWindCharge = KeyEvent.VK_ALT; // Mặc định phím ALT

    private int delay1 = 150; //
    private int delay2 = 100; //

    public MaceTech1() {
        super("Mace Tech 1 (Pearl+Wind)", "Macro", -1); //
        try {
            robot = new Robot(); //
        } catch (Exception e) {
            System.err.println("[SentaiHex] Không thể tạo Robot cho MaceTech1: " + e.getMessage());
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
                setEnabled(false); // Kết thúc combo tự động nhả nút gạt về vị trí OFF
            }
        }, "MaceTech1-Thread").start(); //
    }

    @Override public void onDisable() {}

    @Override
    public void execute() throws InterruptedException {
        if (robot == null) return; //

        // Bước 1: Ấn phím Pearl -> Click phải
        pressKey(slotPearl);      Thread.sleep(delay1); //
        rightClick();              Thread.sleep(50); //

        // Bước 2: Ấn phím WindCharge -> Click phải
        pressKey(slotWindCharge); Thread.sleep(delay2); //
        rightClick(); //
    }

    private void pressKey(int key) {
        try {
            robot.keyPress(key);
            robot.keyRelease(key); //
        } catch (Exception ignored) {}
    }

    private void rightClick() {
        try {
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK); //
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK); //
        } catch (Exception ignored) {}
    }

    // --- GETTERS & SETTERS (Kết nối đồng bộ với ClickGUI) ---
    public int getSlotPearl() { return slotPearl; } //
    public void setSlotPearl(int k) { this.slotPearl = k; } //
    public int getSlotWindCharge() { return slotWindCharge; } //
    public void setSlotWindCharge(int k) { this.slotWindCharge = k; } //
    public int getDelay1() { return delay1; } //
    public void setDelay1(int d) { this.delay1 = d; }
    public int getDelay2() { return delay2; } //
    public void setDelay2(int d) { this.delay2 = d; }
}