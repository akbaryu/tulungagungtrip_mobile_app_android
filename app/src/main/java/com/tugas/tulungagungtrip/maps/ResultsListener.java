package com.tugas.tulungagungtrip.maps;

import android.content.Context;

public interface ResultsListener {
	public void onResultsSucceeded(String result, Context context);

	public void onResultsFailed(Context context);
}
