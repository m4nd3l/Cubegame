package dev.m4nd3l.cubegame.engine;

import dev.m4nd3l.cubegame.engine.interaction.WorldAction;
import dev.m4nd3l.cubegame.engine.rendering.glfw.Window;
import dev.m4nd3l.cubegame.engine.rendering.input.KeyboardKeys;
import dev.m4nd3l.cubegame.game.registries.BlockRegistry;
import dev.m4nd3l.cubegame.game.world.World;
import dev.m4nd3l.cubegame.engine.ticks.UpdateHandler;
import dev.m4nd3l.cubegame.game.world.world.WorldData;
import dev.m4nd3l.cubegame.engine.rendering.input.InputSystem;
import dev.m4nd3l.cubegame.game.world.world.WorldLoader;
import dev.m4nd3l.cubegame.toolbox.ConvertingTool;
import dev.m4nd3l.loggerutil.LoggerUtils;
import dev.m4nd3l.loggerutil.logger.Logger;

import static dev.m4nd3l.cubegame.engine.rendering.opengl.OpenGL.*;

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
        InputSystem.init(window.getGlfwWindow());
        handler.setGlfwWindow(window.getGlfwWindow());
        loadWorld("");
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
    public void loadWorld(String worldName, long seed) { currentWorld = WorldLoader.loadWorld(worldName, seed); }

    public void update(double deltaTime) { worldAction(world -> world.update(deltaTime)); }

    public void tick() { worldAction(World::tick); }

    public void render() {
        WorldData data = getData();
        if (debugMode && InputSystem.isAltDown() && InputSystem.isKeyPressed(KeyboardKeys.H)) wireframeMode = !wireframeMode;
        if (debugMode && InputSystem.isAltDown() && InputSystem.isKeyPressed(KeyboardKeys.L) && data != null)
            data.getPlayer().getCamera().frustumFreeze = !data.getPlayer().getCamera().frustumFreeze;
        if (debugMode) setWireframe(wireframeMode);
        if (InputSystem.isKeyPressed(KeyboardKeys.K)) worldAction(world -> world.placeBlock(0, 0, 0, BlockRegistry.GRASS_BLOCK));
        if (InputSystem.isKeyPressed(KeyboardKeys.J)) worldAction(world -> world.digBlock(0, 0, 0));

        worldAction(World::render);
    }

    public void show() { window.show(); }
    public void hide() { window.hide(); }

    public WorldData getData() { if (currentWorld != null) return currentWorld.getData(); return null; }
    public void worldAction(WorldAction action) {
        if (currentWorld != null) action.worldAction(currentWorld);
    }

    public long getWindow() { return window.getGlfwWindow(); }
}

