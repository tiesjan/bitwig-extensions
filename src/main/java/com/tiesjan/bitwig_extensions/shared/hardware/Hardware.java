package com.tiesjan.bitwig_extensions.shared.hardware;

import java.util.List;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;

public abstract class Hardware
{
    public abstract void init(final HardwareContext context);
    public abstract void handleDawState();
    public abstract boolean handleMidiMessage(final ShortMidiMessage midiMessage);
    public abstract List<ShortMidiMessage> getQueuedMidiMessages();
}
