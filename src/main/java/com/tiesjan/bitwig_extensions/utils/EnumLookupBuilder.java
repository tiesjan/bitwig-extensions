package com.tiesjan.bitwig_extensions.utils;

import java.util.HashMap;
import java.util.Map;

public final class EnumLookupBuilder<EnumT extends Enum<EnumT>> {
    private Map<Integer, EnumT> lookup = new HashMap<>();
    private boolean finalized = false;

    public void put(Integer midiByte, EnumT enumMember) {
        if (finalized == true) {
            throw new RuntimeException("Unable to put new items in lookup once it has been finalized.");
        }

        EnumT existingEnumMember = lookup.get(midiByte);

        if (midiByte == null || enumMember == null) {
            throw new IllegalArgumentException("Midi byte and enum member may not be null.");
        }

        if (existingEnumMember != null) {
            final String errorMessage = String.format(
                "Duplicate enum member for midi byte 0x%02X. Exising: %s; New: %s.",
                midiByte, existingEnumMember, enumMember
            );

            throw new IllegalArgumentException(errorMessage);
        }

        lookup.put(midiByte, enumMember);
    }

    public Map<Integer, EnumT> getLookup() {
        if (finalized == false) {
            finalized = true;
        }

        return lookup;
    }
}
