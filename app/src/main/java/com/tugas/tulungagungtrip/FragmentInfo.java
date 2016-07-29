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

public class FragmentInfo extends Activity {
	private static String url = "http://tulungagungtrip.hol.es";
	//private static String url = "http://wisatakungawi.esy.es/";

	static final int tampil_error = 1;
	ProgressDialog pDialog;
	ListAdapter adapter;
	ArrayList<HashMap<String, String>> resultList;
	ListView lv;
	String filepl;

	static String in_id = "id";
	static String in_nama = "nama";
	static String in_info = "info";
	static String in_tanggal = "tanggal";

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_info_list);

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
		Intent replyIntent = new Intent(FragmentInfo.this, FragmentInfo.class);
		startActivity(replyIntent);
		finish();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	private class DownloadList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(FragmentInfo.this);
			pDialog.setMessage("Tunggu Sebentar...");
			pDialog.setIndeterminate(false);
			// pDialog.setCancelable(true);
			pDialog.show();
		}

		protected Void doInBackground(Void... unused) {
			String url_param;
			url_param = "android.php?pl=info";

			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromUrl(url + url_param);
			Log.d("log", "url:" + url + url_param);
			try {
				JSONArray result = json.getJSONArray("result");
				for (int i = 0; i < result.length(); i++) {

					JSONObject c = result.getJSONObject(i);
					String id = c.getString("id");
					String nama = c.getString("nama");
					String info = c.getString("info");
					String tanggal = c.getString("tanggal");

					HashMap<String, String> map = new HashMap<String, String>();

					map.put(in_id, id);
					map.put(in_nama, nama);
					map.put(in_info, info);
					map.put(in_tanggal, tanggal);
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
					adapter = new FragmentInfo_Adapter(FragmentInfo.this,
							resultList);
					lv.setAdapter(adapter);

					lv.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Log.d("log", "Data:,");

							String id_pil2 = ((TextView) view
									.findViewById(R.id.id_info)).getText()
									.toString();
							String nama_pil2 = ((TextView) view
									.findViewById(R.id.judul_info)).getText()
									.toString();
							String info_pil2 = ((TextView) view
									.findViewById(R.id.isi_info)).getText()
									.toString();
							String tanggal_pil2 = ((TextView) view
									.findViewById(R.id.tanggal_info)).getText()
									.toString();

							Intent in = new Intent(getApplicationContext(),
									Fragment_Info_Detail.class);
							in.putExtra("id", id_pil2);
							in.putExtra("nama", nama_pil2);
							in.putExtra("info", info_pil2);
							in.putExtra("tanggal", tanggal_pil2);

							startActivity(in);

						}

					});

				}
			});

		}

	}

}
