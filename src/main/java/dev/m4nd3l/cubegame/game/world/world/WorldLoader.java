package dev.m4nd3l.cubegame.game.world.world;

import dev.m4nd3l.cubegame.game.saving.KryoSystem;
import dev.m4nd3l.cubegame.game.saving.SavingLocations;
import dev.m4nd3l.cubegame.game.world.World;
import dev.m4nd3l.cubegame.toolbox.util.FileWrapper;
import dev.m4nd3l.cubegame.toolbox.util.StringUtils;

public class WorldLoader {
    public static World loadWorld(String worldName, long seed) {
        String sanitized = StringUtils.sanitize(worldName);
        FileWrapper worldFile = new FileWrapper(SavingLocations.WORLDS_FOLDER.getFile(), sanitized);
        return loadWorld(worldFile, worldName, seed);
    }

    private static World loadWorld(FileWrapper worldFile, String worldName, long seed) {
        WorldData data = null;
        FileWrapper worldDataFile = new FileWrapper(worldFile, "world.dat");
        if (worldFile.exists()) data = KryoSystem.loadFromFile(worldDataFile, WorldData.class);
        if (data == null) data  = new WorldData().setName(worldName).setSeed(seed).setNewPlayer();

        return new World(data, worldFile);
    }
}
