package dev.m4nd3l.cubegame.engine.providers;

import dev.m4nd3l.cubegame.game.world.chunks.Chunk;

@FunctionalInterface public interface ChunkProvider { Chunk getChunkAt(int x, int z); }
