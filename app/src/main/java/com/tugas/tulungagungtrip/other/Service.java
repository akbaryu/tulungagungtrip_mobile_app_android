package com.tugas.tulungagungtrip.other;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Service {

	private static LinkedHashMap<String, Bitmap> bitmapCache = new LinkedHashMap<String, Bitmap>();

	public Service() {
	}

	public static InputStream RetrieveStream(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w(Service.class.getSimpleName(), "Error " + statusCode
						+ " From URL " + url);
				return null;
			}

			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();

		} catch (IOException e) {
			getRequest.abort();
			Log.w(Service.class.getSimpleName(), "Error from URL " + url, e);
		}

		return null;
	}

	public static Bitmap FecthBitmap(String url) {
		Log.d("bitmap url", url);
		synchronized (bitmapCache) {
			Bitmap bitmap = bitmapCache.get(url);
			if (bitmap != null) {
				Log.i("Service", "fetching bitmap GET -> " + url);
				return bitmap;
			}
		}
		Log.w("Service", "fetching bitmap NULL");
		return FetchBitmapFromURL(url);
	}

	private static Bitmap FetchBitmapFromURL(String url) {
		try {
			InputStream is = Service.RetrieveStream(url);
			if (is == null)
				return null;
			Bitmap bitmap = BitmapFactory
					.decodeStream(new FlushInputStream(is));
			AddBitmapToCache(url, bitmap);
			return bitmap;
		} catch (Exception e) {
			Log.w("Service", "fetching failed -> " + e.getMessage());
			return null;
		}
	}

	private static void AddBitmapToCache(String url, Bitmap bitmap) {
		if (bitmap != null) {
			synchronized (bitmapCache) {
				Log.i("Service", "add bitmap SET -> " + url);
				bitmapCache.put(url, bitmap);
			}
		}
	}

	static class FlushInputStream extends FilterInputStream {

		public FlushInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}

			return totalBytesSkipped;
		}
	}
}
