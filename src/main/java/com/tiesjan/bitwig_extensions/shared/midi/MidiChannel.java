package com.tiesjan.bitwig_extensions.shared.midi;

import java.util.Map;

import com.tiesjan.bitwig_extensions.utils.EnumLookupBuilder;

public enum MidiChannel implements MidiByte {
    CHANNEL_01(0x0),
    CHANNEL_02(0x1),
    CHANNEL_03(0x2),
    CHANNEL_04(0x3),
    CHANNEL_05(0x4),
    CHANNEL_06(0x5),
    CHANNEL_07(0x6),
    CHANNEL_08(0x7),
    CHANNEL_09(0x8),
    CHANNEL_10(0x9),
    CHANNEL_11(0xA),
    CHANNEL_12(0xB),
    CHANNEL_13(0xC),
    CHANNEL_14(0xD),
    CHANNEL_15(0xE),
    CHANNEL_16(0xF);

    private static Map<Integer, MidiChannel> midiChannelByByte = null;

    static {
        EnumLookupBuilder<MidiChannel> midiCCLookupBuilder = new EnumLookupBuilder<>();

        for (MidiChannel midiChannel : values()) {
            midiCCLookupBuilder.put(midiChannel.toMidiByte(), midiChannel);
        }

        midiChannelByByte = midiCCLookupBuilder.getLookup();
    }

    private Integer midiByte;

    private MidiChannel(Integer midiByte) {
        this.midiByte = midiByte;
    }

    public static MidiChannel fromMidiByte(Integer midiByte) {
        return midiChannelByByte.get(midiByte);
    }

    @Override
    public Integer toMidiByte() {
        return midiByte;
    }
}
