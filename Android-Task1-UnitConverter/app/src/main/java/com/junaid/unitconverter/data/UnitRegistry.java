package com.junaid.unitconverter.data;

import com.junaid.unitconverter.model.Category;
import com.junaid.unitconverter.model.Unit;

import java.util.ArrayList;
import java.util.List;

public class UnitRegistry {

    private static final List<Unit> ALL = new ArrayList<>();

    static {
        // LENGTH (base: meter)
        ALL.add(new Unit("Millimeter", "mm", Category.LENGTH, 0.001));
        ALL.add(new Unit("Centimeter", "cm", Category.LENGTH, 0.01));
        ALL.add(new Unit("Meter", "m", Category.LENGTH, 1));
        ALL.add(new Unit("Kilometer", "km", Category.LENGTH, 1000));
        ALL.add(new Unit("Inch", "in", Category.LENGTH, 0.0254));
        ALL.add(new Unit("Foot", "ft", Category.LENGTH, 0.3048));
        ALL.add(new Unit("Yard", "yd", Category.LENGTH, 0.9144));
        ALL.add(new Unit("Mile", "mi", Category.LENGTH, 1609.344));

        // WEIGHT (base: kilogram)
        ALL.add(new Unit("Milligram", "mg", Category.WEIGHT, 0.000001));
        ALL.add(new Unit("Gram", "g", Category.WEIGHT, 0.001));
        ALL.add(new Unit("Kilogram", "kg", Category.WEIGHT, 1));
        ALL.add(new Unit("Ounce", "oz", Category.WEIGHT, 0.0283495));
        ALL.add(new Unit("Pound", "lb", Category.WEIGHT, 0.453592));
        ALL.add(new Unit("Stone", "st", Category.WEIGHT, 6.35029));
        ALL.add(new Unit("Tonne", "t", Category.WEIGHT, 1000));

        // TEMPERATURE (base: Celsius)
        ALL.add(new Unit("Celsius", "°C", Category.TEMPERATURE, 1, 0));
        ALL.add(new Unit("Fahrenheit", "°F", Category.TEMPERATURE, 5.0 / 9, -160.0 / 9));
        ALL.add(new Unit("Kelvin", "K", Category.TEMPERATURE, 1, -273.15));

        // VOLUME (base: liter)
        ALL.add(new Unit("Milliliter", "mL", Category.VOLUME, 0.001));
        ALL.add(new Unit("Liter", "L", Category.VOLUME, 1));
        ALL.add(new Unit("Cubic Meter", "m³", Category.VOLUME, 1000));
        ALL.add(new Unit("Teaspoon", "tsp", Category.VOLUME, 0.00492892));
        ALL.add(new Unit("Tablespoon", "tbsp", Category.VOLUME, 0.0147868));
        ALL.add(new Unit("Cup", "cup", Category.VOLUME, 0.236588));
        ALL.add(new Unit("Pint", "pt", Category.VOLUME, 0.473176));
        ALL.add(new Unit("Quart", "qt", Category.VOLUME, 0.946353));
        ALL.add(new Unit("Gallon", "gal", Category.VOLUME, 3.78541));

        // AREA (base: square meter)
        ALL.add(new Unit("Square Millimeter", "mm²", Category.AREA, 0.000001));
        ALL.add(new Unit("Square Centimeter", "cm²", Category.AREA, 0.0001));
        ALL.add(new Unit("Square Meter", "m²", Category.AREA, 1));
        ALL.add(new Unit("Square Kilometer", "km²", Category.AREA, 1_000_000));
        ALL.add(new Unit("Square Foot", "ft²", Category.AREA, 0.092903));
        ALL.add(new Unit("Square Yard", "yd²", Category.AREA, 0.836127));
        ALL.add(new Unit("Acre", "ac", Category.AREA, 4046.86));
        ALL.add(new Unit("Hectare", "ha", Category.AREA, 10_000));

        // SPEED (base: meter per second)
        ALL.add(new Unit("Meter/second", "m/s", Category.SPEED, 1));
        ALL.add(new Unit("Kilometer/hour", "km/h", Category.SPEED, 0.277778));
        ALL.add(new Unit("Mile/hour", "mph", Category.SPEED, 0.44704));
        ALL.add(new Unit("Foot/second", "ft/s", Category.SPEED, 0.3048));
        ALL.add(new Unit("Knot", "kn", Category.SPEED, 0.514444));

        // TIME (base: second)
        ALL.add(new Unit("Millisecond", "ms", Category.TIME, 0.001));
        ALL.add(new Unit("Second", "s", Category.TIME, 1));
        ALL.add(new Unit("Minute", "min", Category.TIME, 60));
        ALL.add(new Unit("Hour", "h", Category.TIME, 3600));
        ALL.add(new Unit("Day", "d", Category.TIME, 86400));
        ALL.add(new Unit("Week", "wk", Category.TIME, 604800));
        ALL.add(new Unit("Year", "yr", Category.TIME, 31_556_952));

        // DATA (base: byte)
        ALL.add(new Unit("Bit", "b", Category.DATA, 0.125));
        ALL.add(new Unit("Byte", "B", Category.DATA, 1));
        ALL.add(new Unit("Kilobyte", "KB", Category.DATA, 1024));
        ALL.add(new Unit("Megabyte", "MB", Category.DATA, 1024.0 * 1024));
        ALL.add(new Unit("Gigabyte", "GB", Category.DATA, 1024.0 * 1024 * 1024));
        ALL.add(new Unit("Terabyte", "TB", Category.DATA, 1024.0 * 1024 * 1024 * 1024));
    }

    public static List<Unit> getUnits(Category category) {
        List<Unit> result = new ArrayList<>();
        for (Unit u : ALL) {
            if (u.getCategory() == category) result.add(u);
        }
        return result;
    }

    public static Unit findByName(Category category, String name) {
        for (Unit u : ALL) {
            if (u.getCategory() == category && u.getName().equals(name)) return u;
        }
        return null;
    }
}