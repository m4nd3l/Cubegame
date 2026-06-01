package dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.attributes;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

public class FloatAttribute extends Attribute {
    public FloatAttribute(int layout) { super(layout, GL_FLOAT, 1); }
    public FloatAttribute(int layout, boolean normalized) { super(layout, GL_FLOAT, 1, normalized); }
}
