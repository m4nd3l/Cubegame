package dev.m4nd3l.cubegame.engine.glfw;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;

public class GLFW {
    // SETUP & TERMINATION
    public static boolean init() { return glfwInit(); }
    public static void terminate() { glfwTerminate(); }

    // WINDOW PROPERTIES
    public static void setDefaultWindowHints() { glfwDefaultWindowHints(); }
    public static void setWindowHint(int hint, int value) { glfwWindowHint(hint, value); }
    public static void setWindowVersion(int majorVersion, int minorVersion) { setWindowHint(GLFW_CONTEXT_VERSION_MAJOR, majorVersion); setWindowHint(GLFW_CONTEXT_VERSION_MINOR, minorVersion); }
    public static void setWindowOpenGLCoreProfile() { glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); }
    public static void setWindowNonVisible() { glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); }
    public static void setWindowResizable() { glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); }
    public static void setWindowMaximized() { glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE); }
    public static void setWindowMaximized(boolean maximized) { glfwWindowHint(GLFW_MAXIMIZED, maximized ? GLFW_TRUE : GLFW_FALSE); }
    public static void setVSync(boolean vSync) { glfwSwapInterval(vSync ? 1 : 0); }

    // WINDOW & OPENGL SETUP
    public static long createWindow(String title, int width, int height) { return glfwCreateWindow(width, height, title, NULL, NULL); }
    public static void createContextCurrent(long window) { glfwMakeContextCurrent(window); }
    public static void createCapabilities() { GL.createCapabilities(); }

    // WINDOW UTILS
    public static void showWindow(long window) { glfwShowWindow(window); }
    public static void hideWindow(long window) { glfwHideWindow(window); }
    public static void destroyWindow(long window) { glfwDestroyWindow(window); }
    public static void setWindowTitle(String title, long window) { glfwSetWindowTitle(window, title); }

    // GAME LOOP UTILS
    public static boolean windowShouldClose(long window) { return glfwWindowShouldClose(window); }
    public static double getTime() { return glfwGetTime(); }

    // CALLBACKS
    public static void freeCallbacks(long window) { glfwFreeCallbacks(window); }
    public static GLFWErrorCallback setErrorCallback(GLFWErrorCallbackI callback) { return glfwSetErrorCallback(callback); }
    public static GLFWKeyCallback setKeyCallback(long window, GLFWKeyCallbackI callback) { return glfwSetKeyCallback(window, callback); }
    public static GLFWCursorPosCallback setCursorPositionCallback(long window, GLFWCursorPosCallbackI callback) { return glfwSetCursorPosCallback(window, callback); }
    public static GLFWMouseButtonCallback setMouseButtonCallback(long window, GLFWMouseButtonCallbackI callback) { return glfwSetMouseButtonCallback(window, callback); }
    public static GLFWScrollCallback setScrollCallback(long window, GLFWScrollCallbackI callback) { return glfwSetScrollCallback(window, callback); }

}
