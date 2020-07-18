package com.example.logback.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 模仿zipkin生成TraceId，源码是brave.internal.HexCodec
 */
public final class TraceUtil {
    private TraceUtil() {
    }

    private static final ThreadLocal<char[]> ID_BUFFER = new ThreadLocal<>();
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String generateId() {
        char[] data = idBuffer();
        writeHexLong(data, 0, ThreadLocalRandom.current().nextLong());
        return new String(data, 0, 16);
    }

    private static char[] idBuffer() {
        char[] idBuffer = ID_BUFFER.get();
        if (idBuffer == null) {
            idBuffer = new char[32];
            ID_BUFFER.set(idBuffer);
        }
        return idBuffer;
    }

    private static void writeHexLong(char[] data, int pos, long v) {
        writeHexByte(data, pos + 0, (byte) ((v >>> 56L) & 0xff));
        writeHexByte(data, pos + 2, (byte) ((v >>> 48L) & 0xff));
        writeHexByte(data, pos + 4, (byte) ((v >>> 40L) & 0xff));
        writeHexByte(data, pos + 6, (byte) ((v >>> 32L) & 0xff));
        writeHexByte(data, pos + 8, (byte) ((v >>> 24L) & 0xff));
        writeHexByte(data, pos + 10, (byte) ((v >>> 16L) & 0xff));
        writeHexByte(data, pos + 12, (byte) ((v >>> 8L) & 0xff));
        writeHexByte(data, pos + 14, (byte) (v & 0xff));
    }

    private static void writeHexByte(char[] data, int pos, byte b) {
        data[pos + 0] = HEX_DIGITS[(b >> 4) & 0xf];
        data[pos + 1] = HEX_DIGITS[b & 0xf];
    }
}
