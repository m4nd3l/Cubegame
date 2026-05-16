package dev.m4nd3l.cubegame.game.coordinates;

public interface Coordinates {
    BlockCoordinates toBlock();
    EntityCoordinates toEntity();
    SubChunkCoordinates toSubChunk();
    ChunkCoordinates toChunk();
    LocalSubChunkCoordinates toLocalSubChunk();
}
