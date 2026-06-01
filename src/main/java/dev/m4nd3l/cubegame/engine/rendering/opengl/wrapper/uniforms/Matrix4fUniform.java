package dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.uniforms;

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import static dev.m4nd3l.cubegame.engine.rendering.opengl.OpenGL.*;

public class Matrix4fUniform extends Uniform<Matrix4f> {
    public Matrix4fUniform(String uniformName, Matrix4f uniformValue, int shaderID) { super(uniformName, uniformValue, shaderID); }

    @Override
    public void upload() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            getValue().get(buffer);
            uploadUniformMatrix4f(getLocation(), buffer);
        }
    }
}
