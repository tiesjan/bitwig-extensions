package com.tiesjan.bitwig_extensions.utils;

import com.bitwig.extension.callback.BooleanValueChangedCallback;

public final class BooleanValueUpdateHandler implements BooleanValueChangedCallback {
    private BooleanValue value;

    public BooleanValueUpdateHandler(BooleanValue value) {
        this.value = value;
    }

    @Override
    public void valueChanged(boolean value) {
        this.value.set(value);
    }
}
 