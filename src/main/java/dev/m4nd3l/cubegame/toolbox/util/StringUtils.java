package dev.m4nd3l.cubegame.toolbox.util;

public class StringUtils {
    public static String sanitize(String toSanitize) {
        return toSanitize
                .replace(" ", "_")
                .replace("<", "")
                .replace(">", "")
                .replace(":", "")
                .replace("\"", "")
                .replace("/", "")
                .replace("\\", "")
                .replace("|", "")
                .replace("?", "")
                .replace("*", "");
    }
}
