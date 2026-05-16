package dev.m4nd3l.cubegame.toolbox.holders;

import dev.m4nd3l.cubegame.game.coordinates.ChunkCoordinates;
import dev.m4nd3l.cubegame.game.coordinates.Coordinates;
import dev.m4nd3l.cubegame.game.coordinates.LocalSubChunkCoordinates;
import dev.m4nd3l.cubegame.game.world.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;

public class ChunkHolder implements Iterable<Map.Entry<ChunkCoordinates, Chunk>> {
    private Map<ChunkCoordinates, Chunk> chunks;

    public ChunkHolder() { this.chunks = new HashMap<>(); }
    public ChunkHolder(Map<ChunkCoordinates, Chunk> chunks) { this.chunks = new HashMap<>(chunks); }

    public Chunk getChunk(Coordinates coordinates) {
        if (coordinates instanceof LocalSubChunkCoordinates) return null;
        ChunkCoordinates chunkCoordinates = coordinates.toChunk();
        if (chunks.containsKey(chunkCoordinates))
            return chunks.entrySet().stream()
                    .filter(chunkEntry -> chunkEntry.getKey().equals(chunkCoordinates))
                    .findFirst()
                    .orElse(null)
                    .getValue();
        return null;
    }

    public void addChunk(Chunk chunk) { chunks.put(chunk.getCoordinates(), chunk); }
    public void addChunk(ChunkCoordinates coordinates, Chunk chunk) { chunks.put(coordinates, chunk); }

    public void removeChunk(Chunk chunk) { chunks.remove(chunk.getCoordinates(), chunk); }
    public void removeChunk(ChunkCoordinates coordinates) { chunks.remove(coordinates); }
    public void removeChunk(ChunkCoordinates coordinates, Chunk chunk) { chunks.remove(coordinates, chunk); }

    public boolean containsChunk(Coordinates coordinates) { return chunks.containsKey(coordinates.toChunk()); }
    public boolean containsChunk(Chunk chunk) { return chunks.containsValue(chunk); }

    @NotNull @Override public Iterator<Map.Entry<ChunkCoordinates, Chunk>> iterator() { return chunks.entrySet().iterator(); }
    @Override public Spliterator<Map.Entry<ChunkCoordinates, Chunk>> spliterator() { return chunks.entrySet().spliterator(); }
    public void forEach(BiConsumer<? super ChunkCoordinates, ? super Chunk> action) { chunks.forEach(action); }
}
