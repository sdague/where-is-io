package net.dague.astro;

import android.app.Activity;
import android.os.Bundle;

public class Details extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		String body = extras.getString("net.dague.astro.DetailBody");
		setContentView(R.layout.details);
		setTitle(body);
	}
}
