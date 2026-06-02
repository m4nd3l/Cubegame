package dev.m4nd3l.cubegame.game.blocks;

import dev.m4nd3l.cubegame.engine.rendering.shape.VoxelShape;
import dev.m4nd3l.cubegame.game.blocks.settings.BlockSettings;
import dev.m4nd3l.cubegame.game.registries.BlockRegistry;

import java.util.Objects;

public class Block {
    private String name;
    private BlockSettings settings;
    private VoxelShape renderingShape = VoxelShape.FULL_CUBE;
    private VoxelShape hitboxShape = VoxelShape.FULL_CUBE;
    private short ID;

    private BlockTexture texture;

    public Block(String name, BlockSettings settings, short ID) { this(name, settings, ID, null); }
    public Block(String name, BlockSettings settings, short ID, BlockTexture texture) {
        this.name = name;
        this.settings = settings;
        this.ID = ID;
        this.texture = texture == null ? new BlockTexture(-1) : texture;
    }

    public boolean isClassicBlock() { return renderingShape == VoxelShape.FULL_CUBE && hitboxShape == VoxelShape.FULL_CUBE; }
    public boolean isAir() { return this == BlockRegistry.AIR; }

    public short getID() { return ID; }
    public String getName() { return name; }
    public BlockSettings getSettings() { return settings; }
    public BlockTexture getTexture() { return texture; }
    public VoxelShape getHitboxShape() { return hitboxShape; }
    public VoxelShape getRenderingShape() { return renderingShape; }

    public Block setHitboxShape(VoxelShape hitboxShape) { this.hitboxShape = hitboxShape; return this; }
    public Block setRenderingShape(VoxelShape renderingShape) { this.renderingShape = renderingShape; return this; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return getID() == block.getID() && Objects.equals(getName(), block.getName()) && Objects.equals(getSettings(), block.getSettings()) && Objects.equals(getRenderingShape(), block.getRenderingShape()) && Objects.equals(getHitboxShape(), block.getHitboxShape()) && Objects.equals(getTexture(), block.getTexture());
    }

    @Override
    public int hashCode() { return Objects.hash(getName(), getSettings(), getRenderingShape(), getHitboxShape(), getID(), getTexture()); }
}
