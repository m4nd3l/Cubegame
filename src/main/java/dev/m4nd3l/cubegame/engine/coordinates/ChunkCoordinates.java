package dev.m4nd3l.cubegame.engine.coordinates;

import org.joml.Vector3f;

import java.util.Objects;

public record ChunkCoordinates(int x, int z) implements Coordinates {
    @Override
    public BlockCoordinates toBlock() { return new BlockCoordinates(x() << 4, 0, z() << 4); }

    @Override
    public EntityCoordinates toEntity() { return new EntityCoordinates((x() << 4) + 0.5f, 0.0f, (z() << 4) + 0.5f); }

    @Override
    public SubChunkCoordinates toSubChunk() { return new SubChunkCoordinates(x(), 0, z()); }

    public SubChunkCoordinates toSubChunk(int y) { return new SubChunkCoordinates(x(), y, z()); }

    @Override
    public ChunkCoordinates toChunk() { return this; }

    @Override
    public LocalSubChunkCoordinates toLocalSubChunk() { return new LocalSubChunkCoordinates((byte) 0, (byte) 0, (byte) 0); }

    @Override
    public Vector3f toVector3f() { return new Vector3f(x(), 0, z()); }

    public static int blockToChunk(int coordinate) { return coordinate >> 4; }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ChunkCoordinates(int x1, int z1) &&
                x1 == x() &&
                z1 == z();
    }

    public boolean equals(int x, int z) { return Objects.equals(x(), x) && Objects.equals(z(), z); }

    @Override
    public String toString() { return x() + "_" + z(); }
}
