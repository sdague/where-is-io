package net.dague.astro.sim;

import android.util.Log;
import net.dague.astro.jupiter.JovianMoons;
import net.dague.astro.util.SkyCoords;
import net.dague.astro.util.TimeUtil;
import net.dague.astro.util.Vector3;

public class JupiterSim {
	private SolarSim sim;
	
	public JupiterSim() {
		sim = new SolarSim();
	}
	
	public double riseTime(double lat, double lon, double jd)
	{
		SkyCoords jupiter = new SkyCoords(sim.calcPosition(SolarSim.EARTH, jd), sim.calcPosition(SolarSim.JUPITER, jd));
		Log.i("IO", jupiter.toString());
		RiseCalculator rc = new RiseCalculator(lat, lon);
		return rc.riseTime(jupiter, jd);
	}
	
	public double setTime(double lat, double lon, double jd)
	{
		SkyCoords jupiter = new SkyCoords(sim.calcPosition(SolarSim.EARTH, jd), sim.calcPosition(SolarSim.JUPITER, jd));
		Log.i("IO", jupiter.toString());
		RiseCalculator rc = new RiseCalculator(lat, lon);
		return rc.setTime(jupiter, jd);
	}
	
	public JovianMoons calcMoons(long time)
	{
		double jd = TimeUtil.mils2JD(time);
		JovianMoons jv = new JovianMoons(jd);

		Vector3 earth = sim.calcPosition(SolarSim.EARTH, jd);
		Vector3 jupiter = sim.calcPosition(SolarSim.JUPITER, jd);
		
		Vector3 callisto = sim.calcPosition(SolarSim.CALLISTO, jd);
		jv.callisto = earthProjection(callisto, earth, jupiter);

		Vector3 io = sim.calcPosition(SolarSim.IO, jd);
		jv.io = earthProjection(io, earth, jupiter);

		Vector3 europa = sim.calcPosition(SolarSim.EUROPA, jd);
		jv.europa = earthProjection(europa, earth, jupiter);

		Vector3 ganymede = sim.calcPosition(SolarSim.GANYMEDE, jd);
		jv.ganymede = earthProjection(ganymede, earth, jupiter);
		
		return jv;
	}

	
	private Vector3 earthProjection(Vector3 moon, Vector3 earth, Vector3 jupiter)
	{
		Vector3 ep = new Vector3();
		
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
		Vector3 projection = Cvector.cross(zplane);
		
		// We need to normalize the projection, as it's value is going to change over time
		// and we don't want earth -> jupiter distance affecting things
		Vector3 xproj = projection.unitv();
		
		// Lastly we get a double by the dot product of the moon vector in 3 space to
		// projection vector.
		ep.X = xproj.dot(moon);

		// The y projection is actually the dot product in the zplane
		ep.Y = zplane.dot(moon);
		
		// The z projection (away from earth) is the projection of the moon vector on
		// the unitvector of earth.
		ep.Z = earth.unitv().dot(moon);
		
		return ep;
	}
}
