package me.sentaihex.client.module.macros;

import me.sentaihex.client.module.ClientModule;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AnchorMacro extends ClientModule {

    private Robot robot;
    private int slotAnchor    = KeyEvent.VK_X;
    private int slotGlowstone = KeyEvent.VK_C;
    private int slotTotem     = KeyEvent.VK_TAB;

    // Giữ nguyên tên biến cũ để ConfigManager không bị lỗi "cannot find symbol"
    private int delay1 = 180; // Dùng làm switchDelay (thời gian chờ đổi item)
    private int delay2 = 80;  // Dùng làm clickDelay (thời gian chờ sau khi click)
    private int delay3 = 35;  // Dùng làm holdDelay (thời gian giữ phím bấm)

    public AnchorMacro() {
        super("Anchor Bomb", "Macro", -1);
        try {
            this.robot = new Robot();
        } catch (Exception e) {
            System.err.println("Robot could not be initialized: " + e.getMessage());
        }
    }

    @Override
    public void onEnable() {
        if (robot == null) {
            setEnabled(false);
            return;
        }
        new Thread(() -> {
            try {
                execute();
            }
            catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            finally {
                setEnabled(false);
            }
        }, "Anchor-Thread").start();
    }

    @Override
    public void onDisable() {
        // No behavior needed on disable
    }

    @Override
    public void execute() throws InterruptedException {
        if (robot == null) return;

        // BƯỚC 1: Chọn Anchor -> Đặt xuống
        pressKey(slotAnchor);
        Thread.sleep(delay1); // Chờ đổi sang Anchor
        rightClick();
        Thread.sleep(delay2); // Chờ đặt xong

        // BƯỚC 2: Chọn Glowstone -> Nạp điện
        pressKey(slotGlowstone);
        Thread.sleep(delay1); // Chờ đổi hẳn sang Glowstone để KHÔNG bị đặt 2 lần Anchor
        rightClick();
        Thread.sleep(delay2); // Chờ nạp xong

        // BƯỚC 3: Chọn Totem -> Click kích nổ
        pressKey(slotTotem);
        Thread.sleep(delay1); // Chờ đổi sang Totem
        rightClick();
    }

    private void pressKey(int key) throws InterruptedException {
        robot.keyPress(key);
        Thread.sleep(delay3); // Thời gian giữ phím xuống
        robot.keyRelease(key);
    }

    private void rightClick() throws InterruptedException {
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        Thread.sleep(delay3); // Thời gian giữ chuột xuống
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }

    // --- GIỮ NGUYÊN HOÀN TOÀN GETTERS & SETTERS CŨ CHO CONFIG MANAGER ---
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