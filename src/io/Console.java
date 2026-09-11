package io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/** Ввод строки с UTF-8. */
public final class Console {

    private Console() {}

    private static final BufferedReader R =
            new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

    public static String readLine() {
        try {
            String s = R.readLine();
            return s == null ? "" : s;
        } catch (Exception e) {
            throw new RuntimeException("read error");
        }
    }
}