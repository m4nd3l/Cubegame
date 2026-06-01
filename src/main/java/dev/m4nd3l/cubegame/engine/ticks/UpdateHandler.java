package dev.m4nd3l.cubegame.engine.ticks;

import dev.m4nd3l.cubegame.engine.ticks.actions.FPSReport;
import dev.m4nd3l.cubegame.engine.ticks.actions.RenderAction;
import dev.m4nd3l.cubegame.engine.ticks.actions.TickAction;
import dev.m4nd3l.cubegame.engine.ticks.actions.UpdateAction;
import dev.m4nd3l.cubegame.engine.rendering.input.InputSystem;

import static dev.m4nd3l.cubegame.engine.rendering.glfw.GLFW.*;
import static dev.m4nd3l.cubegame.engine.rendering.opengl.OpenGL.*;

public class UpdateHandler {
    private long glfwWindow;
    private int fpsCounter;
    private double
            lastTime,
            lag,
            tickLag,
            fpsTimer,
            updateTick,
            updateFrame,
            delta,
            idealDelta,
            idealFPS;

    public UpdateHandler(float idealFps, long glfwWindow){
        this.idealDelta = 1.0f / idealFps;
        this.glfwWindow = glfwWindow;
        this.delta = idealDelta;
        this.idealFPS = idealFps;
        this.fpsTimer = 0.0;
        this.fpsCounter = 0;
        this.updateTick = 1.0 / 20.0;
        this.updateFrame = 1.0 / (double) idealFps;
        this.lag = 0.0;
        this.tickLag = 0.0;
    }


    public void startGameLoop(UpdateAction update, TickAction tick, RenderAction render, FPSReport fpsReport) {
        this.lastTime = getTime();

        clearColor(1.0f, 1.0f, 1.0f, 1.0f);
        enableDepthTest();

        while (!windowShouldClose(glfwWindow)) {
            double currentTime = getTime();
            double deltaTime = currentTime - lastTime;
            lastTime = currentTime;

            if (deltaTime > 0.25) deltaTime = 0.25;

            lag += deltaTime;
            tickLag += deltaTime;

            InputSystem.update();

            pollEvents();

            while (tickLag >= updateTick) {
                tick.tick();
                tickLag -= updateTick;
            }

            while (lag >= updateFrame) {
                update.update(updateFrame);
                lag -= updateFrame;
            }

            clearColorDepthBufferBit();

            render.render();

            InputSystem.endFrame();

            swapBuffers(glfwWindow);

            fpsCounter++;

            fpsTimer += deltaTime;
            if (fpsTimer >= 1.0) {
                fpsReport.report(fpsCounter);
                fpsCounter = 0;
                fpsTimer -= 1.0;
            }
        }
    }
    public UpdateHandler setGlfwWindow(long glfwWindow) { this.glfwWindow = glfwWindow; return this; }
}
