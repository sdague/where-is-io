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

package net.dague.astro.sim;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import net.dague.astro.data.JupiterData;
import net.dague.astro.jupiter.JovianMoonSet;
import net.dague.astro.jupiter.JovianMoons;
import net.dague.astro.util.TimeUtil;
import net.dague.astro.util.Vector3;
import static android.provider.BaseColumns._ID;
import static net.dague.astro.data.Constants.*;

public class SolarSim {
	
	public static final int SUN = 0;
	public static final int MERCURY = 1;
	public static final int VENUS = 2;
	public static final int EARTH = 3;
	public static final int MARS = 4;
	public static final int JUPITER = 5;
	public static final int IO = 51;
	public static final int EUROPA = 52;
	public static final int GANYMEDE = 53;
	public static final int CALLISTO = 54;
	public static final int SATURN = 6;
	public static final int URANUS = 7;
	public static final int NEPTUNE = 8;

	// native double[] returnJD(double jd);
	native double[] mercuryCoords(double jd);
	native double[] venusCoords(double jd);
	native double[] earthCoords(double jd);
	native double[] marsCoords(double jd);
	native double[] jupiterCoords(double jd);
	native double[] saturnCoords(double jd);
	native double[] uranusCoords(double jd);
	native double[] neptuneCoords(double jd);
	
	// jupiter's moons
	native double[] ioCoords(double jd);
	native double[] europaCoords(double jd);
	native double[] ganymedeCoords(double jd);
	native double[] callistoCoords(double jd);
	
	// saturn's moons
	native double[] mimasCoords(double jd);
	native double[] enceladusCoords(double jd);
	native double[] tethysCoords(double jd);
	native double[] dioneCoords(double jd);
	native double[] rheaCoords(double jd);
	native double[] titanCoords(double jd);
	native double[] hyperionCoords(double jd);
	native double[] iapetusCoords(double jd);
	
	public SolarSim() {}
	
	public Vector3 calcPosition(int body, double jd)
	{
		switch(body) { 
		case SUN:
			// the sun is cannonically 0
			return new Vector3();
		case MERCURY:
			return new Vector3(mercuryCoords(jd));
		case VENUS:
			return new Vector3(venusCoords(jd));		
		case EARTH:
			return new Vector3(earthCoords(jd));
		case MARS:
			return new Vector3(marsCoords(jd));		
		case JUPITER:
			return new Vector3(jupiterCoords(jd));
		case SATURN:
			return new Vector3(saturnCoords(jd));		
		case URANUS:
			return new Vector3(uranusCoords(jd));		
		case NEPTUNE:
			return new Vector3(neptuneCoords(jd));
		// Jupiter's moons
		case IO:
			return new Vector3(ioCoords(jd));
		case EUROPA:
			return new Vector3(europaCoords(jd));
		case GANYMEDE:
			return new Vector3(ganymedeCoords(jd));
		case CALLISTO:
			return new Vector3(callistoCoords(jd));
		// Saturn's moons
		default:
			return new Vector3();
		}
	}	
	
	
	
	static {
		System.loadLibrary("solarsym");
	}
}
