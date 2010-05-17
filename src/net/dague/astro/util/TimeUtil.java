package net.dague.astro.util;

public class TimeUtil {
	public static long hours2mils(long hours)
	{
		return hours * 60 * 60 * 1000;
	}
	
	public static long round2minutes(long raw, long minutes)
	{
		return raw - (raw % (minutes * 60 * 1000));
	}
}
