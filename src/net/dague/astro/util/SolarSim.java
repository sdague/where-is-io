package net.dague.astro.util;

public class SolarSim {
	native double[] returnJD(double jd);
	native double[] earthCoords(double jd);
	native double[] jupiterCoords(double jd);
	native double[] ioCoords(double jd);
	native double[] europaCoords(double jd);
	native double[] ganymedeCoords(double jd);
	native double[] callistoCoords(double jd);
	
	public SolarSim()
	{
		
	}
	
	private double moonPos(Vector3 moon, double jd)
	{
		Vector3 earth = new Vector3(earthCoords(jd));
		Vector3 jupiter = new Vector3(jupiterCoords(jd));
		
		// this is the moon vector from the sun
		Vector3 moon_abs = jupiter.add(moon);
		
		// Avector is earth -> moon (no longer used)
		// Cvector is earth -> jupiter
		// these names come from the geometry diagram I made to work this out
		
		Vector3 Cvector = jupiter.sub(earth);
		
		// This is the vector that represents the solar plane.  It would more
		// accurately be earth x jupiter, however funny things happen when they
		// pass each other
		Vector3 zplane = new Vector3(0.0, 0.0, 1.0);
		
		// We now need a vector which represents the leading edge of jupiter.
		// This can be computed with the zplane x Cvector.  We'll be projecting the
		// moon position on this
		Vector3 projection = zplane.cross(Cvector);
		
		// Lastly we get a double by the dot product of the moon vector in 3 space to
		// projection vector.
		double seperation = projection.dot(moon);
		return seperation;
	}
	
	public JovianMoons getMoons(long time)
	{
		return getMoons(JD(time));		
	}
	
	public JovianMoons getMoons(double jd)
	{
		JovianMoons jv = new JovianMoons();
		jv.jd = jd;

		Vector3 callisto = new Vector3(callistoCoords(jd));
		jv.callisto = moonPos(callisto, jd);

		Vector3 io = new Vector3(ioCoords(jd));
		jv.io = moonPos(io, jd);

		Vector3 europa = new Vector3(europaCoords(jd));
		jv.europa = moonPos(europa, jd);

		Vector3 ganymede = new Vector3(ganymedeCoords(jd));
		jv.ganymede = moonPos(ganymede, jd);
		
		return jv;
	}
	
	// Basic conversion to julian date
	public static double JD(long mils)
	{
		return ((mils / (86400.0 * 1000.0) ) + 2440587.5);
	}
	
	static {
		System.loadLibrary("solarsym");
	}
}
