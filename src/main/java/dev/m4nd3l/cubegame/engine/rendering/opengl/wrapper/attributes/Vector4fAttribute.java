package dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.attributes;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

public class Vector4fAttribute extends Attribute {
    public Vector4fAttribute(int layout) { super(layout, GL_FLOAT, 4); }
    public Vector4fAttribute(int layout, boolean normalized) { super(layout, GL_FLOAT, 4, normalized); }
}
