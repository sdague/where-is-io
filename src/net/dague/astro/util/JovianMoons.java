package net.dague.astro.util;

public class JovianMoons {
	public static final int CALLISTO = 0;
	public static final int EUROPA = 1;
	public static final int GANYMEDE = 2;
	public static final int IO = 3;
	
	public double jd;
	public double callisto;
	public double io;
	public double ganymede;
	public double europa;
	
	public JovianMoons()
	{
		jd = SolarSim.JD(System.currentTimeMillis());
		callisto = 0;
		io = 0;
		ganymede = 0;
		europa = 0;
	}
	
	public double get(int i)
	{
		switch(i) {
		case IO:
			return io;
		case EUROPA:
			return europa;
		case CALLISTO:
			return callisto;
		case GANYMEDE:
			return ganymede;
		default:
			return 0;
		}
	}
	
	public void set(int i, double v)
	{
		switch(i) {
		case IO:
			io = v;
			break;
		case EUROPA:
			europa = v;
			break;
		case CALLISTO:
			callisto = v;
			break;
		case GANYMEDE:
			ganymede = v;
			break;
		}
	}
	
	public String toString()
	{
		return "jd: " + jd + " - c: " + callisto + ", i: " + io + ", g: " + ganymede + ", e: " + europa;
	}
}
