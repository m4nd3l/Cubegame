package dev.m4nd3l.cubegame.engine.opengl;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

public class OpenGL {
    // WINDOW
    public static void setViewport(int x, int y, int width, int height) { glViewport(x, y, width, height); }
    public static void swapBuffers(long window) { glfwSwapBuffers(window); }
    public static void pollEvents() { glfwPollEvents(); }
    public static void clearColor(float r, float g, float b, float a) { glClearColor(r, g, b, a); }
    public static void clearColorBuffer() { clear(GL_COLOR_BUFFER_BIT); }
    public static void clear(int mask) { glClear(mask); }
    public static void clearColorDepthBufferBit() { clear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); }
    public static void enable(int mode) { glEnable(mode); }
    public static void enableDepthTest() { enable(GL_DEPTH_TEST); }

    // DRAW
    public static void drawArrays(int mode, int first, int count) { glDrawArrays(mode, first, count); }
    public static void setPolygonModeFrontAndBack(int mode) { glPolygonMode(GL_FRONT_AND_BACK, mode); }
    public static void setWireframe(boolean wireframe) { setPolygonModeFrontAndBack(wireframe ? GL_LINE : GL_FILL); }

    // VAO
    public static int generateVAO() { return glGenVertexArrays(); }
    public static void addAttributePointer(int layout, int size, int type, boolean normalized, int stride, long pointer) { glVertexAttribPointer(layout, size, type, normalized, stride, pointer); }
    public static void enableAttributePointer(int layout) { glEnableVertexAttribArray(layout); }
    public static void disableAttributePointer(int layout) { glDisableVertexAttribArray(layout); }
    public static void bindVAO(int id) { glBindVertexArray(id); }
    public static void unbindVAO() { glBindVertexArray(0); }
    public static void deleteVAO(int id) { glDeleteVertexArrays(id); }

    // VBO
    public static int generateVBO() { return generateBuffers(); }
    public static void addDataToVBO(float[] data, int use) { bufferData(GL_ARRAY_BUFFER, data, use); }
    public static void addDataToVBO(FloatBuffer data, int use) { bufferData(GL_ARRAY_BUFFER, data, use); }
    public static void bindVBO(int id) { bindBuffer(GL_ARRAY_BUFFER, id); }
    public static void unbindVBO() { unbindBuffer(GL_ARRAY_BUFFER); }
    public static void deleteVBO(int id) { deleteBuffer(id); }

    // SHADER
    public static int createShader(int type) { return glCreateShader(type); }
    public static int createVertexShader() { return glCreateShader(GL_VERTEX_SHADER); }
    public static int createFragmentShader() { return glCreateShader(GL_FRAGMENT_SHADER); }
    public static void setShaderSource(int id, String source) { glShaderSource(id, source); }
    public static void compileShader(int id) { glCompileShader(id); }
    public static int getShaderInfo(int id, int info) { return glGetShaderi(id, info); }
    public static String getShaderInfoLog(int id, int chars) { return glGetShaderInfoLog(id, chars); }
    public static void deleteShader(int id) { glDeleteShader(id); }

    // SHADER PROGRAM
    public static int createShaderProgram() { return glCreateProgram(); }
    public static void bindProgram(int id) { glUseProgram(id); }
    public static void unbindProgram() { glUseProgram(0); }
    public static int getUniformLocation(String uniformName, int shaderID) { return glGetUniformLocation(shaderID, uniformName); }
    public static void attachShader(int program, int shader) { glAttachShader(program, shader); }
    public static void linkProgram(int id) { glLinkProgram(id); }
    public static int getProgramInfo(int id, int info) { return glGetProgrami(id, info); }
    public static String getProgramInfoLog(int id, int chars) { return glGetProgramInfoLog(id, chars); }
    public static void deleteProgram(int id) { glDeleteProgram(id); }

    // TEXTURES
    public static int createTexture() { return glGenTextures(); }
    public static void uploadTexture(int mode, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) { glTexImage2D(mode, level, internalformat, width, height, border, format, type, pixels); }
    public static void uploadTexture2D(int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) { glTexImage2D(GL_TEXTURE_2D, level, internalformat, width, height, border, format, type, pixels); }
    public static void bindTexture(int mode, int id) { glBindTexture(mode, id); }
    public static void bindTexture2D(int id) { bindTexture(GL_TEXTURE_2D, id); }
    public static void activateTexture(int slot) { glActiveTexture(slot); }
    public static void unbindTexture(int mode) { glBindTexture(mode, 0); }
    public static void unbindTexture2D() { bindTexture(GL_TEXTURE_2D, 0); }
    public static ByteBuffer loadTexture(String filename, IntBuffer width, IntBuffer height, IntBuffer channels, int desired_channels) { return stbi_load(filename, width, height, channels, desired_channels); }
    public static void flipOnLoad(boolean flip) { stbi_set_flip_vertically_on_load(flip); }
    public static void freeImageAfterLoading(ByteBuffer data) { stbi_image_free(data); }
    public static void setTextureParameter(int mode, int parameter,  int value) { glTexParameteri(mode, parameter, value); }
    public static void setTexture2DParameter(int parameter,  int value) { setTextureParameter(GL_TEXTURE_2D, parameter, value); }
    public static void createMipmap(int mode) { glGenerateMipmap(mode); }
    public static void createMipmap2D() { glGenerateMipmap(GL_TEXTURE_2D); }
    public static void deleteTexture(int id) { glDeleteTextures(id); }

    // BUFFERS
    public static int generateBuffers() { return glGenBuffers(); }
    public static void bindBuffer(int target, int id) { glBindBuffer(target, id); }
    public static void unbindBuffer(int target) { glBindBuffer(target, 0); }
    public static void bufferData(int target, float[] data, int use) { glBufferData(target, data, use); }
    public static void bufferData(int target, FloatBuffer data, int use) { glBufferData(target, data, use); }
    public static void deleteBuffer(int id) { glDeleteBuffers(id); }
}
