package dev.m4nd3l.cubegame.game.world;

public class WorldData {
    private Player player;
    private long seed;
    private String name;

    public String getName() { return name; }
    public Player getPlayer() { return player; }
    public long getSeed() { return seed; }

    public WorldData setName(String name) { this.name = name; return this; }
    public WorldData setPlayer(Player player) { this.player = player; return this; }
    public WorldData setSeed(long seed) { this.seed = seed; return this; }
}
