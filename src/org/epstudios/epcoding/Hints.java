package org.epstudios.epcoding;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class Hints extends Activity {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String url = "file:///android_asset/hints.html";
		webView = new WebView(this);
		setContentView(webView);
		webView.loadUrl(url);
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.helpmenu, menu);
	// return super.onCreateOptionsMenu(menu);
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			// NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;
			// case R.id.about:
			// startActivity(new Intent(this, About.class));
			// return true;

		}

		return super.onOptionsItemSelected(item);
	}

}
