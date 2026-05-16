package dev.m4nd3l.cubegame.engine.opengl.wrapper.attributes;

import dev.m4nd3l.loggerutil.LoggerUtils;
import dev.m4nd3l.loggerutil.logger.Logger;

import static org.lwjgl.opengl.GL12.*;
import static dev.m4nd3l.cubegame.engine.opengl.OpenGL.*;

public class Attribute {
    private static Logger LOGGER = LoggerUtils.getLogger();

    private final int layout;
    private final int dataType;
    private final boolean normalized;
    private final int componentCount;
    private final int bytesPerVertex;

    public Attribute(int layout, int dataType, int componentCount) { this(layout, dataType, componentCount, false); }

    public Attribute(int layout, int dataType, int componentCount, boolean normalized) {
        this.layout = layout;
        this.dataType = dataType;
        this.componentCount = componentCount;
        this.normalized = normalized;
        this.bytesPerVertex = calcBytesPerVertex();
    }

    public void enable() { enableAttributePointer(layout); }
    public void disable() { disableAttributePointer(layout); }

    public void link(int offset, int stride) { addAttributePointer(layout, componentCount, dataType, normalized, stride, offset); }

    public int getBytesPerVertex() { return bytesPerVertex; }

    private int calcBytesPerVertex() {
        if (dataType == GL_FLOAT || dataType == GL_UNSIGNED_INT || dataType == GL_INT) return 4 * componentCount;
        else if (dataType == GL_SHORT || dataType == GL_UNSIGNED_SHORT) return 2 * componentCount;
        else if (dataType == GL_BYTE || dataType == GL_UNSIGNED_BYTE) return componentCount;
        else if (dataType == GL_UNSIGNED_INT_2_10_10_10_REV) return 4;
        LOGGER.error("Unsupported data type for VAO attribute: " + dataType);
        return 0;
    }

    public static Attribute[] getMatrix4fAttributes(int startAttribute) {
        Attribute[] attributes = new Attribute[4];
        for(int i = 0; i < attributes.length; i++) attributes[i] = new Attribute(startAttribute + i, GL_FLOAT, 4);
        return attributes;
    }

}
