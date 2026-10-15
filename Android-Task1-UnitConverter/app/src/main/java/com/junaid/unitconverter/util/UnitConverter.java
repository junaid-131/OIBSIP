package com.junaid.unitconverter.util;

import com.junaid.unitconverter.model.Unit;

public final class UnitConverter {

    private UnitConverter() {}

    public static double convert(double value, Unit from, Unit to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }
        if (from.getCategory() != to.getCategory()) {
            throw new IllegalArgumentException("Cannot convert between categories");
        }
        return to.fromBase(from.toBase(value));
    }
}