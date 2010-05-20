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

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import net.dague.astro.data.SimData;
import static android.provider.BaseColumns._ID;
import static net.dague.astro.data.Constants.*;

public class SolarSim {
	
	private final long timestepMinutes = 60;
	private final long timestepMils = timestepMinutes * 60 * 1000;

	// native double[] returnJD(double jd);
	native double[] earthCoords(double jd);
	native double[] jupiterCoords(double jd);
	native double[] ioCoords(double jd);
	native double[] europaCoords(double jd);
	native double[] ganymedeCoords(double jd);
	native double[] callistoCoords(double jd);
	
	SimData coords;
	Context ctx;
	
	public SolarSim(Context context)
	{
		ctx = context;
		coords = new SimData(this.ctx);
	}
	
	private double moonPos(Vector3 moon, Vector3 earth, Vector3 jupiter)
	{
		// Vector3 earth = lookup("earth", jd);
		// Vector3 jupiter = lookup("jupiter", jd);
		
		// this is the moon vector from the sun
		// Vector3 moon_abs = jupiter.add(moon);
		
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
		Vector3 projection = Cvector.cross(zplane);
		
		// We need to normalize the projection, as it's value is going to change over time
		// and we don't want earth -> jupiter distance affecting things
		Vector3 unitproj = projection.unitv();
		
		// Lastly we get a double by the dot product of the moon vector in 3 space to
		// projection vector.
		double seperation = unitproj.dot(moon);
		return seperation;
	}
	
	public JovianPoints getMoonPoints(long time, long hours, boolean nonblock)
	{	
		long now = TimeUtil.round2minutes(time, timestepMinutes);
		int i;
		int size = 0;
		long end = time + TimeUtil.hours2mils(hours);
		
		JovianPoints jp = new JovianPoints(JD(now), JD(end));
		
		HashMap<Long, JovianMoons> dbmoons = coords.getRange(now, end);
		
		for (i = 0; now < end; now += timestepMils, i++)
		{
			Long n = new Long(now);
			if (dbmoons.containsKey(n)) {
				jp.add(dbmoons.get(n));
				size++;
			} else {
				if(!nonblock) {
					JovianMoons j = getMoons(now);
					coords.addCoords(now, j);
					jp.add(j);
				}
			}
		}
		
		jp.percent = size * 100 / i;
		
		return jp;
	}
	
	public JovianMoons getMoonsAt(long time)
	{
		long now = TimeUtil.round2minutes(time, timestepMinutes);
		return coords.get(now);
	}
	
	public Vector3 lookup(String body, long time)
	{		
		double jd = JD(time);
		
		Log.i("DB", "Calculating new data for " + body + " at: " + jd);
		Vector3 newv;
		if (body.equals("earth")) {
			newv = new Vector3(earthCoords(jd));
		} else if(body.equals("jupiter")) {
			newv = new Vector3(jupiterCoords(jd));
		} else if(body.equals("io")) {
			newv = new Vector3(ioCoords(jd));
		} else if(body.equals("callisto")) {
			newv = new Vector3(callistoCoords(jd));
		} else if(body.equals("ganymede")) {
			newv = new Vector3(ganymedeCoords(jd));
		} else if(body.equals("europa")) {
			newv = new Vector3(europaCoords(jd));
		} else {
			newv = new Vector3();
		}
		
		return newv;		
	}
	
	public JovianMoons getMoons(long time)
	{
		
		JovianMoons jv = new JovianMoons();
		jv.jd = JD(time);

		Vector3 earth = lookup("earth", time);
		Vector3 jupiter = lookup("jupiter", time);
		
		Vector3 callisto = lookup("callisto", time);
		jv.callisto = moonPos(callisto, earth, jupiter);

		Vector3 io = lookup("io", time);
		jv.io = moonPos(io, earth, jupiter);

		Vector3 europa = lookup("europa", time);
		jv.europa = moonPos(europa, earth, jupiter);

		Vector3 ganymede = lookup("ganymede", time);
		jv.ganymede = moonPos(ganymede, earth, jupiter);
		
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
