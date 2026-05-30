package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AnchorMacro extends ClientModule {

    // Lưu mã phím hệ thống (Key Code từ ClickGUI của bạn, ví dụ: KeyEvent.VK_X)
    private int slotAnchor    = KeyEvent.VK_X;   // Mặc định phím X
    private int slotGlowstone = KeyEvent.VK_C;   // Mặc định phím C
    private int slotTotem     = KeyEvent.VK_TAB; // Mặc định phím TAB

    // Các biến delay (ms) giữa các hành động, chỉnh được trên GUI
    private int delay1 = 20;
    private int delay2 = 20;
    private int delay3 = 20;

    private static Robot robot;

    public AnchorMacro() {
        super("Anchor Bomb", "Macro", -1); // Phím kích hoạt chính (Bind)
        try {
            if (robot == null) {
                robot = new Robot();
            }
        } catch (Exception e) {
            System.err.println("[SentaiHex] Không thể khởi tạo Robot giả lập phím: " + e.getMessage());
        }
    }

    @Override
    public void onEnable() {
        // Chạy chuỗi giả lập phím trên một luồng riêng biệt để không làm đơ game/GUI
        new Thread(() -> {
            try {
                execute();
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("[SentaiHex] Lỗi Macro: " + e.getMessage());
            } finally {
                setEnabled(false); // Chạy xong chuỗi tự động tắt nút gạt về OFF
            }
        }, "Anchor-Robot-Thread").start();
    }

    @Override public void onDisable() {}

    @Override
    public void execute() throws InterruptedException {
        if (robot == null) return;

        // --- BƯỚC 1: ẤN PHÍM ANCHOR + CLICK CHUỘT PHẢI ---
        pressAndReleaseKey(slotAnchor);
        if (delay1 > 0) Thread.sleep(delay1);
        clickMouseRight();
        if (delay1 > 0) Thread.sleep(delay1);

        // --- BƯỚC 2: ẤN PHÍM GLOWSTONE + CLICK CHUỘT PHẢI ---
        pressAndReleaseKey(slotGlowstone);
        if (delay2 > 0) Thread.sleep(delay2);
        clickMouseRight();
        if (delay2 > 0) Thread.sleep(delay2);

        // --- BƯỚC 3: ẤN PHÍM TOTEM + CLICK CHUỘT PHẢI ---
        pressAndReleaseKey(slotTotem);
        if (delay3 > 0) Thread.sleep(delay3);
        clickMouseRight();
    }

    /**
     * Hàm phụ trợ giả lập hành động gõ phím (Nhấn xuống và Thả ra)
     */
    private void pressAndReleaseKey(int keyCode) {
        try {
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
        } catch (Exception ignored) {}
    }

    /**
     * Hàm phụ trợ giả lập click chuột phải
     */
    private void clickMouseRight() {
        try {
            robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        } catch (Exception ignored) {}
    }

    // --- GETTERS & SETTERS (Kết nối đồng bộ với ClickGUI và ConfigManager) ---
    public int getSlotAnchor() { return slotAnchor; }
    public void setSlotAnchor(int k) { this.slotAnchor = k; }

    public int getSlotGlowstone() { return slotGlowstone; }
    public void setSlotGlowstone(int k) { this.slotGlowstone = k; }

    public int getSlotTotem() { return slotTotem; }
    public void setSlotTotem(int k) { this.slotTotem = k; }

    public int getDelay1() { return delay1; }
    public void setDelay1(int d) { this.delay1 = d; }

    public int getDelay2() { return delay2; }
    public void setDelay2(int d) { this.delay2 = d; }

    public int getDelay3() { return delay3; }
    public void setDelay3(int d) { this.delay3 = d; }
}