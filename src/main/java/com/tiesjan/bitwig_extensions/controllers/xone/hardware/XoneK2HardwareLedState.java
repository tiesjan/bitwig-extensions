package com.tiesjan.bitwig_extensions.controllers.xone.hardware;

import com.tiesjan.bitwig_extensions.shared.midi.MidiByte;

public enum XoneK2HardwareLedState implements MidiByte {
    ON(0x7F),
    OFF(0x00);

    private Integer midiByte;

    private XoneK2HardwareLedState(Integer midiByte) {
        this.midiByte = midiByte;
    }

    @Override
    public Integer toMidiByte() {
        return midiByte;
    }

}
