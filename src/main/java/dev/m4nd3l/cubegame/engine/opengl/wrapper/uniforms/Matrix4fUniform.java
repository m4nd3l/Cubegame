package dev.m4nd3l.cubegame.engine.opengl.wrapper.uniforms;

import org.joml.Matrix4f;

public class Matrix4fUniform extends Uniform<Matrix4f> {
    public Matrix4fUniform(String uniformName, Matrix4f uniformValue, int shaderID) { super(uniformName, uniformValue, shaderID); }

    @Override
    public void upload() {

    }
}
