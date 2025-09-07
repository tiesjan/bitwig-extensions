package com.tiesjan.bitwig_extensions.controllers.xone.hardware;

import java.util.Map;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.tiesjan.bitwig_extensions.shared.hardware.HardwareControl;
import com.tiesjan.bitwig_extensions.shared.hardware.HardwareControlMidiInfo;
import com.tiesjan.bitwig_extensions.shared.hardware.HardwareControlType;
import com.tiesjan.bitwig_extensions.utils.EnumLookupBuilder;

public enum XoneK2HardwareControl implements HardwareControl
{
    /* Encoders */
    // Top
    ENCODER_A(HardwareControlType.ENCODER, 0x00, 0x34),
    ENCODER_B(HardwareControlType.ENCODER, 0x01, 0x35),
    ENCODER_C(HardwareControlType.ENCODER, 0x02, 0x36),
    ENCODER_D(HardwareControlType.ENCODER, 0x03, 0x37),
    // Bottom
    ENCODER_Y(HardwareControlType.ENCODER, 0x14, 0x0D),
    ENCODER_Z(HardwareControlType.ENCODER, 0x15, 0x0E),

    /* Rotaries & Buttons */
    // Column 1
    ROTARY_A1(HardwareControlType.ROTARY, 0x04, null),
    BUTTON_A1(HardwareControlType.BUTTON, null, 0x30),
    ROTARY_A2(HardwareControlType.ROTARY, 0x08, null),
    BUTTON_A2(HardwareControlType.BUTTON, null, 0x2C),
    ROTARY_A3(HardwareControlType.ROTARY, 0x0C, null),
    BUTTON_A3(HardwareControlType.BUTTON, null, 0x28),
    // Column 2
    ROTARY_B1(HardwareControlType.ROTARY, 0x05, null),
    BUTTON_B1(HardwareControlType.BUTTON, null, 0x31),
    ROTARY_B2(HardwareControlType.ROTARY, 0x09, null),
    BUTTON_B2(HardwareControlType.BUTTON, null, 0x2D),
    ROTARY_B3(HardwareControlType.ROTARY, 0x0D, null),
    BUTTON_B3(HardwareControlType.BUTTON, null, 0x29),
    // Column 3
    ROTARY_C1(HardwareControlType.ROTARY, 0x06, null),
    BUTTON_C1(HardwareControlType.BUTTON, null, 0x32),
    ROTARY_C2(HardwareControlType.ROTARY, 0x0A, null),
    BUTTON_C2(HardwareControlType.BUTTON, null, 0x2E),
    ROTARY_C3(HardwareControlType.ROTARY, 0x0E, null),
    BUTTON_C3(HardwareControlType.BUTTON, null, 0x2A),
    // Column 4
    ROTARY_D1(HardwareControlType.ROTARY, 0x07, null),
    BUTTON_D1(HardwareControlType.BUTTON, null, 0x33),
    ROTARY_D2(HardwareControlType.ROTARY, 0x0B, null),
    BUTTON_D2(HardwareControlType.BUTTON, null, 0x2F),
    ROTARY_D3(HardwareControlType.ROTARY, 0x0F, null),
    BUTTON_D3(HardwareControlType.BUTTON, null, 0x2B),

    /* Faders */
    FADER_A(HardwareControlType.FADER, 0x10, null),
    FADER_B(HardwareControlType.FADER, 0x11, null),
    FADER_C(HardwareControlType.FADER, 0x12, null),
    FADER_D(HardwareControlType.FADER, 0x13, null),

    /* Pads */
    // Grid
    PAD_A(HardwareControlType.PAD, null, 0x24),
    PAD_B(HardwareControlType.PAD, null, 0x25),
    PAD_C(HardwareControlType.PAD, null, 0x26),
    PAD_D(HardwareControlType.PAD, null, 0x27),
    PAD_E(HardwareControlType.PAD, null, 0x20),
    PAD_F(HardwareControlType.PAD, null, 0x21),
    PAD_G(HardwareControlType.PAD, null, 0x22),
    PAD_H(HardwareControlType.PAD, null, 0x23),
    PAD_I(HardwareControlType.PAD, null, 0x1C),
    PAD_J(HardwareControlType.PAD, null, 0x1D),
    PAD_K(HardwareControlType.PAD, null, 0x1E),
    PAD_L(HardwareControlType.PAD, null, 0x1F),
    PAD_M(HardwareControlType.PAD, null, 0x18),
    PAD_N(HardwareControlType.PAD, null, 0x19),
    PAD_O(HardwareControlType.PAD, null, 0x1A),
    PAD_P(HardwareControlType.PAD, null, 0x1B),
    // Bottom row
    PAD_Y(HardwareControlType.PAD, null, 0x0C),
    PAD_Z(HardwareControlType.PAD, null, 0x0F);

    private static Map<Integer, XoneK2HardwareControl> controlByMidiCC = null;
    private static Map<Integer, XoneK2HardwareControl> controlByMidiNote = null;

    static {
        EnumLookupBuilder<XoneK2HardwareControl> midiCCLookupBuilder = new EnumLookupBuilder<>();
        EnumLookupBuilder<XoneK2HardwareControl> midiNoteLookupBuilder = new EnumLookupBuilder<>();

        for (XoneK2HardwareControl control : values()) {
            Integer midiCC = control.toMidiCCByte();
            if (midiCC != null) {
                midiCCLookupBuilder.put(control.toMidiCCByte(), control);
            }

            Integer midiNote = control.toMidiNoteByte();
            if (midiNote != null) {
                midiNoteLookupBuilder.put(control.toMidiNoteByte(), control);
            }
        }

        controlByMidiCC = midiCCLookupBuilder.getLookup();
        controlByMidiNote = midiNoteLookupBuilder.getLookup();
    }

    private final HardwareControlMidiInfo midiInfo;

    private XoneK2HardwareControl(HardwareControlType controlType, Integer midiCC, Integer midiNote) {
        this.midiInfo = new HardwareControlMidiInfo(controlType, midiCC, midiNote);
    }

    public static XoneK2HardwareControl fromShortMidiMessage(ShortMidiMessage msg) {
        final Integer dataByte = msg.getData1();

        XoneK2HardwareControl control = null;

        if (msg.isControlChange())
        {
            control = controlByMidiCC.get(dataByte);
        }
        else if (msg.isNoteOn() || msg.isNoteOff())
        {
            control = controlByMidiNote.get(dataByte);
        }

        if (control == null)
        {
            throw new IllegalArgumentException(String.format("Unable to parse control from data byte 0x%02X.", dataByte));
        }

        return control;
    }

    @Override
    public boolean isButton() {
        return midiInfo.controlType == HardwareControlType.BUTTON;
    }

    @Override
    public boolean isEncoder() {
        return midiInfo.controlType == HardwareControlType.ENCODER;
    }

    @Override
    public boolean isFader() {
        return midiInfo.controlType == HardwareControlType.FADER;
    }

    @Override
    public boolean isPad() {
        return midiInfo.controlType == HardwareControlType.PAD;
    }

    @Override
    public boolean isRotary() {
        return midiInfo.controlType == HardwareControlType.ROTARY;
    }

    @Override
    public Integer toMidiCCByte() {
        return midiInfo.midiCC;
    }

    @Override
    public Integer toMidiNoteByte() {
        return midiInfo.midiNote;
    }

    public Integer toMidiNoteWithLedColour(XoneK2HardwareLedColour ledColour) {
        Integer midiNote = midiInfo.midiNote;

        if (midiInfo.midiNote == null) {
            throw new IllegalArgumentException("Unable to apply color if control has no midi note set.");
        }

        return midiNote + ledColour.toMidiByte();
    }
};
