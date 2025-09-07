package com.tiesjan.bitwig_extensions.shared.extension;

import com.tiesjan.bitwig_extensions.shared.hardware.Hardware;
import com.tiesjan.bitwig_extensions.shared.midi.MidiChannel;

public final class ExtensionHardware {
    public final Hardware hardware;
    public final MidiChannel midiChannel;

    public ExtensionHardware(Hardware hardware, MidiChannel midiChannel) {
        this.hardware = hardware;
        this.midiChannel = midiChannel;
    }
}
