package com.tiesjan.bitwig_extensions.controllers.xone;

import java.util.List;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.MidiIn;
import com.bitwig.extension.controller.api.MidiOut;
import com.tiesjan.bitwig_extensions.controllers.xone.hardware.XoneK2Hardware;
import com.tiesjan.bitwig_extensions.shared.extension.ExtensionHardware;
import com.tiesjan.bitwig_extensions.shared.hardware.HardwareContext;
import com.tiesjan.bitwig_extensions.shared.midi.MidiChannel;

public class XoneExtension extends ControllerExtension
{
    private final ControllerHost host;
    private final ControllerExtensionDefinition extensionDefinition;
    private final List<ExtensionHardware> managedExtensionHardware;
    private final Integer midiInPort = 0;
    private final Integer midiOutPort = 0;

    private MidiIn midiIn;
    private MidiOut midiOut;
    private XoneExtensionPreferences preferences;

    protected XoneExtension(final XoneExtensionDefinition definition, final ControllerHost host)
    {
        super(definition, host);

        this.extensionDefinition = definition;
        this.host = host;

        this.managedExtensionHardware = List.of(
            new ExtensionHardware(new XoneK2Hardware(), MidiChannel.CHANNEL_15)
        );
    }

    @Override
    public void init()
    {
        this.preferences = new XoneExtensionPreferences(host.getPreferences());

        this.midiIn = host.getMidiInPort(midiInPort);
        midiIn.setMidiCallback((ShortMidiMessageReceivedCallback)midiMessage -> onMidi(midiMessage));

        this.midiOut = host.getMidiOutPort(midiOutPort);

        final HardwareContext hardwareContext = new HardwareContext(this.host, this.preferences);
        for (ExtensionHardware managedHardware : managedExtensionHardware) {
            managedHardware.hardware.init(hardwareContext);
        }

        host.showPopupNotification(String.format("%s initialized.", extensionDefinition.getHardwareModel()));
    }

    @Override
    public void exit()
    {
        host.showPopupNotification(String.format("%s exited.", extensionDefinition.getHardwareModel()));
    }

    @Override
    public void flush()
    {
        for (ExtensionHardware managedHardware : managedExtensionHardware) {
            managedHardware.hardware.handleDawState();

            Integer midiChannelByte = managedHardware.midiChannel.toMidiByte();

            for (ShortMidiMessage midiMessage : managedHardware.hardware.getQueuedMidiMessages()) {
                final Integer statusByte = midiMessage.getStatusByte() | midiChannelByte;
                final Integer data1 = midiMessage.getData1();
                final Integer data2 = midiMessage.getData2();

                this.midiOut.sendMidi(statusByte, data1, data2);
            }
        }
    }

    private void onMidi(ShortMidiMessage midiMessage)
    {
        boolean midiMessageHandled = false;
        MidiChannel midiChannel = MidiChannel.fromMidiByte(midiMessage.getChannel());

        for (ExtensionHardware managedHardware: managedExtensionHardware) {
            if (midiChannel == managedHardware.midiChannel) {
                midiMessageHandled = managedHardware.hardware.handleMidiMessage(midiMessage);
            }
        }

        if (this.preferences.debug.get() == true && midiMessageHandled == false) {
            host.println(String.format("Unhandled MIDI message -- %s", midiMessage.toString()));
        }
    }
}
