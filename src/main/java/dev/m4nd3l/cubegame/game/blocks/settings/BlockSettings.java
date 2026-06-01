package dev.m4nd3l.cubegame.game.blocks.settings;

import dev.m4nd3l.cubegame.game.blocks.Block;
import dev.m4nd3l.cubegame.game.blocks.settings.color.Color;
import dev.m4nd3l.cubegame.game.blocks.settings.color.Colors;

public class BlockSettings {
    private Color color;

    public BlockSettings(Color color) { this.color = color; }

    public Color getColor() { return color; }

    public static class Builder {
        public static BlockSettings.Builder get() { return new BlockSettings.Builder(); }

        private Color color = Colors.WHITE.get();

        public BlockSettings.Builder setColor(Color color) { this.color = color; return this; }
        public BlockSettings.Builder setColor(Colors color) { this.color = color.get(); return this; }
        public BlockSettings.Builder setColor(float r, float g, float b, float a) { this.color = new Color(r, g, b, a); return this; }

        public BlockSettings.Builder copy(Block block) { return copy(block.getSettings()); }
        public BlockSettings.Builder copy(BlockSettings settings) { this.color = settings.getColor(); return this; }

        public BlockSettings create() { return new BlockSettings(color); }
    }
}
