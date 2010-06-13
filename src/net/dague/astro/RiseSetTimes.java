package net.dague.astro;

import java.util.List;

import net.dague.astro.sim.JupiterSim;
import net.dague.astro.util.TimeUtil;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class RiseSetTimes extends Activity implements OnClickListener {
	

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riseset);
        
        double now = TimeUtil.mils2JD(System.currentTimeMillis());
        JupiterSim js = new JupiterSim();
        
        double gps[] = getGPS();
        
        double rise = js.riseTime(gps[0], gps[1], now);
        double set = js.setTime(gps[0], gps[1], now);
        
        
        TextView jrise = (TextView) findViewById(R.id.jupiter_rise);
        jrise.setText("Jupiter Rise: " + rise);

        TextView jset = (TextView) findViewById(R.id.jupiter_set);
        jset.setText("Jupiter Set: " + set);
        
        
        // Set up click listeners for all the buttons
//        View continueButton = findViewById(R.id.continue_button);
//        continueButton.setOnClickListener(this);
//        View newButton = findViewById(R.id.new_button);
//        newButton.setOnClickListener(this);
//        View aboutButton = findViewById(R.id.about_button);
//        aboutButton.setOnClickListener(this);
//        View exitButton = findViewById(R.id.exit_button);
//        exitButton.setOnClickListener(this);
    }

	private double[] getGPS() {  

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);    
		List<String> providers = lm.getProviders(true);
		
		/* Loop over the array backwards, and if you get an accurate location, then break out the loop*/  
		Location l = null;  
		Log.i("GPS", "Numer of providers: " + providers.size());
  
		for (int i=providers.size()-1; i>=0; i--) {  
			Log.i("GPS", "Location provider: " + providers.get(i));
			l = lm.getLastKnownLocation(providers.get(i)); 
			if (l != null) break;
		}  
    
		double[] gps = new double[2];  
		Log.i("GPS", "L: " + l);
		if (l != null) {  
			gps[0] = l.getLatitude();  
			gps[1] = l.getLongitude(); 
		}  
		Log.i("GPS", "Lat: " + gps[0] + ", Lon: " + gps[1]);
		return gps;  
	}  

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	

}
