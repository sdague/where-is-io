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

import net.dague.astro.sim.SolarSim;
import net.dague.astro.util.TimeUtil;

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
		jd = TimeUtil.mils2JD(System.currentTimeMillis());
		callisto = 0;
		io = 0;
		ganymede = 0;
		europa = 0;
	}
	
	public JovianMoons(double t)
	{
		jd = t;
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
	
	// x(n) = x(1) + y(n) * (deltax / deltay)
	
	public JovianMoons interpolate(JovianMoons next, long time)
	{
		double newjd = TimeUtil.mils2JD(time);
		JovianMoons newjm = new JovianMoons(newjd);
		if (this.jd < newjd && next.jd > newjd) {
			double deltay = next.jd - jd;
			double incy = newjd - jd;
			newjm.callisto = callisto + incy * (next.callisto - callisto) / deltay;
			newjm.io = io + incy * (next.io - io) / deltay;
			newjm.ganymede = ganymede + incy * (next.ganymede - ganymede) / deltay;
			newjm.europa = europa + incy * (next.europa - europa) / deltay;			
		}
		return newjm;
	}
	
	public String toString()
	{
		return "jd: " + jd + " - c: " + callisto + ", i: " + io + ", g: " + ganymede + ", e: " + europa;
	}
}
