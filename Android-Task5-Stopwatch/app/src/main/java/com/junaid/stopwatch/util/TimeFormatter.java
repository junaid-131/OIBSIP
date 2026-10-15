package com.junaid.stopwatch.util;

import java.util.Locale;

public final class TimeFormatter {

    private TimeFormatter() {}

    /** Main display: HH:MM:SS */
    public static String formatMain(long ms) {
        if (ms < 0) ms = 0;
        long hours   = ms / 3_600_000;
        long minutes = (ms % 3_600_000) / 60_000;
        long seconds = (ms % 60_000) / 1000;
        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
    }

    /** Millis portion: .mmm */
    public static String formatMillis(long ms) {
        if (ms < 0) ms = 0;
        return String.format(Locale.US, ".%03d", ms % 1000);
    }

    /** Full: HH:MM:SS.mmm */
    public static String formatFull(long ms) {
        return formatMain(ms) + formatMillis(ms);
    }

    /** Short (for laps): MM:SS.mmm */
    public static String formatShort(long ms) {
        if (ms < 0) ms = 0;
        long minutes = ms / 60_000;
        long seconds = (ms % 60_000) / 1000;
        long millis  = ms % 1000;
        return String.format(Locale.US, "%02d:%02d.%03d", minutes, seconds, millis);
    }
}