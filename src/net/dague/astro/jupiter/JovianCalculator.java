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

package net.dague.astro.jupiter;

import java.util.HashMap;
import java.util.List;

import net.dague.astro.data.JupiterData;
import net.dague.astro.sim.SolarSim;
import net.dague.astro.util.TimeUtil;
import net.dague.astro.util.Vector3;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

public class JovianCalculator extends Thread {

	public boolean running = false;

	long start;
	long endHours;
	JovianSpiralView view;
	
	SolarSim sim;
	Context context;
	JupiterData data;
	HashMap<Long, JovianMoons> cache;
	
	long START_OFFSET;
	long END_HOURS;
	long TIMESTEP;
	
	public JovianCalculator(Context ctx)
	{
		context = ctx;
		sim = new SolarSim();
		data = new JupiterData(ctx);
		
		
		START_OFFSET = JovianSpiralView.START_HOURS;
		END_HOURS = JovianSpiralView.END_HOURS;
		TIMESTEP = JovianSpiralView.TIMESTEP_MILS;
		cache = data.getRange(startTime(), endTime());
	}
	
	private long endTime()
	{
		return round(System.currentTimeMillis() + TimeUtil.hours2mils(END_HOURS));
	}
	
	private long startTime()
	{
		return round(System.currentTimeMillis() - TimeUtil.hours2mils(START_OFFSET));
	}
	
	private long round(long time) {
		return time - (time % TIMESTEP);
	}
	
	
	public JovianMoonSet getMoonPoints(long time, long hours)
	{
		long when = round(time);
		long stop = when + TimeUtil.hours2mils(hours);
		
		JovianMoonSet set = new JovianMoonSet(when, stop);
		
		int total = 0;
		int found = 0;
		synchronized(cache) {
			// Log.i("IO", "Cache dump: " + cache.toString());
			for (; when < stop; when += TIMESTEP) {
				Long look = new Long(when);
				if (cache.containsKey(look)) {
					// Log.i("IO", "found data for " + look);
					set.add(cache.get(look));
					found++;
				}
				total++;
			}
			
		}
		
		set.percent = (found * 100) / total;
		
		return set;
	}
	
	public JovianMoons getMoonsAt(long time) {
		Long when = new Long(round(time));
		synchronized(cache) {
			if (cache.containsKey(when)) {
				return cache.get(when);
			} else {
				return new JovianMoons();
			}
		}
	}
	
	private double spiralProjection(Vector3 moon, Vector3 earth, Vector3 jupiter)
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
	

	private JovianMoons calcMoons(long time)
	{
		double jd = TimeUtil.mils2JD(time);
		JovianMoons jv = new JovianMoons(jd);

		Vector3 earth = sim.calcPosition(SolarSim.EARTH, jd);
		Vector3 jupiter = sim.calcPosition(SolarSim.JUPITER, jd);
		
		Vector3 callisto = sim.calcPosition(SolarSim.CALLISTO, jd);
		jv.callisto = spiralProjection(callisto, earth, jupiter);

		Vector3 io = sim.calcPosition(SolarSim.IO, jd);
		jv.io = spiralProjection(io, earth, jupiter);

		Vector3 europa = sim.calcPosition(SolarSim.EUROPA, jd);
		jv.europa = spiralProjection(europa, earth, jupiter);

		Vector3 ganymede = sim.calcPosition(SolarSim.GANYMEDE, jd);
		jv.ganymede = spiralProjection(ganymede, earth, jupiter);
		
		return jv;
	}
	
    private void calcData() {
    	long start = startTime();
    	long end = endTime();
    	for (long time = start; time < end; time += TIMESTEP) {
    		synchronized(cache) {
    			if (!cache.containsKey(new Long(time))) {
    				Log.i("IO","Calculate moons for " + time);
    				JovianMoons jm = calcMoons(time);
    				data.addCoords(time, jm);
    				cache.put(new Long(time), jm);
    				// if we got new data, send a signal
    				try {
    					notifyAll();
    				} catch (IllegalMonitorStateException e) {
    					// don't care
    				}
    			}
    		}
    	}
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(running) {
			calcData();
			try {
				sleep(TIMESTEP);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.i("IO", "calc thread woke up unexpectedly");
			}
		}
	}

	public void setRunning(boolean b) {
		// TODO Auto-generated method stub
		running = b;
	}
}
