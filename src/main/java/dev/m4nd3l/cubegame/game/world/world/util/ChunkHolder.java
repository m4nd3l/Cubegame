package dev.m4nd3l.cubegame.game.world.world.util;

import dev.m4nd3l.cubegame.engine.coordinates.ChunkCoordinates;
import dev.m4nd3l.cubegame.engine.coordinates.Coordinates;
import dev.m4nd3l.cubegame.engine.coordinates.LocalSubChunkCoordinates;
import dev.m4nd3l.cubegame.game.blocks.Block;
import dev.m4nd3l.cubegame.game.registries.BlockRegistry;
import dev.m4nd3l.cubegame.game.world.chunks.Chunk;
import dev.m4nd3l.cubegame.game.world.subchunk.SubChunk;
import dev.m4nd3l.cubegame.toolbox.containers.EnhancedMap;
import dev.m4nd3l.cubegame.toolbox.containers.ActionOnKeys;
import dev.m4nd3l.cubegame.toolbox.containers.ActionOnValues;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static dev.m4nd3l.cubegame.engine.coordinates.ChunkCoordinates.blockToChunk;
import static dev.m4nd3l.cubegame.engine.coordinates.LocalSubChunkCoordinates.blockToLocal;

public class ChunkHolder implements Iterable<Map.Entry<ChunkCoordinates, Chunk>> {
    private EnhancedMap<ChunkCoordinates, Chunk> chunks;

    public ChunkHolder() { this.chunks = new EnhancedMap<>(new ConcurrentHashMap<>()); }
    public ChunkHolder(Map<ChunkCoordinates, Chunk> chunks) { this.chunks = new EnhancedMap<>(new ConcurrentHashMap<>(chunks)); }

    public Chunk getChunk(Coordinates coordinates) {
        if (coordinates instanceof LocalSubChunkCoordinates) return null;
        return chunks.get(coordinates.toChunk());
    }

    public Chunk getChunk(int x, int z) { return chunks.get(new ChunkCoordinates(x, z)); }

    public Block getBlock(int x, int y, int z) {
        Chunk chunk = getChunk(blockToChunk(x), blockToChunk(z));
        if (chunk == null) return BlockRegistry.AIR;
        SubChunk subChunk = chunk.getSubChunk(blockToChunk(y));
        if (subChunk == null) return BlockRegistry.AIR;
        return subChunk.getBlock(blockToLocal(x), blockToLocal(y), blockToLocal(z));
    }

    public void addChunk(Chunk chunk) { chunks.put(chunk.getCoordinates(), chunk); }
    public void addChunk(ChunkCoordinates coordinates, Chunk chunk) { chunks.put(coordinates, chunk); }

    public void actionOnKeys(ActionOnKeys<ChunkCoordinates> action) { chunks.actionOnKeys(action); }
    public void actionOnValues(ActionOnValues<Chunk> action) { chunks.actionOnValues(action); }
    public void action(BiConsumer<? super ChunkCoordinates, ? super Chunk> action) { chunks.action(action); }

    public void removeChunk(Chunk chunk) { chunks.remove(chunk.getCoordinates(), chunk); }
    public void removeChunk(ChunkCoordinates coordinates) { chunks.remove(coordinates); }
    public void removeChunk(ChunkCoordinates coordinates, Chunk chunk) { chunks.remove(coordinates, chunk); }

    public boolean containsChunk(Coordinates coordinates) { return chunks.containsKey(coordinates.toChunk()); }
    public boolean containsChunk(Chunk chunk) { return chunks.containsValue(chunk); }

    public void removeIf(Predicate<Chunk> condition) {
        chunks.actionOnValues(chunk -> { if (condition.test(chunk)) chunks.remove(chunk.getCoordinates()); });
    }

    public EnhancedMap<ChunkCoordinates, Chunk> getChunks() { return chunks; }

    @NotNull @Override public Iterator<Map.Entry<ChunkCoordinates, Chunk>> iterator() { return chunks.getEntrySet().iterator(); }
    @Override public Spliterator<Map.Entry<ChunkCoordinates, Chunk>> spliterator() { return chunks.getEntrySet().spliterator(); }
}
