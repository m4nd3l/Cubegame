package dev.m4nd3l.cubegame.game.world.subchunk;

import dev.m4nd3l.cubegame.engine.communication.WorldRequest;
import dev.m4nd3l.cubegame.engine.communication.WorldRequestManager;
import dev.m4nd3l.cubegame.engine.coordinates.Coordinates;
import dev.m4nd3l.cubegame.engine.coordinates.SubChunkCoordinates;
import dev.m4nd3l.cubegame.engine.rendering.input.Camera;
import dev.m4nd3l.cubegame.engine.rendering.renderer.world.subchunk.SubChunkRenderer;
import dev.m4nd3l.cubegame.game.blocks.Block;
import dev.m4nd3l.cubegame.game.registries.BlockRegistry;
import dev.m4nd3l.cubegame.game.world.subchunk.util.BlockHolder;
import it.unimi.dsi.fastutil.floats.FloatArrayList;

public class SubChunk extends BlockHolder {
    public static final short SUBCHUNK_DIMENSION_SIZE = 16;
    private SubChunkCoordinates coordinates;
    private SubChunkRenderer renderer;
    private boolean loaded;
    private volatile boolean meshing = false;

    public SubChunk(SubChunkCoordinates coordinates) {
        this.coordinates = coordinates;
        this.loaded = false;
        generateBlocks((coordinates.x() + coordinates.z()) % 2 == 0 ? BlockRegistry.GRASS_BLOCK : BlockRegistry.STONE);
    }

    public void loadRenderer() { this.renderer = new SubChunkRenderer(); }

    public short placeBlock(Coordinates coordinates, Block block) { return placeBlock(coordinates, block, true); }
    public short placeBlock(byte x, byte y, byte z, Block block) { return placeBlock(x, y, z, block, true); }

    public void digBlock(Coordinates coordinates) { digBlock(coordinates, true); }
    public void digBlock(byte x, byte y, byte z) { digBlock(x, y, z, true);  }

    public short placeBlock(Coordinates coordinates, Block block, boolean setDirty) { short ID = placeBlockInArray(coordinates, block); renderer.turnTrueIf(setDirty); return ID; }
    public short placeBlock(byte x, byte y, byte z, Block block, boolean setDirty) { short ID = placeBlockInArray(x, y, z, block); renderer.turnTrueIf(setDirty); return ID; }

    public void digBlock(Coordinates coordinates, boolean setDirty) { digBlockInArray(coordinates); renderer.turnTrueIf(setDirty); }
    public void digBlock(byte x, byte y, byte z, boolean setDirty) { digBlockInArray(x, y, z); renderer.turnTrueIf(setDirty); }

    public void update(double deltaTime, Camera camera) {
        if (renderer == null) { loadRendererRequest(); return; }
        if (renderer.isDirty()) remeshRequest();
        renderer.update(camera, coordinates);
    }

    public void remeshRequest() { 
        if (renderer == null) loadRendererRequest();
        meshing = true;
        WorldRequestManager.request(coordinates, WorldRequest.REMESH); 
        if (renderer != null) renderer.dirty(false); 
    }
    public void loadRendererRequest() { if (!loaded) WorldRequestManager.request(coordinates, WorldRequest.LOAD); loaded = true; }

    public void render() { if (renderer != null) renderer.render(); }
    public void delete() { if (renderer != null) renderer.delete(); }

    public void remesh(FloatArrayList vertices) { renderer.remesh(vertices); renderer.dirty(false); }
    public void reupload() { renderer.uploadToGPU(); meshing = false; }

    public boolean isMeshing() { return meshing; }

    public SubChunkCoordinates getCoordinates() { return coordinates; }
}
