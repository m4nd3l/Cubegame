package dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.attributes;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;

public class IntAttribute extends Attribute {
    public IntAttribute(int layout) { super(layout, GL_UNSIGNED_INT, 1); }
    public IntAttribute(int layout, boolean normalized) { super(layout, GL_UNSIGNED_INT, 1, normalized); }
}
