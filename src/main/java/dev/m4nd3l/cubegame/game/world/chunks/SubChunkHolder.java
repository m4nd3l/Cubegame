package dev.m4nd3l.cubegame.game.world.chunks;

import dev.m4nd3l.cubegame.engine.coordinates.Coordinates;
import dev.m4nd3l.cubegame.engine.coordinates.LocalSubChunkCoordinates;
import dev.m4nd3l.cubegame.engine.coordinates.SubChunkCoordinates;
import dev.m4nd3l.cubegame.game.blocks.Block;
import dev.m4nd3l.cubegame.game.world.subchunk.SubChunk;
import dev.m4nd3l.cubegame.toolbox.containers.ActionOnKeys;
import dev.m4nd3l.cubegame.toolbox.containers.ActionOnValues;
import dev.m4nd3l.cubegame.toolbox.containers.EnhancedMap;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;

public class SubChunkHolder implements Iterable<Map.Entry<SubChunkCoordinates, SubChunk>>{
    private EnhancedMap<SubChunkCoordinates, SubChunk> subChunks;

    public SubChunkHolder() { this.subChunks = new EnhancedMap<>(); }
    public SubChunkHolder(Map<SubChunkCoordinates, SubChunk> chunks) { this.subChunks = new EnhancedMap<>(chunks); }

    public SubChunk getSubChunk(Coordinates coordinates) {
        if (coordinates instanceof LocalSubChunkCoordinates) return null;
        SubChunkCoordinates subChunkCoordinates = coordinates.toSubChunk();
        if (subChunks.containsKey(subChunkCoordinates))
            return subChunks.getValueSet().stream()
                    .filter(subChunk -> subChunk.getCoordinates().equals(subChunkCoordinates))
                    .findFirst()
                    .orElse(null);
        return null;
    }
    public SubChunk getSubChunk(int y) {
        return subChunks.getValueSet().stream()
                .filter(subChunk -> subChunk.getCoordinates().equals(y))
                .findFirst()
                .orElse(null);
    }

    public void addSubChunk(SubChunk subChunk) { subChunks.put(subChunk.getCoordinates(), subChunk); }
    public void addSubChunk(SubChunkCoordinates coordinates, SubChunk subChunk) { subChunks.put(coordinates, subChunk); }

    public void removeSubChunk(SubChunk subChunk) { subChunks.remove(subChunk.getCoordinates(), subChunk); }
    public void removeSubChunk(SubChunkCoordinates coordinates) { subChunks.remove(coordinates); }
    public void removeSubChunk(SubChunkCoordinates coordinates, SubChunk subChunk) { subChunks.remove(coordinates, subChunk); }

    public boolean containsSubChunk(Coordinates coordinates) { return subChunks.containsKey(coordinates.toSubChunk()); }
    public boolean containsSubChunk(SubChunk subChunk) { return subChunks.containsValue(subChunk); }

    public void actionOnKeys(ActionOnKeys<SubChunkCoordinates> action) { subChunks.actionOnKeys(action); }
    public void actionOnValues(ActionOnValues<SubChunk> action) { subChunks.actionOnValues(action); }
    public void action(BiConsumer<? super SubChunkCoordinates, ? super SubChunk> action) { subChunks.action(action); }

    public Set<SubChunk> getValues() { return subChunks.getValueSet(); }
    public Set<SubChunkCoordinates> getKeys() { return subChunks.getKeySet(); }

    @NotNull
    @Override public Iterator<Map.Entry<SubChunkCoordinates, SubChunk>> iterator() { return subChunks.getEntrySet().iterator(); }
    @Override public Spliterator<Map.Entry<SubChunkCoordinates, SubChunk>> spliterator() { return subChunks.getEntrySet().spliterator(); }
}
