package dev.m4nd3l.cubegame.engine.rendering.renderer.world.subchunk;

import dev.m4nd3l.cubegame.engine.coordinates.SubChunkCoordinates;
import dev.m4nd3l.cubegame.engine.providers.WorldProvider;
import dev.m4nd3l.cubegame.engine.rendering.shape.Cuboid;
import dev.m4nd3l.cubegame.engine.rendering.shape.VoxelShape;
import dev.m4nd3l.cubegame.game.blocks.Block;
import dev.m4nd3l.cubegame.game.blocks.BlockTexture;
import dev.m4nd3l.cubegame.game.blocks.settings.color.Color;
import dev.m4nd3l.cubegame.game.registries.BlockRegistry;
import it.unimi.dsi.fastutil.floats.FloatArrayList;

import static dev.m4nd3l.cubegame.game.world.subchunk.SubChunk.SUBCHUNK_DIMENSION_SIZE;

public class SubChunkMesher {
    public static FloatArrayList mesh(SubChunkCoordinates coords, short[] blocks, WorldProvider worldProvider) {
        FloatArrayList vertices = new FloatArrayList(SUBCHUNK_DIMENSION_SIZE * SUBCHUNK_DIMENSION_SIZE * SUBCHUNK_DIMENSION_SIZE * 6);

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

    private static void addVertex(FloatArrayList list,
                                  float x, float y, float z,
                                  Color c,
                                  float u, float v,
                                  float normalX, float normalY, float normalZ,
                                  float textureID) {
        list.add(x); list.add(y); list.add(z);
        list.add(c.getR()); list.add(c.getG()); list.add(c.getB()); list.add(c.getA());
        list.add(u); list.add(v);
        list.add(normalX); list.add(normalY); list.add(normalZ);
        list.add(textureID);
    }

    private static void addFrontFace(FloatArrayList v, float bx, float by, float bz, Cuboid cuboid, Color color, float textureID) {
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

    private static void addBackFace(FloatArrayList v, float bx, float by, float bz, Cuboid cuboid, Color color, float textureID) {
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

    private static void addRightFace(FloatArrayList v, float bx, float by, float bz, Cuboid cuboid, Color color, float textureID) {
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

    private static void addLeftFace(FloatArrayList v, float bx, float by, float bz, Cuboid cuboid, Color color, float textureID) {
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

    private static void addTopFace(FloatArrayList v, float bx, float by, float bz, Cuboid cuboid, Color color, float textureID) {
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

    private static void addBottomFace(FloatArrayList v, float bx, float by, float bz, Cuboid cuboid, Color color, float textureID) {
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