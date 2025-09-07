package com.tiesjan.bitwig_extensions.shared.hardware;

public final class HardwareControlMidiInfo {
    public final HardwareControlType controlType;
    public final Integer midiCC;
    public final Integer midiNote;

    public HardwareControlMidiInfo(HardwareControlType controlType, Integer midiCC, Integer midiNote) {
        if (midiCC == null && midiNote == null) {
            throw new IllegalArgumentException("At least one of `cc` and `note` should be set.");
        }

        this.controlType = controlType;
        this.midiCC = midiCC;
        this.midiNote = midiNote;
    }
}
