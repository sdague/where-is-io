/*
  Copyright 2010 Sean Dague

  This file is part of Where is Io

  Where is Io is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  Where is Io is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with Where is Io.  If not, see <http://www.gnu.org/licenses/>.
 */

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
	

	// Basic conversion to julian date
	public static double mils2JD(long mils)
	{
		return ((mils / (86400.0 * 1000.0) ) + 2440587.5);
	}

	public static long JD2mils(double jd) {
		return (long) ((jd - 2440587.5) * 86400 * 1000);
	}
	
}
