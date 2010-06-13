package net.dague.astro.util;

import android.util.Log;

public class SkyCoords {
	public double ra;
	public double decl;
	
	public SkyCoords() {
		ra = 0;
		decl = 0;
	}
	
	public SkyCoords(Vector3 v) {
		ra = Math.atan2(v.Y, v.X);
		decl = Math.asin(v.Z / v.mag());
	}
	
	public SkyCoords(Vector3 origin, Vector3 target) {
		Vector3 v = target.sub(origin);
		// This vector is in the delta vector in the Sun axes, but now we
		// have to convert it to the Earth axes by Spherical transform
		
		// the earth's tilt
		double tilt = Convert.deg2rad(23.4406);
		
		
		Vector3 earthV = new Vector3();

		earthV.X = v.X;
		earthV.Y = v.Y * Math.cos(tilt) - v.Z * Math.sin(tilt);
		earthV.Z = v.Y * Math.sin(tilt) + v.Z * Math.cos(tilt);
		
		Log.i("IO", "Computed vector " + earthV);
		ra = Math.atan2(earthV.Y, earthV.X);
		if(ra < 0) {
			ra += (2 * Math.PI);
		}
		decl = Math.asin(earthV.Z / earthV.mag());
	}
	
	public String fmthrs(double rad)
	{
		int hours = (int)Math.floor(Convert.rad2deg(rad) / 15.0);
		int minutes = (int)Math.floor(Convert.rad2deg(rad) / 15.0 * 60 % 60);
		int seconds = (int)Math.floor(Convert.rad2deg(rad) / 15.0 * 60 * 60 % 60);
		return hours + "h " + minutes + "m " + seconds + "s";
	}
	
	public String fmtdegs(double rad)
	{
		String sign = "";
		if (rad < 0) {
			rad = - rad;
			sign = "-";
		}
		int hours = (int)Math.floor(Convert.rad2deg(rad));
		int minutes = (int)Math.floor(Convert.rad2deg(rad) * 60 % 60);
		int seconds = (int)Math.floor(Convert.rad2deg(rad) * 60 * 60 % 60);
		return sign + hours + " " + minutes + "m " + seconds + "s";
	}
	
	
	public String toString()
	{
		return "RA: " + fmthrs(ra) + ", Decl: " + fmtdegs(decl);
	}
}
