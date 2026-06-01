package dev.m4nd3l.cubegame.game.entities;

import dev.m4nd3l.cubegame.engine.coordinates.Coordinates;
import dev.m4nd3l.cubegame.engine.rendering.input.Camera;
import dev.m4nd3l.cubegame.game.Cubegame;

public class Player {
    private Camera camera;

    public Player() { }
    public Player(Camera camera) { this.camera = camera; }

    public void update(double deltaTime) {
        getCamera().processKeyboard(deltaTime);
        getCamera().processMouseMovement(Cubegame.CUBEGAME.getWindow());

        getCamera().updateMatrices();
        getCamera().updateFrustum();
    }

    public Camera getCamera() { return camera; }

    public Coordinates getPosition() { return getCamera().getCoordinates(); }
}
