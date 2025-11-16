package com.tiesjan.bitwig_extensions.controllers.xone_k2;

import com.bitwig.extension.controller.api.Preferences;
import com.bitwig.extension.controller.api.SettableBooleanValue;
import com.tiesjan.bitwig_extensions.shared.extension.ExtensionPreferences;

public final class XoneK2ExtensionPreferences implements ExtensionPreferences {
    public final SettableBooleanValue debug;

    public XoneK2ExtensionPreferences(Preferences preferences) {
        this.debug = preferences.getBooleanSetting("Debug", "General", false);
    }
}
