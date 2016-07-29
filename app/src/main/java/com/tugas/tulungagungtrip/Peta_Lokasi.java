package com.tugas.tulungagungtrip;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.tugas.tulungagungtrip.maps.DrawRoute;
import com.tugas.tulungagungtrip.maps.LocationResult;
import com.tugas.tulungagungtrip.maps.MyLocation;
import com.tugas.tulungagungtrip.maps.OnPointedListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class Peta_Lokasi extends FragmentActivity implements LocationListener {

	Button btnPlus;
	Button btnMinus;
	Button btnLokasi;
	Button btnRefresh;
	Button btnRoute;
	Button btnView;

	GPSTracker gps;
	String takelat, takelng;
	String fileid, filenama, filealamat, filelongitude, filelatitude;
	GoogleMap map;
	private GoogleMap myMap;
	MarkerOptions option;

	ProgressDialog pDialog;
	private LatLng userLocation;
	Double destLatitude, destLongitude;

	ArrayList<LatLng> mMarkerPoints;
	double mLatitude = 0;
	double mLongitude = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.peta__lokasi);

		btnMinus = (Button) findViewById(R.id.btn_minus);
		btnPlus = (Button) findViewById(R.id.btn_plus);
		btnLokasi = (Button) findViewById(R.id.btn_gps);
		btnRefresh = (Button) findViewById(R.id.btn_Refresh);
		btnRoute = (Button) findViewById(R.id.btn_Route);
		btnView = (Button) findViewById(R.id.btn_View);

		Intent in = getIntent();
		fileid = in.getStringExtra("id");
		filenama = in.getStringExtra("nama");
		filealamat = in.getStringExtra("alamat");
		filelongitude = in.getStringExtra("longitude");
		filelatitude = in.getStringExtra("latitude");

		Log.d("log", "kiriman detail:" + filelatitude + "," + filelongitude);

		FragmentManager myFragmentManager = getSupportFragmentManager();
		SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager
				.findFragmentById(R.id.map);
		myMap = mySupportMapFragment.getMap();

		myMap.getUiSettings().setCompassEnabled(true);
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		myMap.setMyLocationEnabled(true);

		final RadioGroup rgViews = (RadioGroup) findViewById(R.id.rg_views);
		new DownloadList().execute();

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		Criteria criteria = new Criteria();

		String provider = locationManager.getBestProvider(criteria, true);

		Location location = locationManager.getLastKnownLocation(provider);

		if (location != null) {
			onLocationChanged(location);
		}

		locationManager.requestLocationUpdates(provider, 150000, 0, this);

		rgViews.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_normal) {
					myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					rgViews.setVisibility(View.GONE);
				} else if (checkedId == R.id.rb_satellite) {
					myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					rgViews.setVisibility(View.GONE);
				} else if (checkedId == R.id.rb_terrain) {
					myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
					rgViews.setVisibility(View.GONE);
				}

			}
		});

		btnMinus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				rgViews.setVisibility(View.GONE);
				myMap.animateCamera(CameraUpdateFactory.zoomTo(myMap
						.getCameraPosition().zoom - 0.5f));
			}
		});

		btnPlus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				rgViews.setVisibility(View.GONE);

				myMap.animateCamera(CameraUpdateFactory.zoomTo(myMap
						.getCameraPosition().zoom + 0.5f));

			}
		});

		btnLokasi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				rgViews.setVisibility(View.GONE);

			}
		});

		btnRefresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				myMap.clear();
				rgViews.setVisibility(View.GONE);
				new DownloadList().execute();

			}
		});

		btnRoute.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				rgViews.setVisibility(View.GONE);
				searchLocation();
			}
		});

		btnView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				RadioGroup rgViews = (RadioGroup) findViewById(R.id.rg_views);
				rgViews.setVisibility(View.VISIBLE);

			}
		});

	}

	private class DownloadList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Peta_Lokasi.this);
			pDialog.setMessage("Tunggu Sebentar...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected Void doInBackground(Void... unused) {
			destLatitude = Double.parseDouble(filelatitude);
			destLongitude = Double.parseDouble(filelongitude);
			return null;
		}

		protected void onPostExecute(Void unused) {

			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {

					addMarkerToMap(new LatLng(destLatitude, destLongitude),
							true);

				}
			});
		}
	}

	private void addMarkerToMap(LatLng point, boolean isDraggable) {
		if (point == null) {
			return;
		}
		LatLng marker = new LatLng(destLatitude, destLongitude);
		MarkerOptions options = new MarkerOptions();
		options.position(marker);
		options.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.marker_wisata));
		options.title(filenama);
		options.snippet(filealamat);
		myMap.addMarker(options);
		myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13));

	}

	private void searchLocation() {
		final ProgressDialog dialog = ProgressDialog.show(this, "",
				"Mencari Lokasi");
		try {

			myMap.clear();
			LocationResult locationResult = new LocationResult() {

				@Override
				public void gotLocation(final Location location,
						LocationType type) {
					if (location == null) {
						Peta_Lokasi.this.runOnUiThread(new Runnable() {

							public void run() {
								dialog.dismiss();
								popUpMessage("Tidak dapat menemukan lokasi");
							}
						});
					} else {
						Peta_Lokasi.this.runOnUiThread(new Runnable() {

							public void run() {
								dialog.dismiss();
								if (myMap != null) {
									userLocation = new LatLng(location
											.getLatitude(), location
											.getLongitude());
									addMarkerToMap(userLocation, false);

									doDrawFastestRoute(userLocation.latitude,
											userLocation.longitude,
											destLatitude, destLongitude);

								}
							}
						});
					}
				}
			};
			MyLocation myLocation = new MyLocation();
			myLocation.getLocation(this, locationResult);
		} catch (Exception e) {
			dialog.dismiss();
			popUpMessage("Tidak dapat menemukan lokasi");
		}
	}

	private void popUpMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private void doDrawFastestRoute(final double srcLatitude,
			final double srcLongitude, double destLatitude, double destLongitude) {
		DrawRoute route = new DrawRoute(srcLatitude, srcLongitude,
				destLatitude, destLongitude);
		route.applyTo(myMap, this, new OnPointedListener() {

			@Override
			public void OnDrawLine(List<Polyline> pointRoute, double distance) {
				myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						srcLatitude, srcLongitude), 15f));
			}
		});

		// distance = (String)route.toString("distance");

	}

	@Override
	public void onLocationChanged(Location location) {

		double latitude = location.getLatitude();

		double longitude = location.getLongitude();

		// LatLng latLng = new LatLng(latitude, longitude);

		takelat = "" + latitude;
		takelng = "" + longitude;

		Toast.makeText(getApplicationContext(),
				"Lokasi anda : " + takelat + takelng, Toast.LENGTH_SHORT)
				.show();

		// myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
