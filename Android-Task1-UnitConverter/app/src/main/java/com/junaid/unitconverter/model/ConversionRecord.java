package com.junaid.unitconverter.model;

public class ConversionRecord {
    public final String fromText;
    public final String toText;
    public final long timestamp;

    public ConversionRecord(String fromText, String toText, long timestamp) {
        this.fromText = fromText;
        this.toText = toText;
        this.timestamp = timestamp;
    }
}