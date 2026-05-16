package dev.m4nd3l.cubegame.engine.opengl.wrapper;

import dev.m4nd3l.cubegame.engine.opengl.wrapper.uniforms.Uniform;
import dev.m4nd3l.loggerutil.LoggerUtils;
import dev.m4nd3l.loggerutil.logger.Logger;

import static dev.m4nd3l.cubegame.engine.opengl.OpenGL.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;

public class ShaderProgram {
    private static Logger LOGGER = LoggerUtils.getLogger();

    enum ShaderType { PROGRAM, SHADER; }

    private int shaderProgramID;
    private String vertexSource, fragmentSource;

    public ShaderProgram(String vertexSource, String fragmentSource) {
        this.fragmentSource = fragmentSource;
        this.vertexSource = vertexSource;

        compileAndLink();
    }

    public void bind() { bindProgram(shaderProgramID); }
    public void unbind() { unbindProgram(); }

    public void uploadUniform(Uniform<?> uniform) { uniform.upload(); }

    public void delete() { deleteShader(shaderProgramID); }

    public int getID() { return shaderProgramID; }

    private void compileAndLink() {
        int vertexID = createVertexShader();
        int fragmentID = createFragmentShader();

        setShaderSource(vertexID, vertexSource);
        setShaderSource(fragmentID, fragmentSource);

        compileShader(vertexID);
        compileErrors(vertexID, ShaderType.SHADER);

        compileShader(fragmentID);
        compileErrors(fragmentID, ShaderType.SHADER);

        shaderProgramID = createShaderProgram();

        attachShader(shaderProgramID, vertexID);
        attachShader(shaderProgramID, fragmentID);

        linkProgram(shaderProgramID);
        compileErrors(shaderProgramID, ShaderType.PROGRAM);

        deleteShader(vertexID);
        deleteShader(fragmentID);
    }

    private void compileErrors(int shader, ShaderType type) {
        int hasCompiled;
        int log = 1024;
        String error = "Error while compiling " + type + " shader.";

        if (type == ShaderType.SHADER) hasCompiled = getShaderInfo(shader, GL_COMPILE_STATUS);
        else hasCompiled = getProgramInfo(shader, GL_LINK_STATUS);

        if (hasCompiled == GL_FALSE && type == ShaderType.SHADER) LOGGER.error(error + "\n" + getShaderInfoLog(shader, log));
        if (hasCompiled == GL_FALSE && type == ShaderType.PROGRAM) LOGGER.error(error + "\n" + getProgramInfoLog(shader, log));
    }
}
