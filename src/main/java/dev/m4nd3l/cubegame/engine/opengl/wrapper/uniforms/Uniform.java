package dev.m4nd3l.cubegame.engine.opengl.wrapper.uniforms;

import dev.m4nd3l.loggerutil.LoggerUtils;
import dev.m4nd3l.loggerutil.logger.Logger;

import static dev.m4nd3l.cubegame.engine.opengl.OpenGL.*;

public abstract class Uniform<T> {
    private static Logger LOGGER = LoggerUtils.getLogger();
    private T uniformValue;
    private String uniformName;
    private int uniformLocation;

    public Uniform(String uniformName, T uniformValue, int shaderID) {
        this.uniformName = uniformName;
        this.uniformValue = uniformValue;
        this.uniformLocation = getLocation(shaderID);
    }

    private int getLocation(int shaderID) {
        String errorMessage = "Uniform with name '" + uniformName + "' hasn't been found in shader " + shaderID + ".";
        int location = getUniformLocation(uniformName, shaderID);
        if (location == -1) LOGGER.error(errorMessage);
        return location;
    }

    public abstract void upload();

    public int getLocation() { return uniformLocation; }
    public T getValue() { return uniformValue; }
}
