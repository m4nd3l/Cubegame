package dev.m4nd3l.cubegame.game.ticks;

import dev.m4nd3l.cubegame.game.ticks.actions.FPSReport;
import dev.m4nd3l.cubegame.game.ticks.actions.RenderAction;
import dev.m4nd3l.cubegame.game.ticks.actions.TickAction;
import dev.m4nd3l.cubegame.game.ticks.actions.UpdateAction;

import static dev.m4nd3l.cubegame.engine.glfw.GLFW.*;
import static dev.m4nd3l.cubegame.engine.opengl.OpenGL.*;

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
            lag += deltaTime;
            tickLag += deltaTime;

            pollEvents();

            clearColor(1.0f, 1.0f, 1.0f, 1.0f);

            if (lag > 0.25) lag = 0.25;

            while (tickLag >= updateTick) {
                tick.tick();
                tickLag -= updateTick;
            }

            while (lag >= updateFrame) {
                update.update(updateFrame);
                lag -= updateFrame;
                fpsCounter++;
            }

            clearColorDepthBufferBit();

            render.render();

            swapBuffers(glfwWindow);

            fpsTimer += deltaTime;
            if (fpsTimer >= 1.0) {
                fpsReport.report(fpsCounter);
                fpsCounter = 0;
                fpsTimer = 0;
            }
        }
    }

    public UpdateHandler setGlfwWindow(long glfwWindow) { this.glfwWindow = glfwWindow; return this; }
}
