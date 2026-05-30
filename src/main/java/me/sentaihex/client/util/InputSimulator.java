package me.sentaihex.client.util;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class InputSimulator {

    private static final int MOUSEEVENTF_LEFTDOWN  = 0x0002;
    private static final int MOUSEEVENTF_LEFTUP    = 0x0004;
    private static final int MOUSEEVENTF_RIGHTDOWN = 0x0008;
    private static final int MOUSEEVENTF_RIGHTUP   = 0x0010;

    public static void pressKey(int winVK) {
        keyDown(winVK);
        keyUp(winVK);
    }

    public static void keyDown(int winVK) {
        WinUser.INPUT input = new WinUser.INPUT();
        input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
        input.input.setType("ki");
        input.input.ki.wVk = new WinDef.WORD(winVK);
        input.input.ki.dwFlags = new WinDef.DWORD(0);
        User32.INSTANCE.SendInput(new WinDef.DWORD(1), new WinUser.INPUT[]{input}, input.size());
    }

    public static void keyUp(int winVK) {
        WinUser.INPUT input = new WinUser.INPUT();
        input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
        input.input.setType("ki");
        input.input.ki.wVk = new WinDef.WORD(winVK);
        input.input.ki.dwFlags = new WinDef.DWORD(WinUser.KEYBDINPUT.KEYEVENTF_KEYUP);
        User32.INSTANCE.SendInput(new WinDef.DWORD(1), new WinUser.INPUT[]{input}, input.size());
    }

    public static void rightClick() {
        sendMouseEvent(MOUSEEVENTF_RIGHTDOWN);
        sendMouseEvent(MOUSEEVENTF_RIGHTUP);
    }

    public static void leftClick() {
        sendMouseEvent(MOUSEEVENTF_LEFTDOWN);
        sendMouseEvent(MOUSEEVENTF_LEFTUP);
    }

    private static void sendMouseEvent(int flags) {
        WinUser.INPUT input = new WinUser.INPUT();
        input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_MOUSE);
        input.input.setType("mi");
        input.input.mi.dwFlags = new WinDef.DWORD(flags);
        User32.INSTANCE.SendInput(new WinDef.DWORD(1), new WinUser.INPUT[]{input}, input.size());
    }

    /**
     * Convert jnativehook VC keycode → Windows Virtual Key code.
     *
     * Quy tắc:
     *  - Chữ cái A-Z: VC = ASCII uppercase = Win VK → pass-through
     *  - Số 0-9: VC = ASCII digit = Win VK → pass-through
     *  - Phím đặc biệt (F-keys, TAB, ALT...): VC khác Win VK → map tường minh
     */
    public static int nativeToWinVK(int vcCode) {
        // Chữ cái A-Z (0x41–0x5A) và số 0-9 (0x30–0x39): trùng Win VK
        if ((vcCode >= 0x41 && vcCode <= 0x5A) || (vcCode >= 0x30 && vcCode <= 0x39)) {
            return vcCode;
        }
        return switch (vcCode) {
            // --- Function keys ---
            case NativeKeyEvent.VC_F1  -> 0x70;
            case NativeKeyEvent.VC_F2  -> 0x71;
            case NativeKeyEvent.VC_F3  -> 0x72;
            case NativeKeyEvent.VC_F4  -> 0x73;
            case NativeKeyEvent.VC_F5  -> 0x74;
            case NativeKeyEvent.VC_F6  -> 0x75;
            case NativeKeyEvent.VC_F7  -> 0x76;
            case NativeKeyEvent.VC_F8  -> 0x77;
            case NativeKeyEvent.VC_F9  -> 0x78;
            case NativeKeyEvent.VC_F10 -> 0x79;
            case NativeKeyEvent.VC_F11 -> 0x7A;
            case NativeKeyEvent.VC_F12 -> 0x7B;
            // --- Navigation / editing ---
            case NativeKeyEvent.VC_TAB       -> 0x09;
            case NativeKeyEvent.VC_ENTER     -> 0x0D;
            case NativeKeyEvent.VC_ESCAPE    -> 0x1B;
            case NativeKeyEvent.VC_SPACE     -> 0x20;
            case NativeKeyEvent.VC_BACKSPACE -> 0x08;
            case NativeKeyEvent.VC_DELETE    -> 0x2E;
            case NativeKeyEvent.VC_INSERT    -> 0x2D;
            case NativeKeyEvent.VC_HOME      -> 0x24;
            case NativeKeyEvent.VC_END       -> 0x23;
            case NativeKeyEvent.VC_PAGE_UP   -> 0x21;
            case NativeKeyEvent.VC_PAGE_DOWN -> 0x22;
            // --- Arrow keys ---
            case NativeKeyEvent.VC_LEFT  -> 0x25;
            case NativeKeyEvent.VC_UP    -> 0x26;
            case NativeKeyEvent.VC_RIGHT -> 0x27;
            case NativeKeyEvent.VC_DOWN  -> 0x28;
            // --- Modifier keys ---
            case NativeKeyEvent.VC_SHIFT   -> 0x10;
            case NativeKeyEvent.VC_CONTROL -> 0x11;
            case NativeKeyEvent.VC_ALT     -> 0x12;
            // --- Fallback: trả nguyên, chấp nhận rủi ro với phím lạ ---
            default -> vcCode;
        };
    }
}