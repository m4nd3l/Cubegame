package dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper;

import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.mother.OpenGLObject;

import java.nio.FloatBuffer;

import static dev.m4nd3l.cubegame.engine.rendering.opengl.OpenGL.*;
import static org.lwjgl.opengl.GL15.*;

public class VBO implements OpenGLObject {
    private int vboID;

    public VBO() { vboID = generateVBO(); }

    public void uploadDataDynamicDraw(float[] data) { uploadData(data, GL_DYNAMIC_DRAW); }
    public void uploadDataDynamicDraw(FloatBuffer data) { uploadData(data, GL_DYNAMIC_DRAW); }
    public void uploadDataStaticDraw(float[] data) { uploadData(data, GL_STATIC_DRAW); }
    public void uploadDataStaticDraw(FloatBuffer data) { uploadData(data, GL_STATIC_DRAW); }
    public void uploadData(float[] data, int mode) { addDataToVBO(data, mode); }
    public void uploadData(FloatBuffer data, int mode) { addDataToVBO(data, mode); }

    @Override
    public void bind() { bindVBO(vboID); }
    @Override
    public void unbind() { unbindVBO(); }

    @Override
    public void delete() { deleteVBO(vboID); }
}
