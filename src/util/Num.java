package util;

/** Свои парсер и форматтер double. */
public final class Num {

    private Num() {}

    // ---------- ПАРСЕР ----------
    public static boolean isDouble(String s) {
        try { parse(s); return true; }
        catch (NumberFormatException e) { return false; }
    }

    public static double parse(String s) {
        s = s.trim();
        if (s.isEmpty()) throw new NumberFormatException("empty");

        int i = 0, len = s.length();
        boolean neg = false;
        if (s.charAt(0) == '+' || s.charAt(0) == '-') {
            neg = s.charAt(0) == '-';
            i = 1;
        }

        double result = 0;
        boolean hasDigits = false;

        while (i < len && isDigit(s.charAt(i))) {
            result = result * 10 + (s.charAt(i) - '0');
            hasDigits = true;
            i++;
        }

        if (i < len && s.charAt(i) == '.') {
            i++;
            double frac = 0, scale = 1;
            boolean fracDigits = false;
            while (i < len && isDigit(s.charAt(i))) {
                frac = frac * 10 + (s.charAt(i) - '0');
                scale *= 10;
                fracDigits = true;
                i++;
            }
            if (fracDigits) { result += frac / scale; hasDigits = true; }
        }

        if (!hasDigits) throw new NumberFormatException("no digits");

        if (i < len && (s.charAt(i) == 'e' || s.charAt(i) == 'E')) {
            i++;
            boolean expNeg = false;
            if (i < len && (s.charAt(i) == '+' || s.charAt(i) == '-')) {
                expNeg = s.charAt(i) == '-';
                i++;
            }
            int exp = 0;
            boolean expDigits = false;
            while (i < len && isDigit(s.charAt(i))) {
                exp = exp * 10 + (s.charAt(i) - '0');
                expDigits = true;
                i++;
            }
            if (!expDigits) throw new NumberFormatException("bad exp");
            if (expNeg) exp = -exp;
            double p = 1;
            for (int k = 0; k < (exp < 0 ? -exp : exp); k++) p *= 10;
            result = exp < 0 ? result / p : result * p;
        }

        if (i != len) throw new NumberFormatException("trailing");
        if (result != result || result == Double.POSITIVE_INFINITY
                || result == Double.NEGATIVE_INFINITY)
            throw new NumberFormatException("overflow");

        return neg ? -result : result;
    }

    // ---------- ФОРМАТТЕР ----------
    public static String format(double v) {
        if (v != v) return "NaN";
        if (v == Double.POSITIVE_INFINITY) return "Infinity";
        if (v == Double.NEGATIVE_INFINITY) return "-Infinity";

        boolean neg = v < 0;
        if (neg) v = -v;

        long intPart = (long) v;
        double fracPart = v - intPart;

        if (fracPart < 1e-12) return (neg ? "-" : "") + intPart;

        StringBuilder frac = new StringBuilder();
        double f = fracPart;
        for (int d = 0; d < 10 && f > 1e-12; d++) {
            f *= 10;
            int digit = (int) f;
            frac.append((char) ('0' + digit));
            f -= digit;
        }
        int end = frac.length();
        while (end > 0 && frac.charAt(end - 1) == '0') end--;
        frac.setLength(end);

        String res = intPart + (end == 0 ? "" : "." + frac);
        return neg ? "-" + res : res;
    }

    // ---------- SPLIT ----------
    public static String[] split(String s) {
        int count = 0;
        boolean inTok = false;
        for (int i = 0; i < s.length(); i++) {
            if (isSep(s.charAt(i))) inTok = false;
            else { if (!inTok) count++; inTok = true; }
        }
        String[] out = new String[count];
        int idx = 0;
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i <= s.length(); i++) {
            char c = (i < s.length()) ? s.charAt(i) : ' ';
            if (isSep(c)) {
                if (cur.length() > 0) { out[idx++] = cur.toString(); cur.setLength(0); }
            } else cur.append(c);
        }
        return out;
    }

    private static boolean isDigit(char c) { return c >= '0' && c <= '9'; }
    private static boolean isSep(char c) {
        return c == ' ' || c == ',' || c == ';' || c == '\t';
    }
}