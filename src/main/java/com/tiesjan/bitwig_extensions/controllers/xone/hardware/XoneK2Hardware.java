package com.tiesjan.bitwig_extensions.controllers.xone.hardware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.ClipLauncherSlot;
import com.bitwig.extension.controller.api.ClipLauncherSlotBank;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Track;
import com.bitwig.extension.controller.api.TrackBank;
import com.tiesjan.bitwig_extensions.shared.daw.LauncherSlot;
import com.tiesjan.bitwig_extensions.shared.daw.LauncherSlotState;
import com.tiesjan.bitwig_extensions.shared.hardware.Hardware;
import com.tiesjan.bitwig_extensions.shared.hardware.HardwareContext;

public class XoneK2Hardware extends Hardware
{
    private static final List<XoneK2HardwareControl> clipLauncherControls = List.of(
        XoneK2HardwareControl.PAD_A,
        XoneK2HardwareControl.PAD_B,
        XoneK2HardwareControl.PAD_C,
        XoneK2HardwareControl.PAD_D,
        XoneK2HardwareControl.PAD_E,
        XoneK2HardwareControl.PAD_F,
        XoneK2HardwareControl.PAD_G,
        XoneK2HardwareControl.PAD_H,
        XoneK2HardwareControl.PAD_I,
        XoneK2HardwareControl.PAD_J,
        XoneK2HardwareControl.PAD_K,
        XoneK2HardwareControl.PAD_L,
        XoneK2HardwareControl.PAD_M,
        XoneK2HardwareControl.PAD_N,
        XoneK2HardwareControl.PAD_O,
        XoneK2HardwareControl.PAD_P
    );

    private Map<XoneK2HardwareControl, LauncherSlot> launcherControlSlots = new HashMap<>();

    private ControllerHost host;
    private TrackBank trackBank;
    private Vector<ShortMidiMessage> midiMessageQueue = new Vector<ShortMidiMessage>();

    public void init(final HardwareContext context) {
        this.host = context.host;

        this.trackBank = host.createTrackBank(4, 0, clipLauncherControls.size());
        Track track = trackBank.getItemAt(2);
        ClipLauncherSlotBank clipLauncherSlotBank = track.clipLauncherSlotBank();
        for (int slotId = 0; slotId < clipLauncherSlotBank.getSizeOfBank(); slotId++) {
            ClipLauncherSlot clipLauncherSlot = clipLauncherSlotBank.getItemAt(slotId);

            LauncherSlot launcherSlot = new LauncherSlot(clipLauncherSlot);
            launcherControlSlots.put(clipLauncherControls.get(slotId), launcherSlot);
        }

        for (int i = 0; i < clipLauncherControls.size(); i++) {
            final XoneK2HardwareControl control = clipLauncherControls.get(i);

            XoneK2HardwareLedState state = XoneK2HardwareLedState.OFF;
            queueMidiMessage(ShortMidiMessage.NOTE_ON, control.toMidiNoteByte(), state.toMidiByte());
        }
    }

    @Override
    public void handleDawState() {
        for (XoneK2HardwareControl control : clipLauncherControls) {
            Integer midiNote = null;
            Integer stateByte = null;

            LauncherSlot launcherSlot = launcherControlSlots.get(control);
            LauncherSlotState nextState = launcherSlot.getNextState();
            switch (nextState) {
                case PLAYING:
                    midiNote = control.toMidiNoteWithLedColour(XoneK2HardwareLedColour.RED);
                    stateByte = XoneK2HardwareLedState.ON.toMidiByte();
                    break;

                case QUEUED:
                    midiNote = control.toMidiNoteWithLedColour(XoneK2HardwareLedColour.AMBER);
                    stateByte = XoneK2HardwareLedState.ON.toMidiByte();
                    break;

                case READY:
                    midiNote = control.toMidiNoteWithLedColour(XoneK2HardwareLedColour.GREEN);
                    stateByte = XoneK2HardwareLedState.ON.toMidiByte();
                    break;

                case EMPTY:
                    midiNote = control.toMidiNoteByte();
                    stateByte = XoneK2HardwareLedState.OFF.toMidiByte();
                    break;

                case null:
                    break;

                default:
                    throw new IllegalArgumentException(String.format("Unexpected launcher slot state: %r", nextState));
            }

            if (midiNote != null && stateByte != null) {
                queueMidiMessage(ShortMidiMessage.NOTE_ON, midiNote, stateByte);
            }
        }
    }

    @Override
    public boolean handleMidiMessage(final ShortMidiMessage midiMessage) {
        XoneK2HardwareControl control = XoneK2HardwareControl.fromShortMidiMessage(midiMessage);

        if (midiMessage.isNoteOn()) {
            if (control.isPad() && clipLauncherControls.contains(control)) {
                LauncherSlot launcherSlot = launcherControlSlots.get(control);
                if (launcherSlot.hasContent() && !launcherSlot.isPlaying()) {
                    launcherSlot.launch();
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public List<ShortMidiMessage> getQueuedMidiMessages() {
        final List<ShortMidiMessage> queuedMidiMessages = List.copyOf(midiMessageQueue);

        midiMessageQueue.clear();

        return queuedMidiMessages;
    }

    private void queueMidiMessage(Integer statusByte, Integer data1, Integer data2) {
        final ShortMidiMessage midiMessage = new ShortMidiMessage(statusByte, data1, data2);
        midiMessageQueue.add(midiMessage);
    }
}
