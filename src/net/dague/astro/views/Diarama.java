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
	
	protected void paintRise(Canvas c, int body) {
		
		double now = TimeUtil.JDfloor(TimeUtil.mils2JD(System.currentTimeMillis()));
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
		

		paintBody(c, SolarSim.NEPTUNE, R.drawable.neptune_icon, p);
		paintBody(c, SolarSim.URANUS, R.drawable.uranus_icon, p);
		paintBody(c, SolarSim.SATURN, R.drawable.saturn_icon, p);
		paintBody(c, SolarSim.JUPITER, R.drawable.jupiter_icon, p);
		paintBody(c, SolarSim.MARS, R.drawable.mars_icon, p);
		paintBody(c, SolarSim.VENUS, R.drawable.venus_icon, p);
		paintBody(c, SolarSim.MERCURY, R.drawable.mercury_icon, p);
		paintBody(c, SolarSim.SUN, R.drawable.sun_icon, p);
		
		BitmapDrawable d = (BitmapDrawable)(getContext().getResources().getDrawable(R.drawable.sun_icon));
		Bitmap sunBitmap = d.getBitmap();
		
		Rect src = new Rect(0, 0, sunBitmap.getWidth(), sunBitmap.getHeight());
		

		// c.drawCircle(w/2, h/2, h/4, p);
		// c.drawBitmap(sunBitmap, src, dst, p);
		c.drawArc(r, 180, 180, false, p);
		

	}

	private void paintBody(Canvas c, int body, int icon, Paint p) {
		double realnow = TimeUtil.mils2JD(System.currentTimeMillis());
		double now = TimeUtil.JDfloor(realnow);
		double stage = 0.0;

		int h = this.getHeight();
		int w = this.getWidth();
		
		h -= h/5; 
		
		double rise = rs.riseTime(body, now);
		double set = rs.setTime(body, now);
		if (rise > set) {
			rise -= 1.0;
		}
		
		if(realnow > rise && realnow < set) {
			stage = (realnow - rise) / (set - rise);
		} else {
			return;
		}
		
		Log.i("DIARAMA", "Body:" + body + " - Stage: " + stage + ", R: " + rise + ", S: " + set);

		Log.i("DIARAMA", "h:" + h + ", w:" + w);

		BitmapDrawable d = (BitmapDrawable)(getContext().getResources().getDrawable(icon));
		Bitmap bodyBitmap = d.getBitmap();
		
		// Now we need to compute where things actually are on the diarama
		
		Rect src = new Rect(0, 0, bodyBitmap.getWidth(), bodyBitmap.getHeight());
		int radius = bodyBitmap.getWidth() / 2;
		
		double radians = Math.PI - (stage * Math.PI);
		Log.i("DIARAMA", "Radians: " + radians);
		
		// This looks really complicated, and it sort of is.  There was a lot of manual
		// tuning with stage = 0.0, 0.5, and 1.0 to make things land in sane places.
		
		double x = Math.cos(radians) * (w-radius)/2 + (w-(radius*2))/2 + radius;
		double y = h - Math.sin(radians) * h + radius;
		Log.i("DIARMA", "Sin: " + Math.sin(radians));

		RectF dst = new RectF((float)(x-radius),(float)(y-radius),(float)(x+radius),(float)(y+radius));
		c.drawBitmap(bodyBitmap, src, dst, p);
		
		// TODO Auto-generated method stub
		
	}

}
