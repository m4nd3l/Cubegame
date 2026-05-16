package dev.m4nd3l.cubegame.game.coordinates;

public record ChunkCoordinates(int x, int z) implements Coordinates {
    @Override
    public BlockCoordinates toBlock() { return new BlockCoordinates(x() << 4, 0, z() << 4); }

    @Override
    public EntityCoordinates toEntity() { return new EntityCoordinates((x() << 4) + 0.5f, 0.0f, (z() << 4) + 0.5f); }

    @Override
    public SubChunkCoordinates toSubChunk() { return new SubChunkCoordinates(x(), 0, z()); }

    @Override
    public ChunkCoordinates toChunk() { return this; }

    @Override
    public LocalSubChunkCoordinates toLocalSubChunk() { return new LocalSubChunkCoordinates((byte) 0, (byte) 0, (byte) 0); }
}
