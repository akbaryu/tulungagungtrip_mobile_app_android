package com.tugas.tulungagungtrip.maps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class URLTask extends AsyncTask<Void, Void, String> {
	private final ProgressDialog progress;
	private ResultsListener listener;
	private String url;
	private String progressMessage;
	private Context context;

	public URLTask(ResultsListener listener, Context context, String url,
			String progressMessage) {
		this.listener = listener;
		this.url = url;
		progress = new ProgressDialog(context);
		this.progressMessage = progressMessage;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {

		if (progress != null && progressMessage != null) {
			progress.setMessage(progressMessage);
			progress.setCancelable(false);
			if (!((Activity) context).isFinishing()) {
				progress.show();
			}
		}
	}

	@Override
	protected String doInBackground(Void... voids) {
		String response = "";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams httpParameters = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 10 * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters, 10 * 1000);

		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse execute = client.execute(httpGet);
			InputStream content = execute.getEntity().getContent();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					content));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		if (progress.isShowing())
			progress.dismiss();
		Log.i("RESULT", result);
		if (result.equals("")) {
			listener.onResultsFailed(context);
		} else {
			listener.onResultsSucceeded(result, context);
		}
	}
}
