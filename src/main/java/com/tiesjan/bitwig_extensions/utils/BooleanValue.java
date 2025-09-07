package com.tiesjan.bitwig_extensions.utils;

public final class BooleanValue {
    private boolean value;

    public BooleanValue(boolean value) {
        this.set(value);
    }

    public void set(boolean value) {
        this.value = value;
    }

    public boolean value() {
        return value;
    }
}
