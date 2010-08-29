package net.dague.astro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainScreen extends Activity implements OnClickListener  {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		

		Button riseset = (Button)findViewById(R.id.time_button);
		riseset.setClickable(true);
		riseset.setOnClickListener(this);
		
		Button jupiter = (Button)findViewById(R.id.jupiter_button);
		jupiter.setClickable(true);
		jupiter.setOnClickListener(this);
		
		Button about = (Button)findViewById(R.id.about_button);
		about.setClickable(true);
		about.setOnClickListener(this);
		
		ImageView diarama = (ImageView)findViewById(R.id.diarama);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.jupiter_button:
		   Intent i = new Intent(this, JovianSpiral.class);
		   startActivity(i);
		   break;
		case R.id.time_button:
			Intent i1 = new Intent(this, RiseSetTimes.class);
			startActivity(i1);
			break;
		case R.id.about_button:
			startActivity(new Intent(this, About.class));
			break;
		// More buttons go here (if any) ...
		}
	}
}
