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

import android.util.Log;

public class TouchMap {
	private HashMap<Integer, String> map;
	private int width;
	private final int range = 20;

	
	public TouchMap(int width)
	{
		this.width = width;
		map = new HashMap<Integer, String>();
	}
	
	public void addPoint(int x, int y, String value)
	{
		Integer loc = new Integer((x + (y * width)));
		map.put(loc, value);	
	}
	
	public String getPoint(int x, int y)
	{
		Integer loc = new Integer(x + (y * width));
		// Log.i("IO", "looking up " + loc);
		if (map.containsKey(loc)) {
			return map.get(loc);
		}
		
		for (int i = 0; i < range; i++) {
			for (int deltaX = -i; deltaX < i; deltaX++) {
				for (int deltaY = -i; deltaY < i; deltaY++) {
					int newX = x + deltaX;
					int newY = y + deltaY;
					loc = new Integer(newX + (newY * width));
					// Log.i("IO", "looking up " + loc);
					if (map.containsKey(loc)) {
						return map.get(loc);
					}
				}
			}
		}
		return null;
	}
}
