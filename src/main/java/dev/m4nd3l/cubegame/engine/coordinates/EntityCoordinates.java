package dev.m4nd3l.cubegame.engine.coordinates;

import org.joml.Vector3f;

import java.util.Objects;

public record EntityCoordinates(float x, float y, float z) implements Coordinates {
    @Override
    public BlockCoordinates toBlock() { return new BlockCoordinates((int) Math.floor(x()), (int) Math.floor(y()), (int) Math.floor(z())); }

    @Override
    public EntityCoordinates toEntity() { return this; }

    @Override
    public SubChunkCoordinates toSubChunk() { return toBlock().toSubChunk(); }

    @Override
    public ChunkCoordinates toChunk() { return toBlock().toChunk(); }

    @Override
    public LocalSubChunkCoordinates toLocalSubChunk() { return toBlock().toLocalSubChunk(); }

    @Override
    public Vector3f toVector3f() { return new Vector3f(x(), y(), z()); }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EntityCoordinates(float x1, float y1, float z1) &&
                x1 == x() &&
                y1 == y() &&
                z1 == z();
    }

    public boolean equals(float x, float y, float z) { return Objects.equals(x(), x) && Objects.equals(y(), y) && Objects.equals(z(), z); }

    @Override
    public String toString() { return x() + "_" + y() + "_" + z(); }
}
