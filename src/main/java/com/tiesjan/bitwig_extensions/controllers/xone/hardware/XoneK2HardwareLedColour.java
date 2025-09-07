package com.tiesjan.bitwig_extensions.controllers.xone.hardware;

import com.tiesjan.bitwig_extensions.shared.midi.MidiByte;

public enum XoneK2HardwareLedColour implements MidiByte {
    RED(0x00),
    AMBER(0x24),
    GREEN(0x48);

    private Integer midiByte;

    private XoneK2HardwareLedColour(Integer midiByte) {
        this.midiByte = midiByte;
    }

    public Integer toMidiByte() {
        return midiByte;
    }

}
