package com.tiesjan.bitwig_extensions.shared.hardware;

import com.bitwig.extension.controller.api.ControllerHost;
import com.tiesjan.bitwig_extensions.shared.extension.ExtensionPreferences;

public final class HardwareContext {
    public final ControllerHost host;
    public final ExtensionPreferences preferences;

    public HardwareContext(ControllerHost host, ExtensionPreferences preferences) {
        this.host = host;
        this.preferences = preferences;
    }
}
