package dev.m4nd3l.cubegame.engine.rendering.renderer;

import dev.m4nd3l.cubegame.engine.rendering.renderer.world.DefaultRenderer;
import dev.m4nd3l.cubegame.engine.coordinates.ChunkCoordinates;
import dev.m4nd3l.cubegame.game.entities.Player;
import dev.m4nd3l.cubegame.game.world.chunks.Chunk;
import dev.m4nd3l.cubegame.toolbox.containers.EnhancedMap;

public class WorldRenderer {
    private DefaultRenderer defaultRenderer;

    public WorldRenderer() { defaultRenderer = new DefaultRenderer(); }

    public void render(EnhancedMap<ChunkCoordinates, Chunk> chunks, Player player) {
        defaultRenderer.render(chunks, player);
    }

    public void delete() { defaultRenderer.delete(); }
}
