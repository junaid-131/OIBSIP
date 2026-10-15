package com.junaid.stopwatch.model;

public class Lap {
    public final int number;
    public final long totalMs;
    public final long splitMs;

    public Lap(int number, long totalMs, long splitMs) {
        this.number = number;
        this.totalMs = totalMs;
        this.splitMs = splitMs;
    }
}