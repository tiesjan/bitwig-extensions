package com.tiesjan.bitwig_extensions.shared.daw;

import com.bitwig.extension.controller.api.ClipLauncherSlot;
import com.tiesjan.bitwig_extensions.utils.BooleanValue;
import com.tiesjan.bitwig_extensions.utils.BooleanValueUpdateHandler;

public final class LauncherSlot {
    private final ClipLauncherSlot clipLauncherSlot;

    private BooleanValue hasContent;
    private BooleanValue isPlaying;
    private BooleanValue isPlaybackQueued;

    private LauncherSlotState state;

    public LauncherSlot(final ClipLauncherSlot clipLauncherSlot) {
        this.clipLauncherSlot = clipLauncherSlot;

        this.hasContent = new BooleanValue(false);
        this.isPlaying = new BooleanValue(false);
        this.isPlaybackQueued = new BooleanValue(false);

        this.state = LauncherSlotState.EMPTY;

        clipLauncherSlot.hasContent().addValueObserver(new BooleanValueUpdateHandler(hasContent));
        clipLauncherSlot.isPlaying().addValueObserver(new BooleanValueUpdateHandler(isPlaying));
        clipLauncherSlot.isPlaybackQueued().addValueObserver(new BooleanValueUpdateHandler(isPlaybackQueued));
    }

    public boolean hasContent() {
        return hasContent.value();
    }

    public boolean isPlaying() {
        return isPlaying.value();
    }

    public boolean isPlaybackQueued() {
        return isPlaybackQueued.value();
    }

    public LauncherSlotState getState() {
        return state;
    }

    public LauncherSlotState getNextState() {
        LauncherSlotState nextState = null;

        if (isPlaying()) {
            if (state != LauncherSlotState.PLAYING) {
                nextState = LauncherSlotState.PLAYING;
            }
        }
        else if (isPlaybackQueued()) {
            if (state != LauncherSlotState.QUEUED) {
                nextState = LauncherSlotState.QUEUED;
            }
        }
        else if (hasContent()) {
            if (state != LauncherSlotState.READY) {
                nextState = LauncherSlotState.READY;
            }
        }
        else {
            if (state != LauncherSlotState.EMPTY) {
                nextState = LauncherSlotState.EMPTY;
            }
        }

        if (nextState != null) {
            state = nextState;
        }

        return nextState;
    }

    public void launch() {
        clipLauncherSlot.launch();
    }
   }
