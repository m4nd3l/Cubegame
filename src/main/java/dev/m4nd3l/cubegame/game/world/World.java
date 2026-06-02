package dev.m4nd3l.cubegame.game.world;

import dev.m4nd3l.cubegame.engine.communication.WorldRequest;
import dev.m4nd3l.cubegame.engine.coordinates.BlockCoordinates;
import dev.m4nd3l.cubegame.engine.coordinates.Coordinates;
import dev.m4nd3l.cubegame.engine.coordinates.SubChunkCoordinates;
import dev.m4nd3l.cubegame.engine.rendering.renderer.WorldRenderer;
import dev.m4nd3l.cubegame.engine.rendering.renderer.world.subchunk.SubChunkMesher;
import dev.m4nd3l.cubegame.game.blocks.Block;
import dev.m4nd3l.cubegame.game.registries.BlockRegistry;
import dev.m4nd3l.cubegame.game.world.chunks.Chunk;
import dev.m4nd3l.cubegame.game.world.subchunk.SubChunk;
import dev.m4nd3l.cubegame.game.world.world.WorldData;
import dev.m4nd3l.cubegame.game.world.world.util.ChunkManager;
import dev.m4nd3l.cubegame.toolbox.util.FileWrapper;

import static dev.m4nd3l.cubegame.engine.coordinates.ChunkCoordinates.blockToChunk;

public class World extends ChunkManager {
    private WorldData data;
    private WorldRenderer renderer;

    public World(WorldData data, FileWrapper mainFolder) {
        super(data.getPlayer().getPosition().toChunk(), mainFolder);
        this.data = data;
        this.renderer = new WorldRenderer();
    }

    public short placeBlock(BlockCoordinates coordinates, Block block) { return placeBlock(coordinates.x(), coordinates.y(), coordinates.z(), block, true); }
    public short placeBlock(int x, int y, int z, Block block) { return placeBlock(x, y, z, block, true); }
    public short placeBlock(BlockCoordinates coordinates, Block block, boolean setDirty) { return placeBlock(coordinates.x(), coordinates.y(), coordinates.z(), block, setDirty); }
    public short placeBlock(int x, int y, int z, Block block, boolean setDirty) {
        Chunk involved = getChunk(blockToChunk(x), blockToChunk(z));
        if (involved == null) return BlockRegistry.AIR.getID();
        involved.placeBlock(x, y, z, block, this::getChunk, setDirty);
        return block.getID();
    }

    public void digBlock(BlockCoordinates coordinates) { placeBlock(coordinates, BlockRegistry.AIR); }
    public void digBlock(int x, int y, int z) { placeBlock(x, y, z, BlockRegistry.AIR); }
    public void digBlock(BlockCoordinates coordinates, boolean setDirty) { placeBlock(coordinates, BlockRegistry.AIR, setDirty); }
    public void digBlock(int x, int y, int z, boolean setDirty) { placeBlock(x, y, z, BlockRegistry.AIR, setDirty); }

    public void initialize() {
    }

    public void update(double deltaTime) {
        data.getPlayer().update(deltaTime);

        super.update(data.getPlayer().getPosition().toChunk());

        actionOnValues(chunk -> chunk.update(deltaTime, data.getPlayer().getCamera()));

        for (int i = 0; i < 15; i++) {
            if (remeshSubChunkThreads.isReadyQueueEmpty()) continue;
            SubChunkCoordinates coordinates = remeshSubChunkThreads.pollReady();
            Chunk chunk = getChunk(coordinates.toChunk());
            if (chunk == null) continue;
            SubChunk subChunk = chunk.getSubChunk(coordinates);
            if (subChunk == null) continue;
            subChunk.reupload();
        }
    }

    public void tick() { }
    public void render() { renderer.render(getChunks(), data.getPlayer()); }

    public void delete() { super.delete(); renderer.delete(); actionOnValues(Chunk::delete); }

    public void subChunkRequest(Coordinates coordinates, WorldRequest request) {
        SubChunkCoordinates subChunkCoordinates = coordinates.toSubChunk();
        if (subChunkCoordinates == null) return;
        Chunk involvedChunk = getChunk(subChunkCoordinates.toChunk());
        if (involvedChunk == null) return;
        SubChunk involvedSubChunk = involvedChunk.getSubChunk(subChunkCoordinates);
        if (involvedSubChunk == null) return;

        switch (request) {
            case REMESH: 
                remeshSubChunkThreads.addToQueue(subChunkCoordinates, () ->
                    involvedSubChunk.remesh(SubChunkMesher.mesh(subChunkCoordinates, involvedSubChunk.getBlocks(), this::getBlock)));
                break;
            case LOAD: 
                involvedSubChunk.loadRenderer();
                break;
            default:
                break;
        }
    }

    public WorldData getData() { return data; }
}
