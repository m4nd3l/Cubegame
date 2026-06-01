package dev.m4nd3l.cubegame.engine.communication;

import dev.m4nd3l.cubegame.engine.coordinates.Coordinates;
import dev.m4nd3l.cubegame.game.Cubegame;

public class WorldRequestManager {
    public static void request(Coordinates coordinates, WorldRequest request) {
        Cubegame.CUBEGAME.worldAction(world -> world.subChunkRequest(coordinates, request));
    }
}
