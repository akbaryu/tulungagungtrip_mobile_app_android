package com.tugas.tulungagungtrip.maps;

import android.location.Location;

public abstract class LocationResult {
	public enum LocationType {
		NETWORK, GPS, LAST_GOOD_GPS, LAST_GOOD_NETWORK;
	}

	public abstract void gotLocation(Location location, LocationType type);
}