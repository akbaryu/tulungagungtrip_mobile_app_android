package com.tugas.tulungagungtrip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Fragment_Info_Detail extends Activity {

	String fileid, filenama, fileinfo, filetanggal;
	TextView tid, tnama, tinfo, ttanggal;
	ProgressDialog pDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_info_detail);

		Intent in = getIntent();
		fileid = in.getStringExtra("id");
		filenama = in.getStringExtra("nama");
		fileinfo = in.getStringExtra("info");
		filetanggal = in.getStringExtra("tanggal");

		Log.d("log", "kiriman detail:" + fileid + "," + filenama + ","
				+ fileinfo + "," + filetanggal);

		tnama = (TextView) findViewById(R.id.tnama);
		tinfo = (TextView) findViewById(R.id.tinfo);
		ttanggal = (TextView) findViewById(R.id.ttanggal);

		new DownloadList().execute();
	}

	private class DownloadList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Fragment_Info_Detail.this);
			pDialog.setMessage("Tunggu Sebentar...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected Void doInBackground(Void... unused) {
			return null;
		}

		protected void onPostExecute(Void unused) {

			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {

					tnama.setText(filenama);
					tinfo.setText(fileinfo);
					ttanggal.setText(filetanggal);

				}
			});

		}
	}
}
