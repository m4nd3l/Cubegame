package dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.attributes;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

public class Vector2fAttribute extends Attribute {
    public Vector2fAttribute(int layout) { super(layout, GL_FLOAT, 2); }
    public Vector2fAttribute(int layout, boolean normalized) { super(layout, GL_FLOAT, 2, normalized); }
}
