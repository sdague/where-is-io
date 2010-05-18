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
			i.setImageResource(R.drawable.jupiter_128x128);
		} else if (body.equals("Callisto")) {
			t.setText(R.string.callisto_details);
			i.setImageResource(R.drawable.callisto_128x128);
			setTitleColor(getResources().getColor(
					R.color.callisto));
		} else if (body.equals("Europa")) {
			t.setText(R.string.europa_details);
			i.setImageResource(R.drawable.europa_128x128);
			setTitleColor(getResources().getColor(
					R.color.europa));
		} else if (body.equals("Ganymede")) {
			t.setText(R.string.ganymede_details);
			i.setImageResource(R.drawable.ganymede_128x128);
			setTitleColor(getResources().getColor(
					R.color.ganymede));
		} else if (body.equals("Io")) {
			t.setText(R.string.io_details);
			i.setImageResource(R.drawable.io_128x128);
			setTitleColor(getResources().getColor(
					R.color.io));
		}
	}
}
