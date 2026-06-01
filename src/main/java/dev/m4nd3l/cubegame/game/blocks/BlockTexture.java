package dev.m4nd3l.cubegame.game.blocks;

import dev.m4nd3l.cubegame.toolbox.util.Mix;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BlockTexture {
    private short frontTextureID;
    private short sideTextureID;
    private short topTextureID;
    private short bottomTextureID;

    public BlockTexture(int all) { this(all, all, all, all); }
    public BlockTexture(int side, int top, int bottom) { this(side, side, top, bottom); }
    public BlockTexture(int front, int side, int top, int bottom) {
        frontTextureID = (short) front;
        sideTextureID = (short) side;
        topTextureID = (short) top;
        bottomTextureID = (short) bottom;
    }

    public short getBottomTextureID() { return bottomTextureID; }
    public short getFrontTextureID() { return frontTextureID; }
    public short getSideTextureID() { return sideTextureID; }
    public short getTopTextureID() { return topTextureID; }

    public BlockTexture setBottomTextureID(int bottomTextureID) { this.bottomTextureID = (short) bottomTextureID; return this; }
    public BlockTexture setFrontTextureID(int frontTextureID) { this.frontTextureID = (short) frontTextureID; return this; }
    public BlockTexture setSideTextureID(int sideTextureID) { this.sideTextureID = (short) sideTextureID; return this; }
    public BlockTexture setTopTextureID(int topTextureID) { this.topTextureID = (short) topTextureID; return this; }

    private BlockTexture setAll(int all) {
        frontTextureID = (short) all;
        sideTextureID = (short) all;
        topTextureID = (short) all;
        bottomTextureID = (short) all;
        return this;
    }

    public Mix<List<String>, Integer> generatePaths(String blockNamePrefix, int currentID) {
        List<Mix<String, Boolean>> possiblePaths = List.of(
                new Mix<>(join(blockNamePrefix), true),
                new Mix<>(join(blockNamePrefix + "_front"), false),
                new Mix<>(join(blockNamePrefix + "_side"), false),
                new Mix<>(join(blockNamePrefix + "_bottom"), false),
                new Mix<>(join(blockNamePrefix + "_top"), false)
        );
        List<String> finalPaths = new ArrayList<>(3);
        int toAdd = 0;
        boolean anyFileFound = false;

        for (Mix<String, Boolean> possiblePath : possiblePaths) {
            String resourcePath = possiblePath.get1();
            boolean isAllSides = possiblePath.get2();
            boolean exists = BlockTexture.class.getResource(resourcePath) != null;

            if (isAllSides) {
                if (exists) {
                    setAll(currentID + ++toAdd);
                    return new Mix<>(List.of(resourcePath), toAdd);
                } else continue;
            }

            if (resourcePath.endsWith("_front.png")) {
                if (exists) {
                    setFrontTextureID(currentID + ++toAdd);
                    anyFileFound = true;
                    finalPaths.add(resourcePath);
                }
                continue;
            }

            if (resourcePath.endsWith("_side.png")) {
                if (exists) {
                    setSideTextureID(currentID + ++toAdd);
                    anyFileFound = true;
                    finalPaths.add(resourcePath);
                }
                continue;
            }

            if (resourcePath.endsWith("_bottom.png")) {
                if (exists) {
                    setBottomTextureID(currentID + ++toAdd);
                    anyFileFound = true;
                    finalPaths.add(resourcePath);
                }
                continue;
            }

            if (resourcePath.endsWith("_top.png") && exists) {
                setTopTextureID(currentID + ++toAdd);
                anyFileFound = true;
                finalPaths.add(resourcePath);
            }
        }

        if (!anyFileFound) {
            setAll(0);
            return new Mix<>(List.of(), 0);
        }

        return new Mix<>(finalPaths, toAdd);
    }

    public static String join(String filename) { return "/assets/textures/blocks/" + filename + ".png"; }

    @Override
    public String toString() {
        return "BlockTexture{" + "bottomTextureID=" + bottomTextureID + ", frontTextureID=" + frontTextureID + ", sideTextureID=" + sideTextureID + ", topTextureID=" + topTextureID + '}';
    }
}