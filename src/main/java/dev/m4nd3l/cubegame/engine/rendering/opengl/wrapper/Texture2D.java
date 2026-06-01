package dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper;

import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.mother.OpenGLObject;
import dev.m4nd3l.loggerutil.LoggerUtils;
import dev.m4nd3l.loggerutil.logger.Logger;
import org.lwjgl.BufferUtils;

import java.nio.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static dev.m4nd3l.cubegame.engine.rendering.opengl.OpenGL.*;

public class Texture2D implements OpenGLObject {
    private static Logger LOGGER = LoggerUtils.getLogger();

    private int textureID;
    private ByteBuffer image;
    private int width;
    private int height;

    public Texture2D(String filename) { this(filename, true); }

    public Texture2D(String filename, boolean flipOnLoad) {
        textureID = createTexture();
        bind();
        setTexture2DParameter(GL_TEXTURE_WRAP_S, GL_REPEAT);
        setTexture2DParameter(GL_TEXTURE_WRAP_T, GL_REPEAT);
        setTexture2DParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        setTexture2DParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        IntBuffer width    = BufferUtils.createIntBuffer(1),
                  height   = BufferUtils.createIntBuffer(1),
                  channels = BufferUtils.createIntBuffer(1);

        flipOnLoad(flipOnLoad);

        image = loadTexture(filename, width, height, channels, 4);

        if (image != null) {
            this.width = width.get(0);
            this.height = height.get(0);

            if (channels.get(0) == 3)
                uploadTexture2D( 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            else if (channels.get(0) == 4)
                uploadTexture2D( 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            else {
                LOGGER.error("Error: could not load the image: '" + filename +
                        "', it need to be made of 4 channels (.png file format), it has " + channels.get(0) + "channels!");
                System.exit(1);
            }
            createMipmap2D();
            freeImageAfterLoading(image);
        } else LOGGER.error("Error: could not load the image: '" + filename + "'.");
    }
    @Override
    public void bind() { activateTexture(GL_TEXTURE0); bindTexture2D(textureID); }
    public void bind(int slot) { activateTexture(GL_TEXTURE0 + slot); bindTexture2D(textureID); }
    @Override
    public void unbind() { unbindTexture2D(); }

    @Override
    public void delete() { deleteTexture(textureID); }
}
