package dev.m4nd3l.cubegame.engine.rendering.renderer.world.subchunk;

import dev.m4nd3l.cubegame.engine.coordinates.SubChunkCoordinates;
import dev.m4nd3l.cubegame.engine.providers.WorldProvider;
import dev.m4nd3l.cubegame.engine.rendering.shape.Cuboid;
import dev.m4nd3l.cubegame.engine.rendering.shape.VoxelShape;
import dev.m4nd3l.cubegame.game.blocks.Block;
import dev.m4nd3l.cubegame.game.blocks.BlockTexture;
import dev.m4nd3l.cubegame.game.blocks.settings.color.Color;
import dev.m4nd3l.cubegame.game.registries.BlockRegistry;
import dev.m4nd3l.cubegame.toolbox.containers.FloatArray;

import java.nio.FloatBuffer;

import static dev.m4nd3l.cubegame.engine.rendering.renderer.world.subchunk.SubChunkRenderer.FLOATS_PER_VERTEX;
import static dev.m4nd3l.cubegame.game.world.subchunk.SubChunk.SUBCHUNK_DIMENSION_SIZE;

public class SubChunkMesher {
    private static final int MAX_FLOATS = 16 * 16 * 16 * 6 * 4 * FLOATS_PER_VERTEX;

    public static FloatArray mesh(SubChunkCoordinates coords, short[] blocks, WorldProvider worldProvider) {
        FloatArray vertices = new FloatArray(MAX_FLOATS);

        int worldStartX = coords.toBlock().x();
        int worldStartY = coords.toBlock().y();
        int worldStartZ = coords.toBlock().z();

        for (byte x = 0; x < SUBCHUNK_DIMENSION_SIZE; x++) {
            for (byte y = 0; y < SUBCHUNK_DIMENSION_SIZE; y++) {
                for (byte z = 0; z < SUBCHUNK_DIMENSION_SIZE; z++) {

                    Block block = getBlock(x, y, z, blocks);
                    if (isTransparent(block)) continue;

                    int worldX = worldStartX + x;
                    int worldY = worldStartY + y;
                    int worldZ = worldStartZ + z;

                    BlockTexture texture = block.getTexture();
                    Color color = block.getSettings().getColor();
                    VoxelShape shape = block.getRenderingShape();

                    for (Cuboid cuboid : shape.getCuboids()) {
                        // FRONT (+Z)
                        if (shouldRenderFace(worldX, worldY, worldZ + 1, x, y, (byte) (z + 1), blocks, worldProvider))
                            addFrontFace(vertices, worldX, worldY, worldZ, cuboid, color, texture.getFrontTextureID());

                        // BACK (-Z)
                        if (shouldRenderFace(worldX, worldY, worldZ - 1, x, y, (byte) (z - 1), blocks, worldProvider))
                            addBackFace(vertices, worldX, worldY, worldZ, cuboid, color, texture.getSideTextureID());

                        // RIGHT (+X)
                        if (shouldRenderFace(worldX + 1, worldY, worldZ, (byte) (x + 1), y, z, blocks, worldProvider))
                            addRightFace(vertices, worldX, worldY, worldZ, cuboid, color, texture.getSideTextureID());

                        // LEFT (-X)
                        if (shouldRenderFace(worldX - 1, worldY, worldZ, (byte) (x - 1), y, z, blocks, worldProvider))
                            addLeftFace(vertices, worldX, worldY, worldZ, cuboid, color, texture.getSideTextureID());

                        // TOP (+Y)
                        if (shouldRenderFace(worldX, worldY + 1, worldZ, x, (byte) (y + 1), z, blocks, worldProvider))
                            addTopFace(vertices, worldX, worldY, worldZ, cuboid, color, texture.getTopTextureID());

                        // BOTTOM (-Y)
                        if (shouldRenderFace(worldX, worldY - 1, worldZ, x, (byte) (y - 1), z, blocks, worldProvider))
                            addBottomFace(vertices, worldX, worldY, worldZ, cuboid, color, texture.getBottomTextureID());
                    }
                }
            }
        }

        return vertices;
    }

    private static boolean shouldRenderFace(int worldX, int worldY, int worldZ,
                                            byte localX, byte localY, byte localZ,
                                            short[] blocks, WorldProvider worldProvider) {
        if (localX < 0 || localX >= SUBCHUNK_DIMENSION_SIZE ||
                localY < 0 || localY >= SUBCHUNK_DIMENSION_SIZE ||
                localZ < 0 || localZ >= SUBCHUNK_DIMENSION_SIZE) {

            if (worldProvider == null) return true;
            return isTransparent(worldProvider.getBlockAt(worldX, worldY, worldZ));
        }

        return isTransparent(getBlock(localX, localY, localZ, blocks));
    }

    public static boolean isTransparent(Block block) { return block.equals(BlockRegistry.AIR); }

    private static void addVertex(FloatArray list,
                                  float x, float y, float z,
                                  Color c,
                                  float u, float v,
                                  float normalX, float normalY, float normalZ,
                                  float textureID) {
        list.put(x); list.put(y); list.put(z);                                           // vec3  POS
        list.put(c.getR()); list.put(c.getG()); list.put(c.getB()); list.put(c.getA());  // vec4  COLOR
        list.put(u); list.put(v);                                                        // vec2  UVs
        list.put(normalX); list.put(normalY); list.put(normalZ);                         // vec3  NORMALS
        list.put(textureID);                                                             // float TEXTURE ID
    }

    private static void addFrontFace(FloatArray v, float bx, float by, float bz, Cuboid cuboid, Color color, float textureID) {
        VoxelShape.UVBounds uv = cuboid.getSouthUV();
        float minX = bx + cuboid.minX(), maxX = bx + cuboid.maxX();
        float minY = by + cuboid.minY(), maxY = by + cuboid.maxY();
        float maxZ = bz + cuboid.maxZ();

        addVertex(v, minX, minY, maxZ, color, uv.uMin(), uv.vMin(), 0, 0, 1, textureID);
        addVertex(v, maxX, minY, maxZ, color, uv.uMax(), uv.vMin(), 0, 0, 1, textureID);
        addVertex(v, maxX, maxY, maxZ, color, uv.uMax(), uv.vMax(), 0, 0, 1, textureID);

        addVertex(v, minX, minY, maxZ, color, uv.uMin(), uv.vMin(), 0, 0, 1, textureID);
        addVertex(v, maxX, maxY, maxZ, color, uv.uMax(), uv.vMax(), 0, 0, 1, textureID);
        addVertex(v, minX, maxY, maxZ, color, uv.uMin(), uv.vMax(), 0, 0, 1, textureID);
    }

    private static void addBackFace(FloatArray v, float bx, float by, float bz, Cuboid cuboid, Color color, float textureID) {
        VoxelShape.UVBounds uv = cuboid.getNorthUV();
        float minX = bx + cuboid.minX(), maxX = bx + cuboid.maxX();
        float minY = by + cuboid.minY(), maxY = by + cuboid.maxY();
        float minZ = bz + cuboid.minZ();

        addVertex(v, maxX, minY, minZ, color, uv.uMin(), uv.vMin(), 0, 0, -1, textureID);
        addVertex(v, minX, minY, minZ, color, uv.uMax(), uv.vMin(), 0, 0, -1, textureID);
        addVertex(v, minX, maxY, minZ, color, uv.uMax(), uv.vMax(), 0, 0, -1, textureID);

        addVertex(v, maxX, minY, minZ, color, uv.uMin(), uv.vMin(), 0, 0, -1, textureID);
        addVertex(v, minX, maxY, minZ, color, uv.uMax(), uv.vMax(), 0, 0, -1, textureID);
        addVertex(v, maxX, maxY, minZ, color, uv.uMin(), uv.vMax(), 0, 0, -1, textureID);
    }

    private static void addRightFace(FloatArray v, float bx, float by, float bz, Cuboid cuboid, Color color, float textureID) {
        VoxelShape.UVBounds uv = cuboid.getEastUV();
        float maxX = bx + cuboid.maxX();
        float minY = by + cuboid.minY(), maxY = by + cuboid.maxY();
        float minZ = bz + cuboid.minZ(), maxZ = bz + cuboid.maxZ();

        addVertex(v, maxX, minY, maxZ, color, uv.uMin(), uv.vMin(), 1, 0, 0, textureID);
        addVertex(v, maxX, minY, minZ, color, uv.uMax(), uv.vMin(), 1, 0, 0, textureID);
        addVertex(v, maxX, maxY, minZ, color, uv.uMax(), uv.vMax(), 1, 0, 0, textureID);

        addVertex(v, maxX, minY, maxZ, color, uv.uMin(), uv.vMin(), 1, 0, 0, textureID);
        addVertex(v, maxX, maxY, minZ, color, uv.uMax(), uv.vMax(), 1, 0, 0, textureID);
        addVertex(v, maxX, maxY, maxZ, color, uv.uMin(), uv.vMax(), 1, 0, 0, textureID);
    }

    private static void addLeftFace(FloatArray v, float bx, float by, float bz, Cuboid cuboid, Color color, float textureID) {
        VoxelShape.UVBounds uv = cuboid.getWestUV();
        float minX = bx + cuboid.minX();
        float minY = by + cuboid.minY(), maxY = by + cuboid.maxY();
        float minZ = bz + cuboid.minZ(), maxZ = bz + cuboid.maxZ();

        addVertex(v, minX, minY, minZ, color, uv.uMin(), uv.vMin(), -1, 0, 0, textureID);
        addVertex(v, minX, minY, maxZ, color, uv.uMax(), uv.vMin(), -1, 0, 0, textureID);
        addVertex(v, minX, maxY, maxZ, color, uv.uMax(), uv.vMax(), -1, 0, 0, textureID);

        addVertex(v, minX, minY, minZ, color, uv.uMin(), uv.vMin(), -1, 0, 0, textureID);
        addVertex(v, minX, maxY, maxZ, color, uv.uMax(), uv.vMax(), -1, 0, 0, textureID);
        addVertex(v, minX, maxY, minZ, color, uv.uMin(), uv.vMax(), -1, 0, 0, textureID);
    }

    private static void addTopFace(FloatArray v, float bx, float by, float bz, Cuboid cuboid, Color color, float textureID) {
        VoxelShape.UVBounds uv = cuboid.getTopUV();
        float minX = bx + cuboid.minX(), maxX = bx + cuboid.maxX();
        float maxY = by + cuboid.maxY();
        float minZ = bz + cuboid.minZ(), maxZ = bz + cuboid.maxZ();

        addVertex(v, minX, maxY, maxZ, color, uv.uMin(), uv.vMin(), 0, 1, 0, textureID);
        addVertex(v, maxX, maxY, maxZ, color, uv.uMax(), uv.vMin(), 0, 1, 0, textureID);
        addVertex(v, maxX, maxY, minZ, color, uv.uMax(), uv.vMax(), 0, 1, 0, textureID);

        addVertex(v, minX, maxY, maxZ, color, uv.uMin(), uv.vMin(), 0, 1, 0, textureID);
        addVertex(v, maxX, maxY, minZ, color, uv.uMax(), uv.vMax(), 0, 1, 0, textureID);
        addVertex(v, minX, maxY, minZ, color, uv.uMin(), uv.vMax(), 0, 1, 0, textureID);
    }

    private static void addBottomFace(FloatArray v, float bx, float by, float bz, Cuboid cuboid, Color color, float textureID) {
        VoxelShape.UVBounds uv = cuboid.getBottomUV();
        float minX = bx + cuboid.minX(), maxX = bx + cuboid.maxX();
        float minY = by + cuboid.minY();
        float minZ = bz + cuboid.minZ(), maxZ = bz + cuboid.maxZ();

        addVertex(v, minX, minY, minZ, color, uv.uMin(), uv.vMin(), 0, -1, 0, textureID);
        addVertex(v, maxX, minY, minZ, color, uv.uMax(), uv.vMin(), 0, -1, 0, textureID);
        addVertex(v, maxX, minY, maxZ, color, uv.uMax(), uv.vMax(), 0, -1, 0, textureID);

        addVertex(v, minX, minY, minZ, color, uv.uMin(), uv.vMin(), 0, -1, 0, textureID);
        addVertex(v, maxX, minY, maxZ, color, uv.uMax(), uv.vMax(), 0, -1, 0, textureID);
        addVertex(v, minX, minY, maxZ, color, uv.uMin(), uv.vMax(), 0, -1, 0, textureID);
    }

    private static Block getBlock(byte x, byte y, byte z, short[] blocks) { return BlockRegistry.getBlock(blocks[getIndex(x, y, z)]); }
    private static int getIndex(byte x, byte y, byte z) { return x + (y * SUBCHUNK_DIMENSION_SIZE) + (z * SUBCHUNK_DIMENSION_SIZE * SUBCHUNK_DIMENSION_SIZE); }
}