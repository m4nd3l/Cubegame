package dev.m4nd3l.cubegame.game;

import dev.m4nd3l.cubegame.engine.CubegameEngine;
import dev.m4nd3l.cubegame.game.registries.BlockRegistry;
import dev.m4nd3l.cubegame.game.saving.CubegameSettings;
import dev.m4nd3l.cubegame.game.saving.KryoSystem;
import dev.m4nd3l.cubegame.game.saving.SavingLocations;
import dev.m4nd3l.loggerutil.LoggerUtils;
import dev.m4nd3l.loggerutil.logger.Logger;

import static dev.m4nd3l.cubegame.game.registries.BlockRegistry.GRASS_BLOCK;

public class Cubegame {
    public static Logger LOGGER = LoggerUtils.getLogger();
    public static CubegameEngine CUBEGAME;
    public static CubegameSettings SETTINGS;
    public static void main(String[] args) {
        LoggerUtils.enableColoredLogging();
        LOGGER.info("Starting...");

        SETTINGS = new CubegameSettings()
                .setPlayerName("Player")
                .setRenderDistance(16)
                .setFOV(80);

        if (SavingLocations.SETTINGS_FILE.exists())
            SETTINGS = KryoSystem.loadFromFile(SavingLocations.SETTINGS_FILE, CubegameSettings.class);
        else KryoSystem.saveToFile(SavingLocations.SETTINGS_FILE, SETTINGS);

        CUBEGAME = new CubegameEngine("Cubegame", 1920, 1080, true, true, 120);
        CUBEGAME.init();
        CUBEGAME.start();
        CUBEGAME.terminate();
    }
}
