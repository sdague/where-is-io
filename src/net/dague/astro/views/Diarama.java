package net.dague.astro.views;

import java.util.List;

import net.dague.astro.R;
import net.dague.astro.sim.RiseCalculator;
import net.dague.astro.sim.SolarSim;
import net.dague.astro.util.TimeUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Diarama extends View {


	RiseCalculator rs;
	
	public Diarama(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public Diarama(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public Diarama(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	

	private double[] getGPS() {  

		LocationManager lm = (LocationManager) (getContext().getSystemService(Context.LOCATION_SERVICE));    
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
	
	protected void onDraw(Canvas c) {
		
		double gps[] = getGPS();
		rs = new RiseCalculator(gps[0], gps[1]);
		

        double now = TimeUtil.JDfloor(TimeUtil.mils2JD(System.currentTimeMillis()));
		
		double jrise = rs.riseTime(SolarSim.NEPTUNE, now);
		double jset = rs.setTime(SolarSim.NEPTUNE, now);
		
		Log.i("DIARAMA", "N: " + now + ", R: " + jrise + ", S: " + jset);
		
		Paint p = new Paint();
		p.setColor(this.getContext().getResources().getColor(R.color.ground));
		p.setStrokeCap(Paint.Cap.BUTT);
		p.setStrokeWidth(4);
		p.setAntiAlias(true);
		int h = this.getHeight();
		int w = this.getWidth();
		Log.i("DIARAMA", "Height: " + h + ", Width: " + w);
		
		int radius = 24;
		RectF dst = new RectF(w/2-radius,h/2-radius,w/2+radius,h/2+radius);
		
		RectF r = new RectF();
		r.top = h - h/6;
		r.bottom = h + h/3;
		r.left = -w/4;
		r.right = w + w/4;
		
		BitmapDrawable d = (BitmapDrawable)(getContext().getResources().getDrawable(R.drawable.sun_icon));
		Bitmap sunBitmap = d.getBitmap();
		
		Rect src = new Rect(0, 0, sunBitmap.getWidth(), sunBitmap.getHeight());
		

		// c.drawCircle(w/2, h/2, h/4, p);
		c.drawBitmap(sunBitmap, src, dst, p);
		c.drawArc(r, 180, 180, false, p);

	}

}
