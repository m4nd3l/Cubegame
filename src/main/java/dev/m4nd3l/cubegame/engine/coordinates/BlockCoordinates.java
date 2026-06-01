package dev.m4nd3l.cubegame.engine.coordinates;

import org.joml.Vector3f;

import java.util.Objects;

public record BlockCoordinates(int x, int y, int z) implements Coordinates {
    @Override
    public BlockCoordinates toBlock() { return this; }

    @Override
    public EntityCoordinates toEntity() { return new EntityCoordinates(x() + 0.5f, y(), z() + 0.5f); }

    @Override
    public SubChunkCoordinates toSubChunk() { return new SubChunkCoordinates(x() >> 4, y() >> 4, z() >> 4); }

    @Override
    public ChunkCoordinates toChunk() { return new ChunkCoordinates(x() >> 4, z() >> 4); }

    @Override
    public LocalSubChunkCoordinates toLocalSubChunk() { return new LocalSubChunkCoordinates((byte) (x() & 15), (byte) (y() & 15), (byte) (z() & 15)); }

    @Override
    public Vector3f toVector3f() { return new Vector3f(x(), y(), z()); }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlockCoordinates(int x1, int y1, int z1) &&
                x1 == x() &&
                y1 == y() &&
                z1 == z();
    }

    public boolean equals(int x, int y, int z) { return Objects.equals(x(), x) && Objects.equals(y(), y) && Objects.equals(z(), z); }

    @Override
    public String toString() { return x() + "_" + y() + "_" + z(); }
}
