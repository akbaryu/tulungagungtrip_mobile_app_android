package com.tugas.tulungagungtrip;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Wisata_Detail extends FragmentActivity {
	String fileid, filenama, fileketerangan, filealamat, filekategori,
			filelongitude, filelatitude, filegambar, filejarak;
	TextView tnama, tketerangan, tkategori, talamat, tjarak;
	public ImageLoader imageLoader;
	public ImageView gambar;
	ProgressDialog pDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wisata_detail);

		Intent in = getIntent();
		fileid = in.getStringExtra("id");
		filenama = in.getStringExtra("nama");
		filealamat = in.getStringExtra("alamat");
		fileketerangan = in.getStringExtra("keterangan");
		filekategori = in.getStringExtra("kategori");
		filejarak = in.getStringExtra("jarak");
		filelongitude = in.getStringExtra("longitude");
		filelatitude = in.getStringExtra("latitude");
		filegambar = in.getStringExtra("gambar");

		gambar = (ImageView) findViewById(R.id.gambar);
		imageLoader = new ImageLoader(getApplicationContext());
		imageLoader.DisplayImage(filegambar, gambar);

		tnama = (TextView) findViewById(R.id.twisata);
		tketerangan = (TextView) findViewById(R.id.tketerangan_wisata);
		talamat = (TextView) findViewById(R.id.talamat);
		tjarak = (TextView) findViewById(R.id.tjarak);

		new DownloadList().execute();

		
	}
	private class DownloadList extends AsyncTask<Void, Void, Void> {
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Wisata_Detail.this);
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
					tketerangan.setText(fileketerangan);
					talamat.setText(filealamat);
					tjarak.setText(filejarak);

				}
			});

		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.ngawi, menu);
		MenuItem peta = menu.add(Menu.NONE, R.id.action_peta, Menu.NONE,
				R.string.action_peta);
		peta.setIcon(R.drawable.navigation_peta);
		peta.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		peta();
		return true;
	}

	private void peta() {
		Intent in = new Intent(getApplicationContext(),
				Peta_Lokasi.class);
		in.putExtra("id", fileid);
		in.putExtra("nama", filenama);
		in.putExtra("alamat", filealamat);
		in.putExtra("longitude", filelongitude);
		in.putExtra("latitude", filelatitude);
		startActivity(in);
	}
}
