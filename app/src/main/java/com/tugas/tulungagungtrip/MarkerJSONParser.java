package com.tugas.tulungagungtrip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MarkerJSONParser {

	public List<HashMap<String, String>> parse(JSONObject jObject) {

		JSONArray jMarkers = null;
		try {
			jMarkers = jObject.getJSONArray("markers");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return getMarkers(jMarkers);
	}

	private List<HashMap<String, String>> getMarkers(JSONArray jMarkers) {
		int markersCount = jMarkers.length();
		List<HashMap<String, String>> markersList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> marker = null;

		for (int i = 0; i < markersCount; i++) {
			try {
				marker = getMarker((JSONObject) jMarkers.get(i));
				markersList.add(marker);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return markersList;
	}

	private HashMap<String, String> getMarker(JSONObject jMarker)
			throws JSONException {

		HashMap<String, String> marker = new HashMap<String, String>();
		String lat = "-NA-";
		String lng = "-NA-";
		String in_nama = "-NA-";
		String in_alamat = "-NA-";

		try {
			if (!jMarker.isNull("lat")) {
				lat = jMarker.getString("lat");
			}

			if (!jMarker.isNull("lng")) {
				lng = jMarker.getString("lng");
			}
			if (!jMarker.isNull("nama")) {
				in_nama = jMarker.getString("nama");
			}
			if (!jMarker.isNull("kategori")) {
				in_alamat = jMarker.getString("kategori");
			}

			marker.put("lat", lat);
			marker.put("lng", lng);
			marker.put("kategori", in_alamat);
			marker.put("nama", in_nama);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return marker;
	}
}
