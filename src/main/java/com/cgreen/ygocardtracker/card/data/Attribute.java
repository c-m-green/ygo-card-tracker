package com.cgreen.ygocardtracker.card.data;

public enum Attribute {
    NONE("None"),
    DARK("DARK"),
    DIVINE("DIVINE"),
    EARTH("EARTH"),
    FIRE("FIRE"),
    LAUGH("LAUGH"),
    LIGHT("LIGHT"),
    WATER("WATER"),
    WIND("WIND");

    private final String name;
    private static final Attribute[] list = Attribute.values();

    Attribute(String name) {
        this.name = name;
    }

    public static Attribute getAttributeByName(String name) {
        for (Attribute attr : list) {
            if (attr.toString().equalsIgnoreCase(name)) {
                return attr;
            }
        }
        // TODO: Log this instead
        return NONE;
    }

    @Override
    public String toString() {
        return name;
    }
}