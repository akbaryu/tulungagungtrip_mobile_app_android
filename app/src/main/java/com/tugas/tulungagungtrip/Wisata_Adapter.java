package com.tugas.tulungagungtrip;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Wisata_Adapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public Wisata_Adapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.wisata_data, null);
		TextView id = (TextView) vi.findViewById(R.id.id_wisata);
		TextView nama = (TextView) vi.findViewById(R.id.nama_wisata);
		TextView alamat = (TextView) vi.findViewById(R.id.alamat_wisata);
		TextView keterangan = (TextView) vi
				.findViewById(R.id.keterangan_wisata);
		TextView kategori = (TextView) vi.findViewById(R.id.kategori_wisata);
		TextView jarak = (TextView) vi.findViewById(R.id.jarak);
		TextView latitude = (TextView) vi.findViewById(R.id.latitudeb);
		TextView longitude = (TextView) vi.findViewById(R.id.longitudeb);
		TextView namagambar = (TextView) vi
				.findViewById(R.id.nama_gambar_wisata);

		ImageView gambar = (ImageView) vi.findViewById(R.id.gambarb);

		HashMap<String, String> berita = new HashMap<String, String>();
		berita = data.get(position);

		id.setText(berita.get(Wisata_Data.in_id));
		nama.setText(berita.get(Wisata_Data.in_nama));
		alamat.setText(berita.get(Wisata_Data.in_alamat));
		keterangan.setText(berita.get(Wisata_Data.in_keterangan));
		kategori.setText(berita.get(Wisata_Data.in_kategori));
		jarak.setText(berita.get(Wisata_Data.in_jarak) + "  Km");
		latitude.setText(berita.get(Wisata_Data.in_latitude));
		longitude.setText(berita.get(Wisata_Data.in_longitude));
		namagambar.setText(berita.get(Wisata_Data.in_gambar));

		Log.d("log", "kiriman:" + berita.get(Wisata_Data.in_gambar) + ","
				+ gambar);
		imageLoader.DisplayImage(berita.get(Wisata_Data.in_gambar), gambar);

		return vi;
	}
}