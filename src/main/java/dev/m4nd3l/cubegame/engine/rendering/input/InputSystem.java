package dev.m4nd3l.cubegame.engine.rendering.input;

import static org.lwjgl.glfw.GLFW.*;

public class InputSystem {
    private static Keyboard keyboard = new Keyboard();
    private static Mouse mouse = new Mouse();

    // KEYBOARD

    public static void init(long window) {
        keyboard.init(window);
        mouse.init(window);
    }

    public static boolean isKeyUp(KeyboardKeys key) { return keyboard.isKeyUp(key); }
    public static boolean isKeyDown(KeyboardKeys key) { return keyboard.isKeyDown(key); }

    public static boolean isKeyPressed(KeyboardKeys key) { return keyboard.isKeyPressed(key); }

    public static boolean isKeyReleased(KeyboardKeys key) { return keyboard.isKeyReleased(key); }

    public static boolean isModDown(int modifier) { return keyboard.isModDown(modifier); }

    public static boolean isShiftDown() { return isModDown(GLFW_MOD_SHIFT); }
    public static boolean isControlDown() { return isModDown(GLFW_MOD_CONTROL); }
    public static boolean isAltDown() { return isModDown(GLFW_MOD_ALT); }

    // MOUSE

    public static double getX() { return mouse.getX(); }
    public static double getY() { return mouse.getY(); }

    public static double getDeltaX() { return mouse.getDeltaX(); }
    public static double getDeltaY() { return mouse.getDeltaY(); }

    public static double getScrollX() { return mouse.getScrollX(); }
    public static double getScrollY() { return mouse.getScrollY(); }

    public static boolean isKeyUp(MouseKeys button) { return mouse.isButtonUp(button); }
    public static boolean isKeyDown(MouseKeys button) { return mouse.isButtonDown(button); }

    // UPDATE

    public static void update() { keyboard.update(); }
    public static void endFrame() { mouse.endFrame(); }
}
