package dev.m4nd3l.cubegame.engine.providers;

import dev.m4nd3l.cubegame.game.blocks.Block;

@FunctionalInterface public interface WorldProvider { Block getBlockAt(int x, int y, int z); }
