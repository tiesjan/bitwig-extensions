package com.tiesjan.bitwig_extensions.utils;

import com.bitwig.extension.callback.DoubleValueChangedCallback;

public final class DoubleValueUpdateHandler implements DoubleValueChangedCallback {
    private DoubleValue value;

    public DoubleValueUpdateHandler(DoubleValue value) {
        this.value = value;
    }

    @Override
    public void valueChanged(double value) {
        this.value.set(value);
    }
}
 