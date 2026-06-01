package dev.m4nd3l.cubegame.game.world.chunks;

import dev.m4nd3l.cubegame.engine.coordinates.BlockCoordinates;
import dev.m4nd3l.cubegame.engine.coordinates.ChunkCoordinates;
import dev.m4nd3l.cubegame.engine.coordinates.Coordinates;
import dev.m4nd3l.cubegame.engine.coordinates.SubChunkCoordinates;
import dev.m4nd3l.cubegame.engine.providers.ChunkProvider;
import dev.m4nd3l.cubegame.engine.providers.WorldProvider;
import dev.m4nd3l.cubegame.engine.rendering.input.Camera;
import dev.m4nd3l.cubegame.engine.rendering.renderer.world.subchunk.SubChunkMesher;
import dev.m4nd3l.cubegame.game.blocks.Block;
import dev.m4nd3l.cubegame.game.registries.BlockRegistry;
import dev.m4nd3l.cubegame.game.world.subchunk.SubChunk;

import static dev.m4nd3l.cubegame.engine.coordinates.ChunkCoordinates.blockToChunk;
import static dev.m4nd3l.cubegame.engine.coordinates.LocalSubChunkCoordinates.blockToLocal;
import static dev.m4nd3l.cubegame.engine.coordinates.SubChunkCoordinates.blockToSubChunk;
import static dev.m4nd3l.cubegame.game.world.subchunk.SubChunk.SUBCHUNK_DIMENSION_SIZE;

public class Chunk extends SubChunkHolder {
    private ChunkCoordinates coordinates;

    public Chunk(Coordinates coordinates) {
        this.coordinates = coordinates.toChunk();
        createSubChunk(this.coordinates.toSubChunk(-4));
        createSubChunk(this.coordinates.toSubChunk(-3));
        createSubChunk(this.coordinates.toSubChunk(-2));
        createSubChunk(this.coordinates.toSubChunk(-1));
        createSubChunk(this.coordinates.toSubChunk(0));
        createSubChunk(this.coordinates.toSubChunk(1));
        createSubChunk(this.coordinates.toSubChunk(2));
        createSubChunk(this.coordinates.toSubChunk(3));
        createSubChunk(this.coordinates.toSubChunk(4));
    }

    public void loadOpenGLData() { actionOnValues(SubChunk::loadRenderer); }

    public short digBlock(BlockCoordinates coordinates, ChunkProvider provider) { return placeBlock(coordinates, BlockRegistry.AIR, provider, true); }
    public short digBlock(int x, int y, int z, ChunkProvider provider) { return placeBlock(x, y, z, BlockRegistry.AIR, provider, true); }
    public short digBlock(BlockCoordinates coordinates, ChunkProvider provider, boolean setDirty) { return placeBlock(coordinates, BlockRegistry.AIR, provider, setDirty); }
    public short digBlock(int x, int y, int z, ChunkProvider provider, boolean setDirty) { return placeBlock(x, y, z, BlockRegistry.AIR, provider, setDirty); }

    public short placeBlock(BlockCoordinates coordinates, Block block, ChunkProvider provider) { return placeBlock(coordinates, block, provider, true); }
    public short placeBlock(int x, int y, int z, Block block, ChunkProvider provider) { return placeBlock(x, y, z, block, provider, true); }
    public short placeBlock(BlockCoordinates coordinates, Block block, ChunkProvider provider, boolean setDirty) { return placeBlock(coordinates.x(), coordinates.y(), coordinates.z(), block, provider, setDirty); }
    public short placeBlock(int x, int y, int z, Block block, ChunkProvider provider, boolean setDirty) {
        SubChunk involved = getSubChunk(blockToChunk(y));
        if (involved == null) return BlockRegistry.AIR.getID();
        involved.placeBlock(blockToLocal(x), blockToLocal(y), blockToLocal(z), block, setDirty);
        if (setDirty) remeshEventualNeighbouringSubChunks(x, y, z, provider);
        return block.getID();
    }

    private void remeshEventualNeighbouringSubChunks(int x, int y, int z, ChunkProvider provider) {
        int currentChunkX = blockToChunk(x);
        int currentChunkZ = blockToChunk(z);
        int currentSubChunkY = blockToSubChunk(y);

        int localX = blockToLocal(x);
        int localY = blockToLocal(y);
        int localZ = blockToLocal(z);

        // X
        if (localX == SUBCHUNK_DIMENSION_SIZE - 1) {
            Chunk neighborChunk = provider.getChunkAt(currentChunkX + 1, currentChunkZ);
            if (neighborChunk != null) {
                SubChunk toRemesh = neighborChunk.getSubChunk(currentSubChunkY);
                if (toRemesh != null) toRemesh.remeshRequest();
            }
        }
        if (localX == 0) {
            Chunk neighborChunk = provider.getChunkAt(currentChunkX - 1, currentChunkZ);
            if (neighborChunk != null) {
                SubChunk toRemesh = neighborChunk.getSubChunk(currentSubChunkY);
                if (toRemesh != null) toRemesh.remeshRequest();
            }
        }

        // Y
        if (localY == SUBCHUNK_DIMENSION_SIZE - 1) {
            Chunk currentChunk = provider.getChunkAt(currentChunkX, currentChunkZ);
            if (currentChunk != null) {
                SubChunk toRemesh = currentChunk.getSubChunk(currentSubChunkY + 1);
                if (toRemesh != null) toRemesh.remeshRequest();
            }
        }
        if (localY == 0) {
            Chunk currentChunk = provider.getChunkAt(currentChunkX, currentChunkZ);
            if (currentChunk != null) {
                SubChunk toRemesh = currentChunk.getSubChunk(currentSubChunkY - 1);
                if (toRemesh != null) toRemesh.remeshRequest();
            }
        }

        // Z
        if (localZ == SUBCHUNK_DIMENSION_SIZE - 1) {
            Chunk neighborChunk = provider.getChunkAt(currentChunkX, currentChunkZ + 1);
            if (neighborChunk != null) {
                SubChunk toRemesh = neighborChunk.getSubChunk(currentSubChunkY);
                if (toRemesh != null) toRemesh.remeshRequest();
            }
        }
        if (localZ == 0) {
            Chunk neighborChunk = provider.getChunkAt(currentChunkX, currentChunkZ - 1);
            if (neighborChunk != null) {
                SubChunk toRemesh = neighborChunk.getSubChunk(currentSubChunkY);
                if (toRemesh != null) toRemesh.remeshRequest();
            }
        }
    }

    public void remeshAll() { actionOnValues(SubChunk::remeshRequest); }
    
    public boolean isMeshing() {
        for (SubChunk subChunk : getValues()) if (subChunk.isMeshing()) return true;
        return false;
    }
    public void createSubChunk(SubChunkCoordinates coordinates) {
        SubChunk subChunk = new SubChunk(coordinates);
        addSubChunk(coordinates, subChunk);
    }

    public void update(double deltaTime, Camera camera) { actionOnValues(subchunk -> subchunk.update(deltaTime, camera)); }
    public void render() { actionOnValues(SubChunk::render); }
    public void delete() { actionOnValues(SubChunk::delete); }

    public ChunkCoordinates getCoordinates() { return coordinates; }

    public void remesh(SubChunkCoordinates subChunkCoordinates, WorldProvider provider) {
        SubChunk toRemesh = getSubChunk(subChunkCoordinates);
        if (toRemesh == null) return;
        toRemesh.remesh(SubChunkMesher.mesh(subChunkCoordinates, toRemesh.getBlocks(), provider));
    }
}
