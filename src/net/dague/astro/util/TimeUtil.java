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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.util.Log;

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

	public static long JD2mils(double jd, TimeZone tz) {
		long mils = JD2mils(jd);
		return mils + tz.getOffset(mils);
	}

	
	public static double JDfloor(double jd)
	{
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		c.setTimeInMillis(TimeUtil.JD2mils(jd));
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		DateFormat df = DateFormat.getInstance();
		
		Log.i("IO", "JD Floor Calculation: " + df.format(c.getTime()));
		return TimeUtil.mils2JD(c.getTimeInMillis());
	}
	
	public static double JD2sideral(double jd) {
		double T = (jd - 2451545.0) / 36525;
		double sid = 100.46061837 + 36000.770053608 * T + 
			0.000387933 * T * T + T * T * T / 38710000;
		sid = sid % 360.0;
		return sid;
	}
	
}
