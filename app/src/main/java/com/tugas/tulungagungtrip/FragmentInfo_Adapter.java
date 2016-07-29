package com.tugas.tulungagungtrip;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FragmentInfo_Adapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public FragmentInfo_Adapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			vi = inflater.inflate(R.layout.fragment_info_data, null);
		TextView id = (TextView) vi.findViewById(R.id.id_info);
		TextView nama = (TextView) vi.findViewById(R.id.judul_info);
		TextView info = (TextView) vi.findViewById(R.id.isi_info);
		TextView tanggal = (TextView) vi.findViewById(R.id.tanggal_info);

		HashMap<String, String> berita = new HashMap<String, String>();
		berita = data.get(position);

		id.setText(berita.get(FragmentInfo.in_id));
		nama.setText(berita.get(FragmentInfo.in_nama));
		info.setText(berita.get(FragmentInfo.in_info));
		tanggal.setText(berita.get(FragmentInfo.in_tanggal));

		return vi;
	}
}