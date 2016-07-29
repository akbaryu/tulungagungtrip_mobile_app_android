package com.tugas.tulungagungtrip.maps;

import java.util.List;

import com.google.android.gms.maps.model.Polyline;

//class yang digunakan sebagai listener atau callback ketika class menggunakan draw route menggunakan class DrawRoute.java
public interface OnPointedListener {
	// dipanggil ketika draw line berhasil di tampilkan pada peta
	public void OnDrawLine(List<Polyline> pointRoute, double distance);
}
