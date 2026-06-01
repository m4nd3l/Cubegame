package dev.m4nd3l.cubegame.game.world.subchunk.util;

import dev.m4nd3l.cubegame.engine.coordinates.Coordinates;
import dev.m4nd3l.cubegame.engine.coordinates.LocalSubChunkCoordinates;
import dev.m4nd3l.cubegame.game.blocks.Block;
import dev.m4nd3l.cubegame.game.registries.BlockRegistry;

import static dev.m4nd3l.cubegame.game.world.subchunk.SubChunk.SUBCHUNK_DIMENSION_SIZE;

public class BlockHolder {
    private static final short NON_STATIC_BLOCK_ID = -1;
    private short[] blocks;

    public BlockHolder() {
        this.blocks = new short[SUBCHUNK_DIMENSION_SIZE * SUBCHUNK_DIMENSION_SIZE * SUBCHUNK_DIMENSION_SIZE];
    }

    public void generateBlocks(Block filler) {
        for (byte x = 0; x < SUBCHUNK_DIMENSION_SIZE; x++) {
            for (byte y = 0; y < SUBCHUNK_DIMENSION_SIZE; y++) {
                for (byte z = 0; z < SUBCHUNK_DIMENSION_SIZE; z++) {
                    placeBlockInArray(x, y, z, filler);
                }
            }
        }
    }

    public short placeBlockInArray(Coordinates coordinates, Block block) {
        LocalSubChunkCoordinates localCoordinates = coordinates.toLocalSubChunk();
        return placeBlockInArray(localCoordinates.x(), localCoordinates.y(), localCoordinates.z(), block);
    }

    public short placeBlockInArray(byte x, byte y, byte z, Block block) {
        blocks[getIndex(x, y, z)] = block.getID();
        return block.getID();
    }

    public void digBlockInArray(Coordinates coordinates) { placeBlockInArray(coordinates, BlockRegistry.AIR); }
    public void digBlockInArray(byte x, byte y, byte z) { placeBlockInArray(x, y, z, BlockRegistry.AIR); }

    public Block getBlock(byte x, byte y, byte z) { return BlockRegistry.getBlock(blocks[getIndex(x, y, z)]); }
    private int getIndex(byte x, byte y, byte z) { return x + (y * SUBCHUNK_DIMENSION_SIZE) + (z * SUBCHUNK_DIMENSION_SIZE * SUBCHUNK_DIMENSION_SIZE); }

    public short[] getBlocks() { return blocks; }
}
