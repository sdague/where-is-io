package net.dague.astro.sim;

import net.dague.astro.util.Convert;
import net.dague.astro.util.SkyCoords;
import net.dague.astro.util.TimeUtil;
import net.dague.astro.util.Vector3;
import android.util.Log;

/**
 * RiseCalculator calculates the rise and set times of planets based on a position on the earth.
 * 
 * @author Sean Dague
 *
 */

public class RiseCalculator {
	
	double lat;
	double lon;
	SolarSim sim;
	
	public RiseCalculator(double lat, double lon)
	{
		this.lat = lat;
		this.lon = lon;
		sim = new SolarSim();
	}
	
	public double riseTime(int body, double jd)
	{
		Vector3 origin = sim.calcPosition(SolarSim.EARTH, jd);
		Vector3 target = sim.calcPosition(body, jd);
		
		SkyCoords targetcoords = new SkyCoords(origin, target);
		
		if (body == SolarSim.SUN) {
			return sunRiseTime(targetcoords, jd);
		} else {
			return riseTime(targetcoords, jd);
		}
		
	}
	
	private double transit(SkyCoords s, double jd)
	{
		jd = TimeUtil.JDfloor(jd);
		double sdt = TimeUtil.JD2sideral(jd);
		
		double m = (Convert.rad2deg(s.ra) + lon - sdt) / 360.0;
		
		Log.i("IO", "Transit: " + m);
		
//		while (m < 0) { m += 1.0; }
//		while (m > 1) { m -= 1.0; }
		
		return m;
	}

	private double riseCalc(SkyCoords s, double h, double jd)
	{
		Log.i("IO", "Lat:" + lat + ", Lon:" + lon);
		double Numerator = Math.sin(h) - Math.sin( Convert.deg2rad(lat) ) * Math.sin( s.decl );
		double Denominator = Math.cos( Convert.deg2rad(lat) ) * Math.cos( s.decl );
		double H = Math.acos( Numerator / Denominator );
		
		double m = transit(s, jd) - Convert.rad2deg(H) / 360.0;
		
//		while (m < 0) { m += 1.0; }
//		while (m > 1) { m -= 1.0; }
		
		return m;
	}
	
	public double sunRiseTime(SkyCoords s, double jd)
	{
		return riseCalc(s, -0.5667, jd);
	}
	
	public double riseTime(SkyCoords s, double jd)
	{
		return riseCalc(s, -0.8333, jd);
	}
	
	private double setCalc(SkyCoords s, double h, double jd)
	{
		double Numerator = Math.sin(h) - Math.sin( Convert.deg2rad(lat) ) * Math.sin( s.decl );
		double Denominator = Math.cos( Convert.deg2rad(lat) ) * Math.cos( s.decl );
		double H = Math.acos( Numerator / Denominator );
		
		double m = transit(s, jd) + Convert.rad2deg(H) / 360.0;
		
//		while (m < 0) { m += 1.0; }
//		while (m > 1) { m -= 1.0; }
		
		return m;
	}
	
	public double sunSetTime(SkyCoords s, double jd)
	{
		return setCalc(s, Convert.deg2rad(-0.5667), jd);
	}
	
	public double setTime(SkyCoords s, double jd)
	{
		return setCalc(s, Convert.deg2rad(-0.8333), jd);
	}
	
}
