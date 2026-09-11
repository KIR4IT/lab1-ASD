package io;

/** Свой ввод без java.io. Читаем байты System.in.read() и собираем UTF-8 вручную. */
public final class Console {

    private Console() {}

    public static String readLine() {
        byte[] buf = new byte[8192];
        int n = 0;
        try {
            int b;
            while ((b = System.in.read()) != -1) {
                if (b == '\n') break;
                if (b == '\r') continue;
                if (n >= buf.length) break;
                buf[n++] = (byte) b;
            }
        } catch (Exception e) {
            throw new RuntimeException("read error");
        }

        // Ручная сборка UTF-8 → char[]
        char[] chars = new char[n];
        int ci = 0, bi = 0;
        while (bi < n) {
            int c = buf[bi++] & 0xFF;
            if (c < 0x80) {
                chars[ci++] = (char) c;
            } else if ((c & 0xE0) == 0xC0 && bi < n) {
                int c2 = buf[bi++] & 0x3F;
                chars[ci++] = (char) (((c & 0x1F) << 6) | c2);
            } else if ((c & 0xF0) == 0xE0 && bi + 1 < n) {
                int c2 = buf[bi++] & 0x3F;
                int c3 = buf[bi++] & 0x3F;
                chars[ci++] = (char) (((c & 0x0F) << 12) | (c2 << 6) | c3);
            } else if ((c & 0xF8) == 0xF0 && bi + 2 < n) {
                int c2 = buf[bi++] & 0x3F;
                int c3 = buf[bi++] & 0x3F;
                int c4 = buf[bi++] & 0x3F;
                int cp = ((c & 0x07) << 18) | (c2 << 12) | (c3 << 6) | c4;
                // surrogate pair для codepoint > 0xFFFF
                cp -= 0x10000;
                chars[ci++] = (char) (0xD800 + (cp >> 10));
                chars[ci++] = (char) (0xDC00 + (cp & 0x3FF));
            }
        }
        return new String(chars, 0, ci);
    }
}