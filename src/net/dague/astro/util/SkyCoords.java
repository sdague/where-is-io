package net.dague.astro.util;

public class SkyCoords {
	double ra;
	double decl;
	
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
		ra = Math.atan2(v.Y, v.X);
		decl = Math.asin(v.Z / v.mag());
	}
}
