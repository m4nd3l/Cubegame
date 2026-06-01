package dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper;

import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.mother.OpenGLObject;
import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.uniforms.Uniform;
import dev.m4nd3l.cubegame.toolbox.util.FileWrapper;
import dev.m4nd3l.loggerutil.LoggerUtils;
import dev.m4nd3l.loggerutil.logger.Logger;

import static dev.m4nd3l.cubegame.engine.rendering.opengl.OpenGL.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;

public class ShaderProgram implements OpenGLObject {
    private static Logger LOGGER = LoggerUtils.getLogger();

    enum ShaderType { PROGRAM, SHADER; }

    private int shaderProgramID;
    private FileWrapper vertexFile, fragmentFile;
    private String vertexSource, fragmentSource;

    public ShaderProgram(FileWrapper vertexFile, FileWrapper fragmentFile) {
        this.vertexFile = vertexFile;
        this.vertexSource = vertexFile.readStringFromJarOrDisk();

        this.fragmentFile = fragmentFile;
        this.fragmentSource = fragmentFile.readStringFromJarOrDisk();

        compileAndLink();
    }

    @Override
    public void bind() { bindProgram(shaderProgramID); }
    @Override
    public void unbind() { unbindProgram(); }

    public void uploadUniform(Uniform<?> uniform) { uniform.upload(); }

    @Override
    public void delete() { deleteShader(shaderProgramID); }

    public int getID() { return shaderProgramID; }
    public FileWrapper getFragmentFile() { return fragmentFile; }
    public FileWrapper getVertexFile() { return vertexFile; }

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
