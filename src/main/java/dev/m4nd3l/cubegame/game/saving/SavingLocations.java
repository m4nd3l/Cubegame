package dev.m4nd3l.cubegame.game.saving;

import dev.m4nd3l.cubegame.toolbox.util.FileWrapper;

import java.io.File;
import java.nio.file.Paths;

public class SavingLocations {
    public static FileWrapper SETTINGS_FILE = new FileWrapper(Paths.get("data", "settings.dat").toUri());
    public static FileWrapper WORLDS_FOLDER = new FileWrapper(Paths.get("data", "saves").toUri());

    public static FileWrapper ASSETS_FOLDER = new FileWrapper(Paths.get("assets").toUri());
}
