package com.tugas.tulungagungtrip.maps;

import com.tugas.tulungagungtrip.maps.LocationResult.LocationType;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyLocation {
	private Timer timer1;
	private LocationManager lm;
	private LocationResult locationResult;
	private Location locationNetwork;
	private boolean gps_enabled = false;
	private boolean network_enabled = false;

	public boolean getLocation(Context context, LocationResult result) {
		// I use LocationResult callback class to pass location value from
		// MyLocation to user code.
		locationResult = result;
		if (lm == null)
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled) {
			return false;
		}

		if (gps_enabled) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListenerGps);
		}

		if (network_enabled) {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListenerNetwork);
		}

		timer1 = new Timer();
		timer1.schedule(new GetLastLocation(), 15 * 1000);
		return true;
	}

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerNetwork);
			timer1.cancel();
			locationResult.gotLocation(location, LocationType.GPS);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			locationNetwork = location;
			lm.removeUpdates(this);
			if (!gps_enabled) {
				timer1.cancel();
				locationResult.gotLocation(location, LocationType.NETWORK);
			}
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	class GetLastLocation extends TimerTask {

		@Override
		public void run() {
			lm.removeUpdates(locationListenerGps);
			lm.removeUpdates(locationListenerNetwork);

			if (locationNetwork != null) {
				locationResult.gotLocation(locationNetwork,
						LocationType.NETWORK);
				return;
			}
			Location net_loc = null, gps_loc = null;
			if (gps_enabled)
				gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (network_enabled)
				net_loc = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			// if there are both values use the latest one
			if (gps_loc != null && net_loc != null) {
				if (gps_loc.getTime() > net_loc.getTime())
					locationResult.gotLocation(gps_loc,
							LocationType.LAST_GOOD_GPS);
				else
					locationResult.gotLocation(net_loc,
							LocationType.LAST_GOOD_NETWORK);
				return;
			}

			if (gps_loc != null) {
				locationResult.gotLocation(gps_loc, LocationType.LAST_GOOD_GPS);
				return;
			}
			if (net_loc != null) {
				locationResult.gotLocation(net_loc,
						LocationType.LAST_GOOD_NETWORK);
				return;
			}
			locationResult.gotLocation(null, null);
		}

	}
}
