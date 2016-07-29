package com.tugas.tulungagungtrip;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class FragmentPeta extends FragmentActivity {
	final int RQS_GooglePlayServices = 1;
	private GoogleMap myMap;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.fragment_maps);

		FragmentManager myFragmentManager = getSupportFragmentManager();
		SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager
				.findFragmentById(R.id.map);

		myMap = mySupportMapFragment.getMap();

		LatLng kota_tulungagung = new LatLng(-8.0941083,111.8887856);

		 myMap.getUiSettings().setCompassEnabled(true);

		myMap.setMyLocationEnabled(true);

		myMap.getUiSettings().setZoomControlsEnabled(true);

		myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kota_tulungagung, 12));

		new RetrieveTask().execute();
	}

	private void addMarker(LatLng latlng, String ketegori, String nama) {
		if (latlng == null) {
			return;
		}
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(latlng);
		markerOptions.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.marker_wisata));
		markerOptions.title(nama);
		markerOptions.snippet(ketegori);
		myMap.addMarker(markerOptions);
	}

	private class RetrieveTask extends AsyncTask<Void, Void, String> {

		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(FragmentPeta.this);
			pDialog.setMessage("Menampilkan Lokasi Wisata...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			//private static String url = "http://localhost/wisatakungawi/";
			String strUrl = "http://tulungagungtrip.hol.es/android.php?pl=peta";
			URL url = null;
			StringBuffer sb = new StringBuffer();
			try {
				url = new URL(strUrl);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.connect();
				InputStream iStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(iStream));
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
				iStream.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return sb.toString();
		}

		@Override
		protected void onPostExecute(final String result) {
			super.onPostExecute(result);

			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					new ParserTask().execute(result);
				}
			});

		}

	}

	private class ParserTask extends
			AsyncTask<String, Void, List<HashMap<String, String>>> {
		@Override
		protected List<HashMap<String, String>> doInBackground(String... params) {
			MarkerJSONParser markerParser = new MarkerJSONParser();
			JSONObject json = null;
			try {
				json = new JSONObject(params[0]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			List<HashMap<String, String>> markersList = markerParser
					.parse(json);
			return markersList;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {
			for (int i = 0; i < result.size(); i++) {
				HashMap<String, String> marker = result.get(i);
				LatLng latlng = new LatLng(
						Double.parseDouble(marker.get("lat")),
						Double.parseDouble(marker.get("lng")));
				String in_alamat = new String(marker.get("kategori"));
				String in_nama = new String(marker.get("nama"));
				addMarker(latlng, in_alamat, in_nama);
			}
		}
	}

}
