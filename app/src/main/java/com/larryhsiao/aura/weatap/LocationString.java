package com.larryhsiao.aura.weatap;

import android.location.Address;
import com.silverhetch.clotho.Source;

/**
 * Source to build a location string.
 */
public class LocationString implements Source<String> {
    private final Address address;

    public LocationString(Address address) {
        this.address = address;
    }

    @Override
    public String value() {
        final String sub = address.getSubLocality();
        final String locality = address.getLocality();
        return String.format(
                "%s%s%s",
                sub == null ? "" : sub,
                sub != null && locality != null ? ", " : "",
                locality == null ? "" : locality
        );
    }
}
