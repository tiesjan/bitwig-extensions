package com.tiesjan.bitwig_extensions.controllers.xone;

import com.bitwig.extension.controller.api.Preferences;
import com.bitwig.extension.controller.api.SettableBooleanValue;
import com.tiesjan.bitwig_extensions.shared.extension.ExtensionPreferences;

public final class XoneExtensionPreferences implements ExtensionPreferences {
    public final SettableBooleanValue debug;

    public XoneExtensionPreferences(Preferences preferences) {
        this.debug = preferences.getBooleanSetting("Debug", "General", false);
    }
}
