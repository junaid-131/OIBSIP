package com.junaid.unitconverter.util;

import java.util.Locale;

public final class NumberFormatter {

    private NumberFormatter() {}

    public static String format(double v) {
        if (Double.isNaN(v) || Double.isInfinite(v)) return "—";
        double abs = Math.abs(v);
        if (abs == 0) return "0";

        // Very large or very small → scientific
        if (abs >= 1e12 || abs < 1e-6) {
            return String.format(Locale.US, "%.4e", v);
        }

        // Round to 6 significant decimal places
        String s;
        if (abs >= 1) {
            s = String.format(Locale.US, "%.6f", v);
        } else {
            s = String.format(Locale.US, "%.8f", v);
        }

        // Strip trailing zeros
        if (s.contains(".")) {
            s = s.replaceAll("0+$", "").replaceAll("\\.$", "");
        }
        return s;
    }
}