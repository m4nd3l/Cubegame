package dev.m4nd3l.cubegame.engine.coordinates;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public interface Coordinates {
    @Nullable BlockCoordinates toBlock();
    @Nullable EntityCoordinates toEntity();
    @Nullable SubChunkCoordinates toSubChunk();
    @Nullable ChunkCoordinates toChunk();
    LocalSubChunkCoordinates toLocalSubChunk();
    Vector3f toVector3f();
}
