package com.junaid.unitconverter.model;

public class Unit {

    private final String name;
    private final String symbol;
    private final Category category;
    private final double scale;
    private final double offset;

    public Unit(String name, String symbol, Category category, double scale, double offset) {
        this.name = name;
        this.symbol = symbol;
        this.category = category;
        this.scale = scale;
        this.offset = offset;
    }

    public Unit(String name, String symbol, Category category, double scale) {
        this(name, symbol, category, scale, 0);
    }

    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public Category getCategory() { return category; }
    public double getScale() { return scale; }
    public double getOffset() { return offset; }

    public double toBase(double value) { return value * scale + offset; }
    public double fromBase(double base) { return (base - offset) / scale; }

    @Override
    public String toString() {
        return name + " (" + symbol + ")";
    }
}