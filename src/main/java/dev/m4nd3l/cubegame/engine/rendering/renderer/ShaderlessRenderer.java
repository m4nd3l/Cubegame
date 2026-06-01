package dev.m4nd3l.cubegame.engine.rendering.renderer;

import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.mother.OpenGLObject;

public class ShaderlessRenderer {
    protected void actionBetweenBinding(Runnable action, OpenGLObject... objects) {
        for (OpenGLObject object : objects) if (object != null) object.bind();
        action.run();
        for (int i = objects.length - 1; i >= 0; i--) if (objects[i] != null) objects[i].unbind();
    }

    protected void unbindAndDelete(OpenGLObject... objects) {
        for (OpenGLObject object : objects) {
            if (object == null) continue;
            object.unbind();
            object.delete();
        }
    }
}
