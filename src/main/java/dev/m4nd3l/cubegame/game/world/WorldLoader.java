package dev.m4nd3l.cubegame.game.world;

import dev.m4nd3l.cubegame.game.saving.SavingLocations;
import dev.m4nd3l.cubegame.toolbox.util.StringUtils;

import java.io.File;

public class WorldLoader {
    public static World load(String worldName, long seed) {
        String sanitized = StringUtils.sanitize(worldName);
        File worldFile = new File(SavingLocations.WORLDS_FOLDER, sanitized);
    }
}
