package dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper;

import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.mother.OpenGLObject;
import dev.m4nd3l.loggerutil.LoggerUtils;
import dev.m4nd3l.loggerutil.logger.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import static dev.m4nd3l.cubegame.engine.rendering.opengl.OpenGL.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

public class TextureArray implements OpenGLObject {
    private static final Logger LOGGER = LoggerUtils.getLogger();

    private int textureID;
    private int width;
    private int height;
    private int layerCount;

    public TextureArray(List<String> textureNames) { this(textureNames, true); }
    public TextureArray(List<String> textureNames, boolean flipOnLoad) {
        if (textureNames == null || textureNames.isEmpty()) quitWithError("Error: Cannot create a TextureArray with no files.");
        this.layerCount = textureNames.size();
        this.textureID = createTexture();

        bind();

        setTexture2DArrayParameter(GL_TEXTURE_WRAP_S, GL_REPEAT);
        setTexture2DArrayParameter(GL_TEXTURE_WRAP_T, GL_REPEAT);
        setTexture2DArrayParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        setTexture2DArrayParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);

        flipOnLoad(flipOnLoad);

        ByteBuffer firstImage = loadTextureFromJar(textureNames.getFirst(), widthBuffer, heightBuffer, channelsBuffer, 4);
        if (firstImage == null) quitWithError("Error: Could not load base image for TextureArray: '" + textureNames.getFirst() + "'");

        this.width = widthBuffer.get(0);
        this.height = heightBuffer.get(0);

        allocateTextureStorage3D(4, GL_RGBA8, this.width, this.height, this.layerCount);

        uploadTextureSubImage3D(0, 0, 0, 0, this.width, this.height, 1, GL_RGBA, GL_UNSIGNED_BYTE, firstImage);
        freeImageAfterLoading(firstImage);

        for (int i = 1; i < layerCount; i++) addTextureInternal(textureNames.get(i), widthBuffer, heightBuffer, channelsBuffer, i);

        createMipmap2DArray();
        unbind();
    }

    private ByteBuffer loadTextureFromJar(String resourcePath, IntBuffer widthBuffer, IntBuffer heightBuffer, IntBuffer channelsBuffer, int desiredChannels) {
        try (InputStream is = TextureArray.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                LOGGER.error("Resource not found in JAR: " + resourcePath);
                return null;
            }

            ByteBuffer buffer;
            try (ReadableByteChannel rbc = Channels.newChannel(is)) {
                buffer = BufferUtils.createByteBuffer(16 * 1024);
                while (rbc.read(buffer) != -1) {
                    if (buffer.remaining() == 0) {
                        ByteBuffer newBuffer = BufferUtils.createByteBuffer(buffer.capacity() * 2);
                        buffer.flip();
                        newBuffer.put(buffer);
                        buffer = newBuffer;
                    }
                }
                buffer.flip();
            }

            return loadTextureFromMemory(buffer, widthBuffer, heightBuffer, channelsBuffer, desiredChannels);

        } catch (IOException e) {
            LOGGER.error("Failed to read JAR resource '" + resourcePath + "'.\nMore info:\n" + e);
            return null;
        }
    }

    @Override
    public void bind() { activateTexture(GL_TEXTURE0); bindTexture2DArray(textureID); }
    public void bind(int slot) { activateTexture(GL_TEXTURE0 + slot); bindTexture2DArray(textureID); }

    @Override
    public void unbind() { unbindTexture2DArray(); }
    @Override
    public void delete() { deleteTexture(textureID); }

    public int getLayerCount() { return layerCount; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void resizeAndAddTexture(String filename) {
        org.lwjgl.system.MemoryStack stack = org.lwjgl.system.MemoryStack.stackPush();
        IntBuffer widthBuffer = stack.mallocInt(1);
        IntBuffer heightBuffer = stack.mallocInt(1);
        IntBuffer channelsBuffer = stack.mallocInt(1);

        ByteBuffer newImage = loadTextureFromJar(filename, widthBuffer, heightBuffer, channelsBuffer, 4);
        if (newImage == null) {
            LOGGER.error("Error: Could not load image '" + filename + "' for resizing TextureArray.");
            stack.close();
            return;
        }

        if (widthBuffer.get(0) != this.width || heightBuffer.get(0) != this.height) {
            LOGGER.error("Error: New texture size does not match TextureArray size.");
            freeImageAfterLoading(newImage);
            stack.close();
            return;
        }

        int newLayerCount = this.layerCount + 1;
        int newTextureID = createTexture();

        activateTexture(GL_TEXTURE0);
        bindTexture(org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY, newTextureID);

        setTexture2DArrayParameter(GL_TEXTURE_WRAP_S, GL_REPEAT);
        setTexture2DArrayParameter(GL_TEXTURE_WRAP_T, GL_REPEAT);
        setTexture2DArrayParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        setTexture2DArrayParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        allocateTextureStorage3D(4, GL_RGBA8, this.width, this.height, newLayerCount);

        copyImageSubData(
                this.textureID, org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY, 0, 0, 0, 0,
                newTextureID, org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY, 0, 0, 0, 0,
                this.width, this.height, this.layerCount
        );

        uploadTextureSubImage3D(0, 0, 0, this.layerCount, this.width, this.height, 1, GL_RGBA, GL_UNSIGNED_BYTE, newImage);
        createMipmap2DArray();
        unbindTexture2DArray();

        delete();

        this.textureID = newTextureID;
        this.layerCount = newLayerCount;

        freeImageAfterLoading(newImage);
        stack.close();

        LOGGER.info("TextureArray resized successfully. New layer count: " + this.layerCount);
    }

    public void updateTextureLayer(String filename, int index) {
        if (index < 0 || index >= this.layerCount) {
            LOGGER.error("Error: Index " + index + " out of bounds for TextureArray of size " + this.layerCount);
            return;
        }

        MemoryStack stack = MemoryStack.stackPush();
        IntBuffer widthBuffer    = stack.mallocInt(1);
        IntBuffer heightBuffer   = stack.mallocInt(1);
        IntBuffer channelsBuffer = stack.mallocInt(1);

        ByteBuffer image = loadTextureFromJar(filename, widthBuffer, heightBuffer, channelsBuffer, 4);

        if (image == null) {
            LOGGER.error("Error: Could not load the image: '" + filename + "' to update layer " + index);
            stack.close();
            return;
        }

        int currentWidth = widthBuffer.get(0);
        int currentHeight = heightBuffer.get(0);

        if (currentWidth != this.width || currentHeight != this.height) {
            LOGGER.error("Error: Texture '" + filename + "' size (" + currentWidth + "x" + currentHeight +
                    ") does not match base TextureArray size (" + this.width + "x" + this.height + ")!");
            freeImageAfterLoading(image);
            stack.close();
            return;
        }

        bind();
        uploadTextureSubImage3D(0, 0, 0, index, this.width, this.height, 1, GL_RGBA, GL_UNSIGNED_BYTE, image);
        createMipmap2DArray();
        unbind();

        freeImageAfterLoading(image);
        stack.close();
    }

    private void addTextureInternal(String filename, IntBuffer widthBuffer, IntBuffer heightBuffer, IntBuffer channelsBuffer, int index) {
        widthBuffer.clear();
        heightBuffer.clear();
        channelsBuffer.clear();

        ByteBuffer image = loadTextureFromJar(filename, widthBuffer, heightBuffer, channelsBuffer, 4);
        if (image != null) {
            int currentWidth = widthBuffer.get(0);
            int currentHeight = heightBuffer.get(0);

            if (currentWidth != this.width || currentHeight != this.height) quitWithError(
                    "Error: Texture '" + filename + "' size (" + currentWidth + "x" + currentHeight +
                            ") does not match base TextureArray size (" + this.width + "x" + this.height + ")!");

            uploadTextureSubImage3D(0, 0, 0, index, this.width, this.height, 1, GL_RGBA, GL_UNSIGNED_BYTE, image);
            freeImageAfterLoading(image);
        } else { LOGGER.error("Error: Could not load the image: '" + filename + "' while adding a new texture to the TextureArray."); }
    }

    private void quitWithError(String error) { LOGGER.error(error); System.exit(1); }
}