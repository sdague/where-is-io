package net.dague.astro;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import net.dague.astro.sim.JupiterSim;
import net.dague.astro.sim.RiseCalculator;
import net.dague.astro.sim.SolarSim;
import net.dague.astro.util.TimeUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class RiseSetTimes extends Activity implements OnClickListener {

	private void fillRiseTime(int id, int body, double now)
	{
		double rise = rs.riseTime(body, now);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(TimeUtil.JD2mils(rise));
        
        TextView view = (TextView) findViewById(id);
        view.setText("" + df.format(cal.getTime()));
	}
	
	private void fillSetTime(int id, int body, double now)
	{
		double set = rs.setTime(body, now);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(TimeUtil.JD2mils(set));

        TextView view = (TextView) findViewById(id);
        view.setText("" + df.format(cal.getTime()));
	}

	SimpleDateFormat df;
	RiseCalculator rs;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riseset);
        
        df = new SimpleDateFormat("H:mm");
        
        
        double gps[] = getGPS();
        
        rs = new RiseCalculator(gps[0], gps[1]);
        
        double now = TimeUtil.JDfloor(TimeUtil.mils2JD(System.currentTimeMillis()));

        // The Sun
        fillRiseTime(R.id.sun_rise, SolarSim.SUN, now);
        fillSetTime(R.id.sun_set, SolarSim.SUN, now);
        
        fillRiseTime(R.id.mercury_rise, SolarSim.MERCURY, now);
        fillSetTime(R.id.mercury_set, SolarSim.MERCURY, now);
        
        fillRiseTime(R.id.venus_rise, SolarSim.VENUS, now);
        fillSetTime(R.id.venus_set, SolarSim.VENUS, now);
        
        fillRiseTime(R.id.mars_rise, SolarSim.MARS, now);
        fillSetTime(R.id.mars_set, SolarSim.MARS, now);

        fillRiseTime(R.id.jupiter_rise, SolarSim.JUPITER, now);
        fillSetTime(R.id.jupiter_set, SolarSim.JUPITER, now);
        
        fillRiseTime(R.id.saturn_rise, SolarSim.SATURN, now);
        fillSetTime(R.id.saturn_set, SolarSim.SATURN, now);
        
        fillRiseTime(R.id.uranus_rise, SolarSim.URANUS, now);
        fillSetTime(R.id.uranus_set, SolarSim.URANUS, now);

        fillRiseTime(R.id.neptune_rise, SolarSim.NEPTUNE, now);
        fillSetTime(R.id.neptune_set, SolarSim.NEPTUNE, now);

        
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

    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			startActivity(new Intent(this, About.class));
		    return true;

        case R.id.spiral:
		    startActivity(new Intent(this, JovianSpiral.class));
			this.finish();
		    return true;
		    
		case R.id.riseset:
			startActivity(new Intent(this, RiseSetTimes.class));
			this.finish();
			return true;
			
		case R.id.quit:
			this.finish();
		}
		
		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		   super.onCreateOptionsMenu(menu);
		   MenuInflater inflater = getMenuInflater();
		   inflater.inflate(R.menu.menu, menu);
		   return true;
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
