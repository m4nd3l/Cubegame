package dev.m4nd3l.cubegame.engine.rendering.glfw;

import org.lwjgl.glfw.GLFWErrorCallback;

import static dev.m4nd3l.cubegame.engine.rendering.glfw.GLFW.*;

public class Window {
    private String title;
    private int width;
    private int height;
    private boolean maximized;

    private long glfwWindow;

    public Window(String title, int width, int height, boolean maximized) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.maximized = maximized;
    }

    public void initialize() {
        if (!init()) throw new IllegalStateException("Unable to initialize GLFW");

        setErrorCallback(GLFWErrorCallback.createPrint(System.err));
        setDefaultWindowHints();

        setWindowVersion(3, 3);

        setWindowNonVisible();
        setWindowResizable();

        this.glfwWindow = createWindow(title, width, height);
        if (this.glfwWindow == 0) throw new RuntimeException("Failed to create window. Check if your GPU supports OpenGL 3.3+");

        createContextCurrent(glfwWindow);
        createCapabilities();

        setVSync(false);
    }

    public void show() { showWindow(glfwWindow); }
    public void hide() { hideWindow(glfwWindow); }

    public void delete() {
        freeCallbacks(glfwWindow);
        destroyWindow(glfwWindow);

        terminate();
        setErrorCallback(null).free();
    }

    public String getTitle() { return title; }
    public long getGlfwWindow() { return glfwWindow; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isMaximized() { return maximized; }

    public void setTitle(String title) { this.title = title; setWindowTitle(title, glfwWindow); }
}
