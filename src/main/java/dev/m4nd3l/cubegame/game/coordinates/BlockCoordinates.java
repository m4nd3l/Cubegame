package dev.m4nd3l.cubegame.game.coordinates;

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
}
