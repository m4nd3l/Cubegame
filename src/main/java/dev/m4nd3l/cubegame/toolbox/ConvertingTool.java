package dev.m4nd3l.cubegame.toolbox;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ConvertingTool {
    private static final ThreadLocal<MessageDigest> DIGEST_HOLDER = ThreadLocal.withInitial(() -> {
        try { return MessageDigest.getInstance("SHA-256"); }
        catch (NoSuchAlgorithmException e) { throw new RuntimeException("SHA-256 not supported!", e); }
    });

    private static ByteBuffer getHashBuffer(String input) {
        MessageDigest digest = DIGEST_HOLDER.get();
        digest.reset();
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return ByteBuffer.wrap(hashBytes);
    }

    public static long convertToLong(String input) { return getHashBuffer(input).getLong(); }
    public static int convertToInt(String input) { return getHashBuffer(input).getInt(); }
    public static short convertToShort(String input) { return getHashBuffer(input).getShort(); }
    public static byte convertToByte(String input) { return getHashBuffer(input).get(); }
    public static double convertToDouble(String input) { long val = convertToLong(input); return (val >>> 11) / (double) (1L << 53); }
    public static float convertToFloat(String input) { int val = convertToInt(input); return (val >>> 8) / (float) (1 << 24); }
}