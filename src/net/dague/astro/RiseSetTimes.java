package net.dague.astro;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import net.dague.astro.sim.JupiterSim;
import net.dague.astro.sim.RiseCalculator;
import net.dague.astro.sim.SolarSim;
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
        
        double gps[] = getGPS();

        
        double now = TimeUtil.JDfloor(TimeUtil.mils2JD(System.currentTimeMillis()));
        // set now for the example program
        // now = 2447240.5;
        gps[0] = 42.3333;
        gps[1] = -71.0833;
        
        RiseCalculator rs = new RiseCalculator(gps[0], gps[1]);
        
        // Get us these as del
        double JupiterRise = rs.riseTime(SolarSim.JUPITER, now);
        double JupiterSet = rs.setTime(SolarSim.JUPITER, now);
        DateFormat df = DateFormat.getInstance();

        TimeZone utc = TimeZone.getTimeZone("UTC");
        TimeZone tz = TimeZone.getTimeZone("America/New_York");
        df.setTimeZone(tz);
        
        Calendar jscal = Calendar.getInstance(utc);
        jscal.setTimeInMillis(TimeUtil.JD2mils(JupiterSet));
        Calendar jrcal = Calendar.getInstance(utc);
        jrcal.setTimeInMillis(TimeUtil.JD2mils(JupiterRise));
        
        
        TextView jrise = (TextView) findViewById(R.id.jupiter_rise);
        jrise.setText("Jupiter Rise: " + df.format(jrcal.getTime()));

        TextView jset = (TextView) findViewById(R.id.jupiter_set);
        jset.setText("Jupiter Set: " + df.format(jscal.getTime()));

        // Sun now
        double SunRise = rs.riseTime(SolarSim.SUN, now);
        double SunSet = rs.setTime(SolarSim.SUN, now);
        Calendar sscal = Calendar.getInstance(utc);
        sscal.setTimeInMillis(TimeUtil.JD2mils(SunSet));
        Calendar srcal = Calendar.getInstance(utc);
        srcal.setTimeInMillis(TimeUtil.JD2mils(SunRise));        
        
        TextView srise = (TextView) findViewById(R.id.sun_rise);
        srise.setText("Sun Rise: " + df.format(srcal.getTime()));

        TextView sset = (TextView) findViewById(R.id.sun_set);
        sset.setText("Sun Set: " + df.format(sscal.getTime()));
        
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
