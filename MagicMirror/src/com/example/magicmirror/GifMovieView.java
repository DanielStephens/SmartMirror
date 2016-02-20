package com.example.magicmirror;

import android.content.Context;
import android.webkit.WebView;

public class GifMovieView extends WebView {

	public GifMovieView(Context context, String path) {
		super(context);
		loadUrl(path);
	}
}
