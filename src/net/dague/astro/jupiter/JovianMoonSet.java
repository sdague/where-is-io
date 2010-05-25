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

import java.util.Vector;


public class JovianMoonSet {
	private Vector<JovianMoons> jp;
	
	// max is actually an AU size
	public final static double max = 0.03;
	public final static double scale = 2.1;
	public int percent = 0;
	
	double starttime = 300000;
	double endtime;
	
	public JovianMoonSet(double start, double end)
	{
		starttime = start;
		endtime = end;
		jp = new Vector<JovianMoons>();
	}
	
	public boolean complete() {
		return percent == 100;
	}
	
	public void add(JovianMoons jm)
	{
		jp.add(jm);
	}
	
	// the screen width in AU
	public static float screenWidth()
	{
		return (float) (max * scale);
	}
	
	private float xPos(double moon, int width)
	{
		float x = (float) ((moon / max) * (width / scale) + (width / 2));
		return x;
	}
	
	private float yPos(double time, int height)
	{
		float y = (float) ((time - starttime) / (endtime - starttime) * (float) height);
		return y;
	}
	
	public float[] getMoonLines(int moon, int width, int height)
	{
		float[] results = new float[jp.size() * 4 - 4];
		int end = jp.size() - 1;
		for (int i = 0; i < end; i++) {
			JovianMoons j = jp.elementAt(i);
			JovianMoons next = jp.elementAt(i + 1);
			float x = xPos(j.get(moon), width);
			float y = yPos(j.jd, height);
			float xnext = xPos(next.get(moon), width);
			float ynext = yPos(next.jd, height);
			results[4*i] = x;
			results[4*i + 1] = y;
			results[4*i + 2] = xnext;
			results[4*i + 3] = ynext;
		}
		return results;
	}
	
	public float[] getMoonPoints(int moon, int width, int height)
	{
		float[] results = new float[jp.size() * 2];
		for (int i = 0; i < jp.size(); i++) {
			JovianMoons j = jp.elementAt(i);
			float x = (float) ((j.get(moon) / max) * (width / scale) + (width / 2));
			float y = i * height / jp.size();
			results[2*i] = x;
			results[2*i + 1] = y;
		}
		
		return results;
	}

	public float[] getIoPoints(int width, int height)
	{
		return getMoonPoints(JovianMoons.IO, width, height);
	}
	
	public float[] getEuropaPoints(int width, int height)
	{
		return getMoonPoints(JovianMoons.EUROPA, width, height);
	}
	
	public float[] getCallistoPoints(int width, int height)
	{
		return getMoonPoints(JovianMoons.CALLISTO, width, height);
	}
	
	public float[] getGanymedePoints(int width, int height)
	{
		return getMoonPoints(JovianMoons.GANYMEDE, width, height);
	}

	
}
