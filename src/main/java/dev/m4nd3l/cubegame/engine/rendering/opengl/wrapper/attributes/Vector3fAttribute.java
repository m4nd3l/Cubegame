package dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.attributes;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

public class Vector3fAttribute extends Attribute {
    public Vector3fAttribute(int layout) { super(layout, GL_FLOAT, 3); }
    public Vector3fAttribute(int layout, boolean normalized) { super(layout, GL_FLOAT, 3, normalized); }
}
