package dev.m4nd3l.cubegame.game.saving;

import java.io.File;
import java.nio.file.Paths;

public class SavingLocations {
    public static File WORLDS_FOLDER = new File(Paths.get("data", "saves").toUri());
}
