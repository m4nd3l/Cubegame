package dev.m4nd3l.cubegame.toolbox.holders;

import dev.m4nd3l.cubegame.game.coordinates.Coordinates;
import dev.m4nd3l.cubegame.game.coordinates.LocalSubChunkCoordinates;
import dev.m4nd3l.cubegame.game.coordinates.SubChunkCoordinates;
import dev.m4nd3l.cubegame.game.world.SubChunk;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.BiConsumer;

public class SubChunkHolder implements Iterable<Map.Entry<SubChunkCoordinates, SubChunk>>{
    private Map<SubChunkCoordinates, SubChunk> subChunks;

    public SubChunkHolder() { this.subChunks = new HashMap<>(); }
    public SubChunkHolder(Map<SubChunkCoordinates, SubChunk> chunks) { this.subChunks = new HashMap<>(chunks); }

    public SubChunk getSubChunk(Coordinates coordinates) {
        if (coordinates instanceof LocalSubChunkCoordinates) return null;
        SubChunkCoordinates subChunkCoordinates = coordinates.toSubChunk();
        if (subChunks.containsKey(subChunkCoordinates))
            return subChunks.entrySet().stream()
                    .filter(subChunkEntry -> subChunkEntry.getKey().equals(subChunkCoordinates))
                    .findFirst()
                    .orElse(null)
                    .getValue();
        return null;
    }

    public void addChunk(SubChunk subChunk) { subChunks.put(subChunk.getCoordinates(), subChunk); }
    public void addChunk(SubChunkCoordinates coordinates, SubChunk subChunk) { subChunks.put(coordinates, subChunk); }

    public void removeChunk(SubChunk subChunk) { subChunks.remove(subChunk.getCoordinates(), subChunk); }
    public void removeChunk(SubChunkCoordinates coordinates) { subChunks.remove(coordinates); }
    public void removeChunk(SubChunkCoordinates coordinates, SubChunk subChunk) { subChunks.remove(coordinates, subChunk); }

    public boolean containsChunk(Coordinates coordinates) { return subChunks.containsKey(coordinates.toChunk()); }
    public boolean containsChunk(SubChunk subChunk) { return subChunks.containsValue(subChunk); }

    @NotNull
    @Override public Iterator<Map.Entry<SubChunkCoordinates, SubChunk>> iterator() { return subChunks.entrySet().iterator(); }
    @Override public Spliterator<Map.Entry<SubChunkCoordinates, SubChunk>> spliterator() { return subChunks.entrySet().spliterator(); }
    public void forEach(BiConsumer<? super SubChunkCoordinates, ? super SubChunk> action) { subChunks.forEach(action); }

}
