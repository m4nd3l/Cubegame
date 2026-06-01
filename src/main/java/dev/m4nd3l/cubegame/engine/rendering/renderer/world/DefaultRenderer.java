package dev.m4nd3l.cubegame.engine.rendering.renderer.world;

import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.TextureArray;
import dev.m4nd3l.cubegame.engine.rendering.renderer.Renderer;
import dev.m4nd3l.cubegame.engine.coordinates.ChunkCoordinates;
import dev.m4nd3l.cubegame.game.entities.Player;
import dev.m4nd3l.cubegame.game.registries.BlockRegistry;
import dev.m4nd3l.cubegame.game.world.chunks.Chunk;
import dev.m4nd3l.cubegame.toolbox.containers.EnhancedMap;

public class DefaultRenderer extends Renderer {
    private TextureArray array;

    public DefaultRenderer() {
        shader = new DefaultShader();
        array = new TextureArray(BlockRegistry.blockTextures);
    }

    public void render(EnhancedMap<ChunkCoordinates, Chunk> chunks, Player player) {
        actionBetweenBinding(() -> {
            player.getCamera().uploadUniforms(shader);
            chunks.actionOnValues(Chunk::render);
        }, shader, array);
    }

    public void delete() { unbindAndDelete(shader, array); }
}
