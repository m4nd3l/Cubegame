package dev.m4nd3l.cubegame.game.registries;

import dev.m4nd3l.cubegame.game.blocks.Block;
import dev.m4nd3l.cubegame.game.blocks.settings.BlockSettings;
import dev.m4nd3l.cubegame.toolbox.containers.DoubleWayMap;
import dev.m4nd3l.loggerutil.LoggerUtils;
import dev.m4nd3l.loggerutil.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class BlockRegistry {
    private static Logger LOGGER = LoggerUtils.getLogger();
    private static DoubleWayMap<Short, Block> BLOCKS = new DoubleWayMap<>();
    private static DoubleWayMap<String, Block> BLOCK_NAMES = new DoubleWayMap<>();
    private static short lastID = 0, lastTextureID = 0;

    public static final List<String> blockTextures = new ArrayList<>(List.of("/assets/textures/blocks/unknown.png"));

    public static final Block AIR
            = register("air", BlockSettings.Builder.get().create());

    public static final Block DIRT
            = register("dirt", BlockSettings.Builder.get().create());

    public static final Block GRASS_BLOCK
            = register("grass_block", BlockSettings.Builder.get().create(), DIRT.getTexture().getBottomTextureID(), -1, -1);

    public static final Block SAND
            = register("sand", BlockSettings.Builder.get().create());

    public static final Block STONE
            = register("stone", BlockSettings.Builder.get().create());

    private static Block register(String name, BlockSettings settings) { return register(name, settings, -1, -1, -1); }
    private static Block register(String name, BlockSettings settings, int forceTopAndBottom) { return register(name, settings, forceTopAndBottom, forceTopAndBottom, -1); }
    private static Block register(String name, BlockSettings settings,
                                      int forceBottomTextureID, int forceTopTextureID, int forceFrontTextureID) {
        Block instance = new Block(name, settings, lastID);
        BLOCKS.put(lastID++, instance);
        BLOCK_NAMES.put(name, instance);
        var texturesToAdd = instance.getTexture().generatePaths(name, lastTextureID);

        if (instance.getTexture().getFrontTextureID() == -1)
            instance.getTexture().setFrontTextureID(instance.getTexture().getSideTextureID());
        if (forceBottomTextureID >= 0) instance.getTexture().setBottomTextureID(forceBottomTextureID);
        if (forceTopTextureID >= 0) instance.getTexture().setTopTextureID(forceTopTextureID);
        if (forceFrontTextureID >= 0) instance.getTexture().setFrontTextureID(forceFrontTextureID);

        blockTextures.addAll(texturesToAdd.get1());
        lastTextureID += texturesToAdd.get2();
        return instance;
    }

    public static Block getBlock(short ID) { return BLOCKS.getV(ID); }
    public static Block getBlock(String name) { return BLOCK_NAMES.getV(name); }

    public static short getBlockID(Block block) { return BLOCKS.getK(block); }
    public static short getBlockID(String name) { return BLOCK_NAMES.getV(name).getID(); }

    public static String getBlockName(short ID) { return BLOCKS.getV(ID).getName(); }
    public static String getBlockName(Block block) { return BLOCK_NAMES.getK(block); }
}