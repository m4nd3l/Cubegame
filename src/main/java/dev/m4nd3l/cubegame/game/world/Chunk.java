package dev.m4nd3l.cubegame.game.world;

import dev.m4nd3l.cubegame.game.coordinates.ChunkCoordinates;
import dev.m4nd3l.cubegame.toolbox.holders.SubChunkHolder;

public class Chunk extends SubChunkHolder {
    private ChunkCoordinates coordinates;

    public ChunkCoordinates getCoordinates() { return coordinates; }
}
