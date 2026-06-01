package dev.m4nd3l.cubegame.engine.rendering.opengl;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL43.glCopyImageSubData;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL42.*;

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
    public static void bindVAO(int ID) { glBindVertexArray(ID); }
    public static void unbindVAO() { glBindVertexArray(0); }
    public static void deleteVAO(int ID) { glDeleteVertexArrays(ID); }

    // VBO
    public static int generateVBO() { return generateBuffers(); }
    public static void addDataToVBO(float[] data, int use) { bufferData(GL_ARRAY_BUFFER, data, use); }
    public static void addDataToVBO(FloatBuffer data, int use) { bufferData(GL_ARRAY_BUFFER, data, use); }
    public static void bindVBO(int ID) { bindBuffer(GL_ARRAY_BUFFER, ID); }
    public static void unbindVBO() { unbindBuffer(GL_ARRAY_BUFFER); }
    public static void deleteVBO(int ID) { deleteBuffer(ID); }

    // SHADER
    public static int createShader(int type) { return glCreateShader(type); }
    public static int createVertexShader() { return glCreateShader(GL_VERTEX_SHADER); }
    public static int createFragmentShader() { return glCreateShader(GL_FRAGMENT_SHADER); }
    public static void setShaderSource(int ID, String source) { glShaderSource(ID, source); }
    public static void compileShader(int ID) { glCompileShader(ID); }
    public static int getShaderInfo(int ID, int info) { return glGetShaderi(ID, info); }
    public static String getShaderInfoLog(int ID, int chars) { return glGetShaderInfoLog(ID, chars); }
    public static void deleteShader(int ID) { glDeleteShader(ID); }

    // SHADER PROGRAM
    public static int createShaderProgram() { return glCreateProgram(); }
    public static void bindProgram(int ID) { glUseProgram(ID); }
    public static void unbindProgram() { glUseProgram(0); }
    public static int getUniformLocation(String uniformName, int shaderID) { return glGetUniformLocation(shaderID, uniformName); }
    public static void attachShader(int program, int shader) { glAttachShader(program, shader); }
    public static void linkProgram(int ID) { glLinkProgram(ID); }
    public static int getProgramInfo(int ID, int info) { return glGetProgrami(ID, info); }
    public static String getProgramInfoLog(int ID, int chars) { return glGetProgramInfoLog(ID, chars); }
    public static void deleteProgram(int ID) { glDeleteProgram(ID); }

    // TEXTURES
    public static int createTexture() { return glGenTextures(); }
    public static void uploadTexture(int mode, int level, int internalFormat, int width, int height, int border, int format, int type, ByteBuffer pixels) { glTexImage2D(mode, level, internalFormat, width, height, border, format, type, pixels); }
    public static void uploadTexture2D(int level, int internalFormat, int width, int height, int border, int format, int type, ByteBuffer pixels) { glTexImage2D(GL_TEXTURE_2D, level, internalFormat, width, height, border, format, type, pixels); }
    public static void bindTexture(int mode, int ID) { glBindTexture(mode, ID); }
    public static void bindTexture2D(int ID) { bindTexture(GL_TEXTURE_2D, ID); }
    public static void activateTexture(int slot) { glActiveTexture(slot); }
    public static void unbindTexture(int mode) { glBindTexture(mode, 0); }
    public static void unbindTexture2D() { bindTexture(GL_TEXTURE_2D, 0); }
    public static ByteBuffer loadTextureFromMemory(ByteBuffer buffer, IntBuffer width, IntBuffer height, IntBuffer channels, int desiredChannels) { return stbi_load_from_memory(buffer, width, height, channels, desiredChannels); }
    public static ByteBuffer loadTexture(String filename, IntBuffer width, IntBuffer height, IntBuffer channels, int desiredChannels) { return stbi_load(filename, width, height, channels, desiredChannels); }
    public static void flipOnLoad(boolean flip) { stbi_set_flip_vertically_on_load(flip); }
    public static void freeImageAfterLoading(ByteBuffer data) { stbi_image_free(data); }
    public static void setTextureParameter(int mode, int parameter,  int value) { glTexParameteri(mode, parameter, value); }
    public static void setTexture2DParameter(int parameter,  int value) { setTextureParameter(GL_TEXTURE_2D, parameter, value); }
    public static void createMipmap(int mode) { glGenerateMipmap(mode); }
    public static void createMipmap2D() { glGenerateMipmap(GL_TEXTURE_2D); }
    public static void deleteTexture(int ID) { glDeleteTextures(ID); }

    // TEXTURE ARRAYS
    public static void bindTexture2DArray(int ID) { bindTexture(GL_TEXTURE_2D_ARRAY, ID); }
    public static void unbindTexture2DArray() { glBindTexture(GL_TEXTURE_2D_ARRAY, 0); }
    public static void allocateTextureStorage3D(int levels, int internalFormat, int width, int height, int depth) { glTexStorage3D(GL_TEXTURE_2D_ARRAY, levels, internalFormat, width, height, depth); }
    public static void uploadTextureSubImage3D(int level, int xOffset, int yOffset, int zOffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) { glTexSubImage3D(GL_TEXTURE_2D_ARRAY, level, xOffset, yOffset, zOffset, width, height, depth, format, type, pixels); }
    public static void setTexture2DArrayParameter(int parameter, int value) { setTextureParameter(GL_TEXTURE_2D_ARRAY, parameter, value); }
    public static void createMipmap2DArray() { glGenerateMipmap(GL_TEXTURE_2D_ARRAY); }
    public static void copyImageSubData(int source, int sourceTarget, int sourceLevel, int sourceX, int sourceY, int sourceZ, int destination,int destinationTarget, int destinationLevel, int destinationX, int destinationY, int destinationZ, int sourceWidth, int sourceHeight, int sourceDepth) { glCopyImageSubData(source, sourceTarget, sourceLevel, sourceX, sourceY, sourceZ, destination, destinationTarget, destinationLevel, destinationX, destinationY, destinationZ, sourceWidth, sourceHeight, sourceDepth); }

    // BUFFERS
    public static int generateBuffers() { return glGenBuffers(); }
    public static void bindBuffer(int target, int ID) { glBindBuffer(target, ID); }
    public static void unbindBuffer(int target) { glBindBuffer(target, 0); }
    public static void bufferData(int target, float[] data, int use) { glBufferData(target, data, use); }
    public static void bufferData(int target, FloatBuffer data, int use) { glBufferData(target, data, use); }
    public static void deleteBuffer(int ID) { glDeleteBuffers(ID); }

    // UNIFORMS
    public static void uploadUniformMatrix4f(int location, FloatBuffer buffer) { glUniformMatrix4fv(location, false, buffer); }
}
