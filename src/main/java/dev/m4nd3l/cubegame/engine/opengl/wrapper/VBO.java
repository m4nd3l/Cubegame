package dev.m4nd3l.cubegame.engine.opengl.wrapper;

import java.nio.FloatBuffer;

import static dev.m4nd3l.cubegame.engine.opengl.OpenGL.*;
import static org.lwjgl.opengl.GL15.*;

public class VBO {
    private int vboID;

    public VBO() { vboID = generateVBO(); }

    public void uploadDataDynamicDraw(float[] data) { uploadData(data, GL_DYNAMIC_DRAW); }
    public void uploadDataDynamicDraw(FloatBuffer data) { uploadData(data, GL_DYNAMIC_DRAW); }
    public void uploadDataStaticDraw(float[] data) { uploadData(data, GL_STATIC_DRAW); }
    public void uploadDataStaticDraw(FloatBuffer data) { uploadData(data, GL_STATIC_DRAW); }
    public void uploadData(float[] data, int mode) { uploadData(data, mode); }
    public void uploadData(FloatBuffer data, int mode) { uploadData(data, mode); }

    public void bind() { bindVBO(vboID); }
    public void unbind() { unbindVBO(); }

    public void delete() { deleteVBO(vboID); }
}
