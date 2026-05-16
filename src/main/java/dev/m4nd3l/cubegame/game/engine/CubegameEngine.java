package dev.m4nd3l.cubegame.game.engine;

import dev.m4nd3l.cubegame.engine.glfw.Window;
import dev.m4nd3l.cubegame.game.world.World;
import dev.m4nd3l.cubegame.game.ticks.UpdateHandler;
import dev.m4nd3l.cubegame.game.world.WorldLoader;
import dev.m4nd3l.cubegame.toolbox.ConvertingTool;
import dev.m4nd3l.loggerutil.LoggerUtils;
import dev.m4nd3l.loggerutil.logger.Logger;

import static dev.m4nd3l.cubegame.engine.opengl.OpenGL.*;

public class CubegameEngine {
    private Window window;
    private UpdateHandler handler;
    private World currentWorld;
    private boolean debugMode, wireframeMode;
    private static Logger LOGGER = LoggerUtils.getLogger();

    public CubegameEngine() { this("Cubegame", 1920, 1080, true, false, 120); }
    public CubegameEngine(String title, int width, int height, boolean maximized, int fps) { this(title, width, height, maximized, false, fps); }
    public CubegameEngine(String title, int width, int height, boolean maximized, boolean debugMode, int fps) {
        this.window = new Window(title, width, height, maximized);
        this.handler = new UpdateHandler(fps, -1);
        this.debugMode = debugMode;
        this.wireframeMode = false;
    }

    public void init() {
        window.initialize();
        handler.setGlfwWindow(window.getGlfwWindow());
        worldAction(World::initialize);
    }

    public void start() {
        LOGGER.info("Starting...");
        show();
        handler.startGameLoop(this::update, this::tick, this::render, fps -> window.setTitle("Cubegame FPS: " + fps));
    }

    public void terminate() {
        LOGGER.info("Terminating...");
        window.hide();
        window.delete();
        worldAction(World::delete);
    }

    public void loadWorld(String worldName) { loadWorld(worldName, ConvertingTool.convertToLong(worldName)); }
    public void loadWorld(String worldName, long seed) { currentWorld = WorldLoader.load(worldName, seed); }

    public void update(double deltaTime) {
        worldAction(world -> world.update(deltaTime));
    }

    public void tick() {
        worldAction(World::tick);
    }

    public void render() {
        if (debugMode) setWireframe(wireframeMode);
        worldAction(World::render);
    }

    public void show() { window.show(); }
    public void hide() { window.hide(); }

    public void worldAction(WorldAction action) {
        if (currentWorld == null) return;
        action.worldAction(currentWorld);
    }
}

