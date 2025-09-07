package com.tiesjan.bitwig_extensions.shared.hardware;

public interface HardwareControl
{
    public abstract boolean isButton();
    public abstract boolean isEncoder();
    public abstract boolean isFader();
    public abstract boolean isPad();
    public abstract boolean isRotary();

    public abstract Integer toMidiCCByte();
    public abstract Integer toMidiNoteByte();
}
