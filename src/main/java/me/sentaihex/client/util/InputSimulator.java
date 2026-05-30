package me.sentaihex.client.util;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public class InputSimulator {

    private static final int MOUSEEVENTF_LEFTDOWN  = 0x0002;
    private static final int MOUSEEVENTF_LEFTUP    = 0x0004;
    private static final int MOUSEEVENTF_RIGHTDOWN = 0x0008;
    private static final int MOUSEEVENTF_RIGHTUP   = 0x0010;

    public static void pressKey(int vkCode) {
        keyDown(vkCode);
        keyUp(vkCode);
    }

    public static void keyDown(int vkCode) {
        WinUser.INPUT input = new WinUser.INPUT();
        input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
        input.input.setType("ki");
        input.input.ki.wVk = new WinDef.WORD(vkCode);
        input.input.ki.dwFlags = new WinDef.DWORD(0);
        User32.INSTANCE.SendInput(new WinDef.DWORD(1), new WinUser.INPUT[]{input}, input.size());
    }

    public static void keyUp(int vkCode) {
        WinUser.INPUT input = new WinUser.INPUT();
        input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
        input.input.setType("ki");
        input.input.ki.wVk = new WinDef.WORD(vkCode);
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

    public static int toWinVK(int awtVK) {
        return switch (awtVK) {
            case java.awt.event.KeyEvent.VK_TAB     -> 0x09;
            case java.awt.event.KeyEvent.VK_ALT     -> 0x12;
            case java.awt.event.KeyEvent.VK_SHIFT   -> 0x10;
            case java.awt.event.KeyEvent.VK_CONTROL -> 0x11;
            case java.awt.event.KeyEvent.VK_ENTER   -> 0x0D;
            case java.awt.event.KeyEvent.VK_ESCAPE  -> 0x1B;
            case java.awt.event.KeyEvent.VK_SPACE   -> 0x20;
            default -> awtVK;
        };
    }
}
