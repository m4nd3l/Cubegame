package dev.m4nd3l.cubegame.toolbox.util;

import dev.m4nd3l.cubegame.engine.coordinates.ChunkCoordinates;

import java.util.Comparator;
import java.util.List;

public class ChunkSorter {

    public static void sortByDistanceToCenter(List<ChunkCoordinates> chunks, int centerX, int centerZ) {
        chunks.sort(Comparator.comparingInt(chunk -> {
            int dx = chunk.x() - centerX;
            int dz = chunk.z() - centerZ;
            return (dx * dx) + (dz * dz);
        }));
    }
}