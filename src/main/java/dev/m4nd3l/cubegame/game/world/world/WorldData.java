package dev.m4nd3l.cubegame.game.world.world;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import dev.m4nd3l.cubegame.engine.rendering.input.Camera;
import dev.m4nd3l.cubegame.game.entities.Player;
import org.joml.Vector3f;

public class WorldData {
    @Tag(1) private Player player;
    @Tag(2) private long seed;
    @Tag(3) private String name;

    public Player getPlayer() { return player; }
    public long getSeed() { return seed; }
    public String getName() { return name; }

    public WorldData setName(String name) { this.name = name; return this; }
    public WorldData setPlayer(Player player) { this.player = player; return this; }
    public WorldData setSeed(long seed) { this.seed = seed; return this; }

    public WorldData setNewPlayer() {
        setPlayer(new Player(new Camera(0.1f, 1000.0f, 1920, 1080,
                new Vector3f(0.0f, 0.0f, 0.0f))));
        return this;
    }
}
