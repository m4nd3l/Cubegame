package dev.m4nd3l.cubegame.game;

import dev.m4nd3l.cubegame.game.engine.CubegameEngine;
import dev.m4nd3l.loggerutil.LoggerUtils;
import dev.m4nd3l.loggerutil.logger.Logger;

public class Cubegame {
    public static Logger LOGGER = LoggerUtils.getLogger("MAIN");
    public static CubegameEngine cubegame;
    public static void main(String[] args) {
        LoggerUtils.enableColoredLogging();
        LOGGER.info("Starting...");
        cubegame = new CubegameEngine();
        cubegame.init();
        cubegame.start();
    }
}
