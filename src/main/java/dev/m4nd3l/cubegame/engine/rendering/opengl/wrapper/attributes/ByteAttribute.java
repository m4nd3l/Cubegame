package dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.attributes;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;

public class ByteAttribute extends Attribute {
    public ByteAttribute(int layout) { super(layout, GL_UNSIGNED_BYTE, 1); }
    public ByteAttribute(int layout, boolean normalized) { super(layout, GL_UNSIGNED_BYTE, 1, normalized); }
}
