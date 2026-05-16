package dev.m4nd3l.cubegame.game.coordinates;

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
}
