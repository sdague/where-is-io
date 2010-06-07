package net.dague.astro.util;

public class RiseCalculator {
	
	double lat;
	double lon;
	
	public RiseCalculator(double lat, double lon)
	{
		this.lat = lat;
		this.lon = lon;
	}
	
	private double transit(SkyCoords s, double jd)
	{
		jd = TimeUtil.JDfloor(jd);
		double sdt = TimeUtil.JD2sideral(jd);
		
		double m = (s.ra + lon - sdt) / 360.0;
		
		while (m < 0) { m += 1.0; }
		while (m > 1) { m -= 1.0; }
		
		return m;
	}

	private double riseCalc(SkyCoords s, double h, double jd)
	{
		double Numerator = Math.sin(h) - Math.sin( Convert.deg2rad(lat) ) * Math.sin( Convert.deg2rad(s.decl) );
		double Denominator = Math.cos( Convert.deg2rad(lat) ) * Math.cos( Convert.deg2rad(s.decl) );
		double H = Math.acos( Numerator / Denominator );
		
		double m = transit(s, jd) - H / 360.0;
		
		while (m < 0) { m += 1.0; }
		while (m > 1) { m -= 1.0; }
		
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
		double Numerator = Math.sin(h) - Math.sin( Convert.deg2rad(lat) ) * Math.sin( Convert.deg2rad(s.decl) );
		double Denominator = Math.cos( Convert.deg2rad(lat) ) * Math.cos( Convert.deg2rad(s.decl) );
		double H = Math.acos( Numerator / Denominator );
		
		double m = transit(s, jd) + H / 360.0;
		
		while (m < 0) { m += 1.0; }
		while (m > 1) { m -= 1.0; }
		
		return m;
	}
	
	public double sunSetTime(SkyCoords s, double jd)
	{
		return setCalc(s, -0.5667, jd);
	}
	
	public double setTime(SkyCoords s, double jd)
	{
		return setCalc(s, -0.8333, jd);
	}
	
}
