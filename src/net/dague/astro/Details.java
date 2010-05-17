package net.dague.astro;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Details extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		setDetails();
	}
	
	private void setDetails() {
		Bundle extras = getIntent().getExtras();
		String body = extras.getString("net.dague.astro.DetailBody");
		setTitle(body);
		
		TextView t = (TextView)findViewById(R.id.details_content);
		ImageView i = (ImageView)findViewById(R.id.details_image);

		if (body.equals("Jupiter")) {
			t.setText(R.string.jupiter_details);
			i.setImageResource(R.drawable.jupiter);
		}
	}
}
