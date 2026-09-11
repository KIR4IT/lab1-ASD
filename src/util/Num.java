package util;

/** Свои парсер, форматтер double и split. Без сторонних библиотек. */
public final class Num {

    private Num() {}

    // ================= ПАРСЕР =================
    public static boolean isDouble(String s) {
        try { parse(s); return true; }
        catch (NumberFormatException e) { return false; }
    }

    public static double parse(String s) {
        s = trim(s);
        if (s.length() == 0) throw new NumberFormatException("empty");

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
            int n = exp < 0 ? -exp : exp;
            for (int k = 0; k < n; k++) p *= 10;
            result = exp < 0 ? result / p : result * p;
        }

        if (i != len) throw new NumberFormatException("trailing");
        if (result != result || result == 1.0/0.0 || result == -1.0/0.0)
            throw new NumberFormatException("overflow");

        return neg ? -result : result;
    }

    // ================= ФОРМАТТЕР =================
    public static String format(double v) {
        if (v != v) return "NaN";
        if (v == 1.0/0.0) return "Infinity";
        if (v == -1.0/0.0) return "-Infinity";

        boolean neg = v < 0;
        if (neg) v = -v;

        long intPart = (long) v;
        double fracPart = v - intPart;

        if (fracPart < 1e-12) return (neg ? "-" : "") + intPart;

        // округляем до 6 знаков
        int digits = 6;
        double scale = 1;
        for (int k = 0; k < digits; k++) scale *= 10;
        long roundedFrac = (long) (fracPart * scale + 0.5);
        if (roundedFrac >= (long) scale) {
            intPart++;
            roundedFrac = 0;
        }
        if (roundedFrac == 0) return (neg ? "-" : "") + intPart;

        char[] buf = new char[digits];
        long f = roundedFrac;
        for (int k = digits - 1; k >= 0; k--) {
            buf[k] = (char) ('0' + (int) (f % 10));
            f /= 10;
        }
        int end = digits;
        while (end > 0 && buf[end - 1] == '0') end--;

        StringBuilder sb = new StringBuilder();
        if (neg) sb.append('-');
        sb.append(intPart).append('.');
        for (int k = 0; k < end; k++) sb.append(buf[k]);
        return sb.toString();
    }

    // ================= SPLIT =================
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

    // ================= TRIM =================
    public static String trim(String s) {
        int a = 0, b = s.length();
        while (a < b && s.charAt(a) <= ' ') a++;
        while (b > a && s.charAt(b - 1) <= ' ') b--;
        if (a == 0 && b == s.length()) return s;
        char[] buf = new char[b - a];
        for (int k = 0; k < buf.length; k++) buf[k] = s.charAt(a + k);
        return new String(buf);
    }

    private static boolean isDigit(char c) { return c >= '0' && c <= '9'; }
    private static boolean isSep(char c) {
        return c == ' ' || c == ',' || c == ';' || c == '\t';
    }
}