package dev.m4nd3l.cubegame.engine.rendering.renderer.world.subchunk;

import dev.m4nd3l.cubegame.engine.coordinates.SubChunkCoordinates;
import dev.m4nd3l.cubegame.engine.rendering.input.Camera;
import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.VAO;
import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.VBO;
import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.attributes.FloatAttribute;
import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.attributes.Vector2fAttribute;
import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.attributes.Vector3fAttribute;
import dev.m4nd3l.cubegame.engine.rendering.opengl.wrapper.attributes.Vector4fAttribute;
import dev.m4nd3l.cubegame.engine.rendering.renderer.ShaderlessRenderer;
import dev.m4nd3l.cubegame.game.world.subchunk.SubChunk;
import it.unimi.dsi.fastutil.floats.FloatArrayList;

import static dev.m4nd3l.cubegame.engine.rendering.opengl.OpenGL.drawArrays;
import static dev.m4nd3l.cubegame.game.world.subchunk.SubChunk.SUBCHUNK_DIMENSION_SIZE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class SubChunkRenderer extends ShaderlessRenderer {
    private transient volatile boolean dirty;
    private final int FLOATS_PER_VERTEX = 13;
    private transient boolean attributesSet, isSeen;
    private transient VAO vao;
    private transient VBO vbo;
    private transient int verticesCount;
    private transient FloatArrayList vertices;

    public SubChunkRenderer() {
        this.dirty = true;
        this.attributesSet = false;
        this.verticesCount = 0;
        this.vertices = new FloatArrayList();
    }

    public void uploadToGPU() {
        if (vertices == null) return;

        verticesCount = vertices.size() / FLOATS_PER_VERTEX;
        if (vertices.isEmpty()) {
            vertices = null;
            return;
        }

        if (vao == null) vao = new VAO();
        if (vbo == null) vbo = new VBO();

        actionBetweenBinding(() -> vbo.uploadDataStaticDraw(vertices.elements()), vbo);

        if (!attributesSet)
            actionBetweenBinding(() ->
                    vao.addAttributes(Float.BYTES * FLOATS_PER_VERTEX,
                            new Vector3fAttribute(0, false),          // vec3 POS
                            new Vector4fAttribute(1, false),          // vec4 COLOR
                            new Vector2fAttribute(2, false),          // vec2 UVs
                            new Vector3fAttribute(3, false),          // vec3 NORMALS
                            new FloatAttribute(4, false)), vao, vbo); // float TEXTURE ID
        attributesSet = true;
        vertices = null;
    }

    public void remesh(FloatArrayList vertices) { this.vertices = vertices; }

    public void update(Camera camera, SubChunkCoordinates coordinates) { if (!camera.frustumFreeze) isSeen(camera, coordinates); }
    public void render() { if (verticesCount == 0 || !isSeen) return; actionBetweenBinding(() -> drawArrays(GL_TRIANGLES, 0, verticesCount), vao); }
    public void delete() { unbindAndDelete(vao, vbo); }

    private boolean isSeen(Camera camera, SubChunkCoordinates coordinates) {
        float margin = 0.5f;
        float minX = (coordinates.x() * SUBCHUNK_DIMENSION_SIZE) - margin;
        float minY = (coordinates.y() * SUBCHUNK_DIMENSION_SIZE) - margin;
        float minZ = (coordinates.z() * SUBCHUNK_DIMENSION_SIZE) - margin;
        float maxX = minX + SUBCHUNK_DIMENSION_SIZE + (margin * 2);
        float maxY = minY + SUBCHUNK_DIMENSION_SIZE + (margin * 2);
        float maxZ = minZ + SUBCHUNK_DIMENSION_SIZE + (margin * 2);

        isSeen = camera.isInsideFrustum(minX, minY, minZ, maxX, maxY, maxZ);
        return isSeen;
    }

    public void dirty(boolean dirty) { this.dirty = dirty; }
    public void turnTrueIf(boolean condition) { this.dirty = condition || dirty; }
    public void dirty(Runnable action, boolean dirty) { action.run(); this.dirty = dirty; }
    public boolean isDirty() { return dirty; }
}
