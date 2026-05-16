package dev.m4nd3l.cubegame.game.coordinates;

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
}
