package dev.m4nd3l.cubegame.game.saving;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public class CubegameSettings {
    @Tag(1) private String playerName;
    @Tag(2) private int renderDistance;
    @Tag(3) private float FOV;

    public String getPlayerName() { return playerName; }
    public int getRenderDistance() { return renderDistance; }
    public float getFOV() { return FOV; }

    public CubegameSettings setPlayerName(String playerName) { this.playerName = playerName; return this; }
    public CubegameSettings setRenderDistance(int renderDistance) { this.renderDistance = renderDistance; return this; }
    public CubegameSettings setFOV(float FOV) { this.FOV = FOV; return this; }
}
