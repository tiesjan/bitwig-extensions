package com.tiesjan.bitwig_extensions.utils;

public final class DoubleValue {
    private double value;

    public DoubleValue(double value) {
        this.set(value);
    }

    public void set(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }
}
