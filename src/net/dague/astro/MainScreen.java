package net.dague.astro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class MainScreen extends Activity implements OnClickListener  {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		

		ImageButton riseset = (ImageButton)findViewById(R.id.time_button);
		riseset.setClickable(true);
		riseset.setOnClickListener(this);
		
		ImageButton jupiter = (ImageButton)findViewById(R.id.jupiter_button);
		jupiter.setClickable(true);
		jupiter.setOnClickListener(this);
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
		// More buttons go here (if any) ...
		}
	}
}
