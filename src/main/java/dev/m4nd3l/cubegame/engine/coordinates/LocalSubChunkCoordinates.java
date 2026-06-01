package dev.m4nd3l.cubegame.engine.coordinates;

import org.joml.Vector3f;

import java.util.Objects;

public record LocalSubChunkCoordinates(byte x, byte y, byte z) implements Coordinates {
    @Override
    public BlockCoordinates toBlock() { return null; }

    @Override
    public EntityCoordinates toEntity() { return null; }

    @Override
    public SubChunkCoordinates toSubChunk() { return null; }

    @Override
    public ChunkCoordinates toChunk() { return null; }

    @Override
    public LocalSubChunkCoordinates toLocalSubChunk() { return this; }

    public BlockCoordinates toBlock(SubChunkCoordinates context) {
        return new BlockCoordinates(
                (context.x() << 4) + this.x,
                (context.y() << 4) + this.y,
                (context.z() << 4) + this.z
        );
    }

    public EntityCoordinates toEntity(SubChunkCoordinates context) {
        return new EntityCoordinates(
                (context.x() << 4) + this.x + 0.5f,
                (context.y() << 4) + this.y,
                (context.z() << 4) + this.z + 0.5f
        );
    }

    @Override
    public Vector3f toVector3f() { return new Vector3f(x(), y(), z()); }

    public static byte blockToLocal(int coordinate) { return (byte) (coordinate & 15); }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LocalSubChunkCoordinates(byte x1, byte y1, byte z1) &&
                x1 == x() &&
                y1 == y() &&
                z1 == z();
    }

    public boolean equals(byte x, byte y, byte z) { return Objects.equals(x(), x) && Objects.equals(y(), y) && Objects.equals(z(), z); }

    @Override
    public String toString() { return x() + "_" + y() + "_" + z(); }
}
