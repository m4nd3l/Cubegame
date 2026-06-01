package dev.m4nd3l.cubegame.toolbox.util;

import dev.m4nd3l.loggerutil.LoggerUtils;
import dev.m4nd3l.loggerutil.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Predicate;

public class FileWrapper {
    private static Logger LOGGER = LoggerUtils.getLogger();
    private File file;

    public FileWrapper(URI uri) { this.file = new File(uri); }
    public FileWrapper(Path path) { this.file = new File(path.toUri()); }
    public FileWrapper(File first, String... next) { this.file = Paths.get(first.getAbsolutePath(), next).toFile(); }
    public FileWrapper(String first, String... next) { this.file = Paths.get(first, next).toFile(); }
    public FileWrapper(FileWrapper first, String... next) { this.file = Paths.get(first.getAbsolutePath(), next).toFile(); }

    public boolean exists() { return file.exists(); }

    public boolean create() { return createFile(null); }
    public boolean create(String content) { return createFile(content); }
    public boolean createIf(Predicate<File> condition) { if (condition.test(file)) return create(); else return false; }
    public boolean createIf(Predicate<File> condition, String content) { if (condition.test(file)) return create(content); else return false; }

    public boolean createParentFolders() { return file.getParentFile().mkdirs(); }
    public boolean createParentFoldersIf(Predicate<File> condition) { if (condition.test(file)) return file.getParentFile().mkdirs(); else return false; }

    public boolean delete() { return file.delete(); }
    public boolean deleteIf(Predicate<File> condition) { if (condition.test(file)) return delete(); else return false; }

    public Path getPath() { return file.toPath(); }
    public File getFile() { return file; }
    public String getAbsolutePath() { return file.getAbsolutePath(); }
    public String getName() { return file.getName(); }

    public String readString() {
        if (!file.exists()) return "";
        try { return Files.readString(file.toPath()); }
        catch (IOException e) { LOGGER.error(e); return ""; }
    }

    public String readStringFromJarOrDisk() {
        if (file.exists()) return readString();

        String absolutePath = file.getAbsolutePath().replace("\\", "/");
        int assetsIdx = absolutePath.indexOf("/assets/");
        if (assetsIdx != -1) {
            String resourcePath = absolutePath.substring(assetsIdx);
            try (InputStream is = FileWrapper.class.getResourceAsStream(resourcePath)) {
                if (is != null) return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) { LOGGER.error("Failed to read shader resource from JAR: " + resourcePath, e); }
        }

        LOGGER.error("Could not find file on disk or inside JAR: " + file.getAbsolutePath());
        return "";
    }

    public void writeString(String text) { writeString(text, false); }
    public void writeString(String text, boolean append) {
        try {
            createParentFolders();
            Files.writeString(
                    file.toPath(),
                    text,
                    StandardOpenOption.CREATE,
                    append ? StandardOpenOption.APPEND: StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) { LOGGER.error(e); }
    }

    public byte[] readBytes() {
        if (!file.exists()) return new byte[0];
        try { return Files.readAllBytes(file.toPath()); }
        catch (IOException e) { LOGGER.error(e); return new byte[0]; }
    }

    public void writeBytes(byte[] data, boolean append) {
        try {
            createParentFolders();
            Files.write(
                    file.toPath(),
                    data,
                    StandardOpenOption.CREATE,
                    append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) { LOGGER.error(e); }
    }

    private boolean createFile(String content) {
        try {
            createParentFolders();
            boolean result = file.createNewFile();
            if (content == null || content.isEmpty()) return result;
            writeString(content);
            return result;
        } catch (IOException e) { LOGGER.error("Couldn't create " + file.getAbsolutePath() + " file."); }
        return false;
    }
}
