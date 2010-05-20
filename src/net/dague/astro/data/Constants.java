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

package net.dague.astro.data;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
	public static final String TABLE_NAME = "jovian_moons" ;
	// Columns in the Events database
	public static final String TIME = "time" ;
	public static final String EUROPA = "europa";
	public static final String IO = "io";
	public static final String GANYMEDE = "ganymede";
	public static final String CALLISTO = "callisto";
	public static final String BODY = "body" ;
	public static final String X = "x";
	public static final String Y = "y";
	public static final String Z = "z";

}
