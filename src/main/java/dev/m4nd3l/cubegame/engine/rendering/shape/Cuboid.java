package dev.m4nd3l.cubegame.engine.rendering.shape;

public record Cuboid(
        float minX, float minY, float minZ,
        float maxX, float maxY, float maxZ,
        int textureU, int textureV,
        float textureWidth,
        float textureHeight
) {
    private float w() { return (maxX - minX) * 16f; }
    private float h() { return (maxY - minY) * 16f; }
    private float d() { return (maxZ - minZ) * 16f; }

    public VoxelShape.UVBounds getTopUV() { return toNormalized(textureU + d(), textureV, w(), d()); }
    public VoxelShape.UVBounds getBottomUV() { return toNormalized(textureU + d() + w(), textureV, w(), d()); }
    public VoxelShape.UVBounds getNorthUV() { return toNormalized(textureU + d(), textureV + d(), w(), h()); }
    public VoxelShape.UVBounds getSouthUV() { return toNormalized(textureU + d() + w() + d(), textureV + d(), w(), h()); }
    public VoxelShape.UVBounds getEastUV() { return toNormalized(textureU, textureV + d(), d(), h()); }
    public VoxelShape.UVBounds getWestUV() { return toNormalized(textureU + d() + w(), textureV + d(), d(), h()); }

    private VoxelShape.UVBounds toNormalized(float x, float y, float width, float height) {
        return new VoxelShape.UVBounds(
                x / textureWidth,
                y / textureHeight,
                (x + width) / textureWidth,
                (y + height) / textureHeight
        );
    }
}