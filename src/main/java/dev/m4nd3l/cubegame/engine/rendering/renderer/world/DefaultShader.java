package dev.m4nd3l.cubegame.engine.rendering.renderer.world;

import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.ShaderProgram;
import dev.m4nd3l.cubegame.game.saving.SavingLocations;
import dev.m4nd3l.cubegame.toolbox.util.FileWrapper;

public class DefaultShader extends ShaderProgram {
    public DefaultShader() {
        super(new FileWrapper(SavingLocations.ASSETS_FOLDER, "shaders", "default.vert"),
              new FileWrapper(SavingLocations.ASSETS_FOLDER, "shaders", "default.frag"));
    }
}
