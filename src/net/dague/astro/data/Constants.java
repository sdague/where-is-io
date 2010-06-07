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
	public static final String EUROPA_X = "europa_x";
	public static final String IO_X = "io_x";
	public static final String GANYMEDE_X = "ganymede_x";
	public static final String CALLISTO_X = "callisto_x";
	public static final String EUROPA_Y = "europa_y";
	public static final String IO_Y = "io_y";
	public static final String GANYMEDE_Y = "ganymede_y";
	public static final String CALLISTO_Y = "callisto_y";
	public static final String EUROPA_Z = "europa_z";
	public static final String IO_Z = "io_z";
	public static final String GANYMEDE_Z = "ganymede_z";
	public static final String CALLISTO_Z = "callisto_z";
	public static final String BODY = "body" ;
	public static final String X = "x";
	public static final String Y = "y";
	public static final String Z = "z";

}
