package dev.m4nd3l.cubegame.engine.opengl.wrapper;

import dev.m4nd3l.cubegame.engine.opengl.wrapper.attributes.Attribute;

import java.util.ArrayList;
import java.util.List;

import static dev.m4nd3l.cubegame.engine.opengl.OpenGL.*;

public class VAO {
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

    public void bind() { bindVAO(vaoID); }
    public void unbind() { unbindVAO(); }

    public void delete() { deleteVAO(vaoID); }
}
