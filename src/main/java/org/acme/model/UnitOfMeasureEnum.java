package org.acme.model;

public enum UnitOfMeasureEnum {
    UN(false),   // não fracionável
    KG(true),
    G(true),
    L(true),
    ML(true);

    private final boolean fractional;

    UnitOfMeasureEnum(boolean fractional) { this.fractional = fractional; }

    public boolean isFractional() { return fractional; }
}

