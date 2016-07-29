package com.tugas.tulungagungtrip;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Wisata_Data extends Activity {

	private static String url = "http://192.168.1.17/wisatakungawi/";
	//private static String url = "http://wisatakungawi.esy.es/";

	static final int tampil_error = 1;
	ProgressDialog pDialog;
	ListAdapter adapter;
	ArrayList<HashMap<String, String>> resultList;
	ListView lv;
	TextView tjudul;
	String filepl, filekategori;
	String longitude, latitude;

	String takelat, takelng;

	static String in_id = "id";
	static String in_nama = "nama";
	static String in_alamat = "alamat";
	static String in_keterangan = "keterangan";
	static String in_kategori = "kategori";
	static String in_jarak = "jarak";
	static String in_gambar = "gambar1";
	static String in_latitude = "latitude";
	static String in_longitude = "longitude";

	GPSTracker gps;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.explorelist);

		gps = new GPSTracker(Wisata_Data.this);

		if (gps.canGetLocation()) {

			double latitudeku = gps.getLatitude();
			double longitudeku = gps.getLongitude();
			takelat = "" + latitudeku;
			takelng = "" + longitudeku;
			Toast.makeText(getApplicationContext(),
					"Lokasi anda : " + takelat + "," + takelng,
					Toast.LENGTH_SHORT).show();
		} else {
			gps.showSettingsAlert();
		}

		Intent in = getIntent();
		filekategori = in.getStringExtra("kategori");
		filepl = in.getStringExtra("pl");
		Log.d("log", "url:" + url + filepl);

		resultList = new ArrayList<HashMap<String, String>>();
		if (cek_status(this) == true) {
			new DownloadList().execute();
		} else {
			showDialog(tampil_error);
		}
	}

	public boolean cek_status(Context cek) {
		ConnectivityManager cm = (ConnectivityManager) cek
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	// kirim error
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case tampil_error:
			AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
			errorDialog.setTitle("Koneksi Error");
			errorDialog.setMessage("Koneksi ke Server gagal...!!!");
			errorDialog.setNeutralButton("Ok",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
							finish();
						}
					});
			AlertDialog errorAlert = errorDialog.create();
			return errorAlert;
		default:
			break;
		}
		return dialog;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem refresh = menu.add(Menu.NONE, R.id.action_refresh, Menu.NONE,
				R.string.action_refresh);
		refresh.setIcon(R.drawable.navigation_refresh);
		refresh.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		refresh();
		return true;
	}

	private void refresh() {
		Intent replyIntent = new Intent(Wisata_Data.this, Wisata_Data.class);
		gps = new GPSTracker(Wisata_Data.this);
		startActivity(replyIntent);
		finish();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	// end menu

	private class DownloadList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Wisata_Data.this);
			pDialog.setMessage("Tunggu Sebentar...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected Void doInBackground(Void... unused) {
			String url_param;
			url_param = "android.php?pl=" + filepl + "&kategori="
					+ filekategori + "&latitude=" + takelat + "&longitude="
					+ takelng;

			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromUrl(url + url_param);
			Log.d("log", "url:" + url + url_param);
			try {
				JSONArray result = json.getJSONArray("result");
				for (int i = 0; i < result.length(); i++) {

					JSONObject c = result.getJSONObject(i);
					// double longitude = gps.getLongitude();
					String id_wisata = c.getString("id");
					String nama = c.getString("nama");
					String alamat = c.getString("alamat");
					String keterangan = c.getString("keterangan");
					String kategori = c.getString("kategori");
					String jarak = c.getString("jarak");
					String latitude2 = c.getString("latitude");
					String longitude2 = c.getString("longitude");

					String gambar = "http://wisatakungawi.esy.es/photo/"
							+ c.getString("gambar1");

					HashMap<String, String> map = new HashMap<String, String>();

					map.put(in_id, id_wisata);
					map.put(in_nama, nama);
					map.put(in_alamat, alamat);
					map.put(in_keterangan, keterangan);
					map.put(in_kategori, kategori);
					map.put(in_gambar, gambar);
					map.put(in_jarak, jarak);
					map.put(in_latitude, latitude2);
					map.put(in_longitude, longitude2);
					resultList.add(map);
				}
				Log.d("log", "bla:" + resultList);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Void unused) {

			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {

					lv = (ListView) findViewById(R.id.list);
					adapter = new Wisata_Adapter(Wisata_Data.this, resultList);
					lv.setAdapter(adapter);

					lv.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Log.d("log", "Data:,");

							String id_pil2 = ((TextView) view
									.findViewById(R.id.id_wisata)).getText()
									.toString();
							String nama_pil2 = ((TextView) view
									.findViewById(R.id.nama_wisata)).getText()
									.toString();
							String keterangan_pil2 = ((TextView) view
									.findViewById(R.id.keterangan_wisata))
									.getText().toString();
							String alamat_pil2 = ((TextView) view
									.findViewById(R.id.alamat_wisata))
									.getText().toString();
							String jarak_pil2 = ((TextView) view
									.findViewById(R.id.jarak)).getText()
									.toString();
							String latitude_pil2 = ((TextView) view
									.findViewById(R.id.latitudeb)).getText()
									.toString();
							String longitude_pil2 = ((TextView) view
									.findViewById(R.id.longitudeb)).getText()
									.toString();

							String gambar_pil2 = ((TextView) view
									.findViewById(R.id.nama_gambar_wisata))
									.getText().toString();

							Log.d("gambar", gambar_pil2);
							Intent in = new Intent(getApplicationContext(),
									Wisata_Detail.class);
							in.putExtra("id", id_pil2);
							in.putExtra("nama", nama_pil2);
							in.putExtra("keterangan", keterangan_pil2);
							in.putExtra("alamat", alamat_pil2);
							in.putExtra("jarak", jarak_pil2);
							in.putExtra("latitude", latitude_pil2);
							in.putExtra("longitude", longitude_pil2);
							in.putExtra("gambar", gambar_pil2);
							startActivity(in);

						}

					});

				}
			});

		}

	}

}
