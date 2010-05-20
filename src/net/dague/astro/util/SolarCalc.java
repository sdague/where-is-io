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

import android.view.View;

public class SolarCalc {
	public SolarSim s;
	public long start;
	public long endhours;
	public View view;
	
	public SolarCalc(SolarSim s, long start, long endhours, View view) {
		this.s = s;
		this.start = start;
		this.endhours = endhours;
		this.view = view;
	}
}
