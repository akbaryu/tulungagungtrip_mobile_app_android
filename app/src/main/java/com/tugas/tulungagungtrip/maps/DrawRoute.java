package com.tugas.tulungagungtrip.maps;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

//class yang berhubungan dengan membuat rute pada peta
public class DrawRoute {

	private String url;
	private GoogleMap mMap;
	private double distance = 0;
	private OnPointedListener pointedListener;

	// constructor
	public DrawRoute(double startLat, double startLon, double endLat,
			double endLon) {
		url = makeURL(startLat, startLon, endLat, endLon);
	}

	// method yang di pakai di class lain untuk mengeksekusi draw route pada
	// mMap
	public void applyTo(GoogleMap mMap, Context context,
			OnPointedListener onPointedListener) {
		this.mMap = mMap;
		this.pointedListener = onPointedListener;
		new URLTask(listener, context, url, "Load route").execute();

	}

	// callback atau listener jika request berhasil atau tidak
	private ResultsListener listener = new ResultsListener() {

		@Override
		public void onResultsSucceeded(String result, Context context) {
			parseRoute(result);
		}

		@Override
		public void onResultsFailed(Context context) {
		}
	};

	// url menentukan jalur tercepat menggunakan googleapi
	private String makeURL(double sourcelat, double sourcelog, double destlat,
			double destlog) {
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(Double.toString(sourcelat));
		urlString.append(",");
		urlString.append(Double.toString(sourcelog));
		urlString.append("&destination=");// to
		urlString.append(Double.toString(destlat));
		urlString.append(",");
		urlString.append(Double.toString(destlog));
		urlString.append("&sensor=false&mode=driving&alternatives=true");
		return urlString.toString();
	}

	// parsing request dari google berupa data string (result) untuk jalur
	// tercepat
	private void parseRoute(String result) {

		try {
			// Tranform the string into a json object
			final JSONObject json = new JSONObject(result);
			JSONArray routeArray = json.getJSONArray("routes");
			JSONObject routes = routeArray.getJSONObject(0);
			JSONObject overviewPolylines = routes
					.getJSONObject("overview_polyline");
			String encodedString = overviewPolylines.getString("points");
			List<LatLng> list = decodePoly(encodedString);

			drawRoute(list, Color.BLUE);
		} catch (JSONException e) {
		}
	}

	// draw route pada peta di lakukan di method ini dengan bentuk rute sudah
	// menjadi List<LatLng> yg sebelumnya hanya string
	private void drawRoute(List<LatLng> list, int color) {
		List<Polyline> lines = new ArrayList<Polyline>();
		for (int z = 0; z < list.size() - 1; z++) {
			LatLng src = list.get(z);
			LatLng dest = list.get(z + 1);
			// perhitungan jarak, dihitung per checkpoint
			distance = distance
					+ SphericalCosinus(src.latitude, src.longitude,
							dest.latitude, dest.longitude);
			// polyline telah di add ke map
			Polyline line = mMap.addPolyline(new PolylineOptions()
					.add(new LatLng(src.latitude, src.longitude),
							new LatLng(dest.latitude, dest.longitude)).width(5)
					.color(color).geodesic(true));
			lines.add(line);
		}
		pointedListener.OnDrawLine(lines, distance);
	}

	// merubah entity string hasil dari json string menjadi titik-titik
	// checkpoint yg akan di draw line-nya
	private List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}

	// perhitungan jarak antara 2 titik pada map
	private double SphericalCosinus(double lat1, double lon1, double lat2,
			double lon2) {
		double R = 6371; // km
		double dLon = (lon2 - lon1) * Math.PI / 180;
		lat1 = lat1 * Math.PI / 180;
		lat2 = lat2 * Math.PI / 180;
		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1)
				* Math.cos(lat2) * Math.cos(dLon))
				* R;
		return d;
	}

	public String toString(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
