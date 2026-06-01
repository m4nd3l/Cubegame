package dev.m4nd3l.cubegame.engine.rendering.shape;

import java.util.List;

public class VoxelShape {
    public static final VoxelShape FULL_CUBE = new VoxelShape(List.of(
            new Cuboid(0.0f, 0.0f, 0.0f,
                    1.0f, 1.0f, 1.0f,
                    0, 0, 16.0f, 16.0f)
    ));

    private final List<Cuboid> cuboids;

    public VoxelShape(List<Cuboid> cuboids) { this.cuboids = cuboids; }
    public List<Cuboid> getCuboids() { return cuboids; }

    public record UVBounds(float uMin, float vMin, float uMax, float vMax) {}
}