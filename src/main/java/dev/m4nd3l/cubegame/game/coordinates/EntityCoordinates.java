package dev.m4nd3l.cubegame.game.coordinates;

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
}
