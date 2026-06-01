package dev.m4nd3l.cubegame.engine.coordinates;

import org.joml.Vector3f;

import java.util.Objects;

public record SubChunkCoordinates(int x, int y, int z) implements Coordinates {
    @Override
    public BlockCoordinates toBlock() { return new BlockCoordinates(x() << 4, y() << 4, z() << 4); }

    @Override
    public EntityCoordinates toEntity() { return new EntityCoordinates((x() << 4) + 0.5f, (y() << 4), (z() << 4) + 0.5f); }

    @Override
    public SubChunkCoordinates toSubChunk() { return this; }

    @Override
    public ChunkCoordinates toChunk() { return new ChunkCoordinates(x(), z()); }

    @Override
    public LocalSubChunkCoordinates toLocalSubChunk() { return new LocalSubChunkCoordinates((byte) 0, (byte) 0, (byte) 0); }

    @Override
    public Vector3f toVector3f() { return new Vector3f(x(), y(), z()); }

    public static int blockToSubChunk(int coordinate) { return coordinate >> 4; }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SubChunkCoordinates(int x1, int y1, int z1) &&
                x1 == x() &&
                y1 == y() &&
                z1 == z();
    }

    public boolean equals(int x, int y, int z) { return Objects.equals(x(), x) && Objects.equals(y(), y) && Objects.equals(z(), z); }
    public boolean equals(int y) { return Objects.equals(y(), y); }

    @Override
    public String toString() { return x() + "_" + y() + "_" + z(); }
}
