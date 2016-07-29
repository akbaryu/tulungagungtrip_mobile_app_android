package com.tugas.tulungagungtrip;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Button;

public class FragmentWisata extends Fragment {

	String longitude, latitude;
	Button btnKeluarga;
	Button btnSejarah;
	Button btnAlam;
	Button btnBudaya;

	GPSTracker gps;
	String takelat, takelng;

	public int currentimageindex = 0;

	public FragmentWisata() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_wisata, container,
				false);

		btnKeluarga = (Button) rootView.findViewById(R.id.btn_wisatakeluarga);
		btnSejarah = (Button) rootView.findViewById(R.id.btn_wisatasejarah);
		btnAlam = (Button) rootView.findViewById(R.id.btn_wisataalam);
		btnBudaya = (Button) rootView.findViewById(R.id.btn_wisatabudaya);

		btnKeluarga.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent in = new Intent(v.getContext(), Wisata_Data.class);
				in.putExtra("pl", "wisata");
				in.putExtra("currentlatitude", latitude);
				in.putExtra("currentlongitude", longitude);
				in.putExtra("kategori", "keluarga");
				startActivity(in);
			}
		});

		btnSejarah.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(v.getContext(), Wisata_Data.class);
				in.putExtra("pl", "wisata");
				in.putExtra("currentlatitude", latitude);
				in.putExtra("currentlongitude", longitude);
				in.putExtra("kategori", "sejarah");
				startActivity(in);
			}
		});

		btnBudaya.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(v.getContext(), Wisata_Data.class);
				in.putExtra("pl", "wisata");
				in.putExtra("currentlatitude", latitude);
				in.putExtra("currentlongitude", longitude);
				in.putExtra("kategori", "budaya");
				startActivity(in);
			}
		});

		btnAlam.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(v.getContext(), Wisata_Data.class);
				in.putExtra("pl", "wisata");
				in.putExtra("currentlatitude", latitude);
				in.putExtra("currentlongitude", longitude);
				in.putExtra("kategori", "alam");
				startActivity(in);
			}
		});
		return rootView;
	}

}
