package dev.m4nd3l.cubegame.game.world.world;

import dev.m4nd3l.cubegame.game.saving.KryoSystem;
import dev.m4nd3l.cubegame.game.saving.SavingLocations;
import dev.m4nd3l.cubegame.toolbox.util.FileWrapper;
import dev.m4nd3l.cubegame.toolbox.util.StringUtils;

import java.io.File;

public class WorldLoader {
    public static World loadWorld(String worldName, long seed) {
        String sanitized = StringUtils.sanitize(worldName);
        File worldFile = new File(SavingLocations.WORLDS_FOLDER.getFile(), sanitized);
        return loadWorld(worldFile, worldName, seed);
    }

    private static World loadWorld(File worldFile, String worldName, long seed) {
        World world;
        WorldData data = new WorldData()
                .setName(worldName)
                .setSeed(seed)
                .setNewPlayer();
        FileWrapper worldDataFile = new FileWrapper(worldFile, "world.dat");
        if (worldFile.exists()) data = KryoSystem.loadFromFile(worldDataFile, WorldData.class);
        else KryoSystem.saveToFile(worldDataFile, data);

        world = new World(data);
        return world;
    }
}
