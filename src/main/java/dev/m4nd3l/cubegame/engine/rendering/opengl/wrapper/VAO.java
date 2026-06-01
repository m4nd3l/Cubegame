package dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper;

import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.attributes.Attribute;
import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.mother.OpenGLObject;

import java.util.ArrayList;
import java.util.List;

import static dev.m4nd3l.cubegame.engine.rendering.opengl.OpenGL.*;

public class VAO implements OpenGLObject {
    private int vaoID;
    private List<Attribute> attributes;

    public VAO() { vaoID = generateVAO(); attributes = new ArrayList<>(5); }

    public void addAttributes(int bytesPerVertex, Attribute... newAttributes) {
        int offset = 0;
        for (Attribute attribute : newAttributes) {
            attribute.link(offset, bytesPerVertex);
            offset += attribute.getBytesPerVertex();
            attribute.enable();
            attributes.add(attribute);
        }
    }

    @Override
    public void bind() { bindVAO(vaoID); }
    @Override
    public void unbind() { unbindVAO(); }

    @Override
    public void delete() { deleteVAO(vaoID); }
}
