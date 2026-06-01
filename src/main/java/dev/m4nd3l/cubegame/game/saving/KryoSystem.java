package dev.m4nd3l.cubegame.game.saving;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import dev.m4nd3l.cubegame.game.world.world.WorldData;
import dev.m4nd3l.cubegame.toolbox.util.FileWrapper;
import dev.m4nd3l.loggerutil.LoggerUtils;
import dev.m4nd3l.loggerutil.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class KryoSystem {
    private static final Logger LOGGER = LoggerUtils.getLogger();

    private static final ThreadLocal<KryoContext> CONTEXT = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(WorldData.class);
        kryo.register(CubegameSettings.class);

        return new KryoContext(kryo);
    });

    public static void saveToFile(FileWrapper file, Object object) {
        if (object == null) return;

        KryoContext context = CONTEXT.get();
        file.create();
        try (FileOutputStream fileStream = new FileOutputStream(file.getFile())) {
            context.output.setOutputStream(fileStream);
            context.kryo.writeObject(context.output, object);
            context.output.flush();
        } catch (IOException e) {
            LOGGER.error("Error while saving " + object.getClass().getSimpleName() + " to " + file.getAbsolutePath(), e);
        } finally { context.output.setOutputStream(null); }
    }

    public static <T> T loadFromFile(FileWrapper file, Class<T> type) {
        if (!file.exists()) return null;

        KryoContext context = CONTEXT.get();
        try (FileInputStream fileStream = new FileInputStream(file.getFile())) {
            context.inputStream.setInputStream(fileStream);
            return context.kryo.readObject(context.inputStream, type);
        } catch (IOException e) {
            LOGGER.error("Error while loading " + file.getAbsolutePath() + " to " + type.getSimpleName() + " - " + e);
            return null;
        } finally { context.inputStream.setInputStream(null); }
    }

    private static class KryoContext {
        final Kryo kryo;
        final Output output;
        final Input inputStream;

        KryoContext(Kryo kryo) {
            this.kryo = kryo;
            this.output = new Output(4096, -1);
            this.inputStream = new Input(4096);
        }
    }
}