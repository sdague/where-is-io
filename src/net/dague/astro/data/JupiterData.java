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

import java.util.HashMap;
import java.util.Vector;

import net.dague.astro.jupiter.JovianMoons;
import net.dague.astro.sim.SolarSim;
import net.dague.astro.util.TimeUtil;
import net.dague.astro.util.Vector3;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import static android.provider.BaseColumns._ID;
import static net.dague.astro.data.Constants.*;

/*
 *  JupiterData is a base database caching class for the information that we
 *  calculate around jupiter moon positions.  The reason for this is that the
 *  numerical simulation takes about 20 seconds for the 480 points we need on 
 *  and HTC Hero.  So after we calculate it we keep it on disk.
 *  
 *  What we are keeping:
 *    - time in milliseconds of the row.  Storing / selecting by jd 
 *      doesn't work because it's a double and there are rounding errors
 *    - 3 cols each for each of the moons, x, y, and z.  
 *    	- X is the projection along plane of the solar system.  
 *    		Close enough to jupiter equitorial plane.
 *      - Y is the projection up from the plane, only useful in 2D plotting
 *      - Z is the projection towards / away from the earth.  Used to get the
 *          spiral sequence right.
 */

public class JupiterData extends SQLiteOpenHelper {
	
	private static String[] FROM = { _ID, TIME, CALLISTO_X, CALLISTO_Y, CALLISTO_Z,
									EUROPA_X, EUROPA_Y, EUROPA_Z,
									GANYMEDE_X, GANYMEDE_Y, GANYMEDE_Z,
									IO_X, IO_Y, IO_Z};
	private static String ORDER_BY = TIME + " DESC" ;
	private static String WHERE_TIME = TIME + " = ?";
	private static String WHERE_RANGE = TIME + " >= ? and " + TIME + " <= ?";
	private static String WHERE_DELETE = TIME + " < ?";
	
	private static final String DATABASE_NAME = "astro.db";
	private static final int DATABASE_VERSION = 2;

	public JupiterData(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		// TODO Auto-generated constructor stub
	}

	public JovianMoons get(long time) 
	{
		JovianMoons j = new JovianMoons();
		SQLiteDatabase db = getReadableDatabase();
		String[] wherebits = {Long.toString(time)};
		Cursor cursor = db.query(TABLE_NAME, FROM, WHERE_TIME, wherebits, null, null, ORDER_BY);

		while(cursor.moveToNext()) {
			long dbtime = cursor.getLong(1);
			j.jd = TimeUtil.mils2JD(dbtime);
			j.callisto.X = cursor.getDouble(2);
			j.callisto.Y = cursor.getDouble(3);
			j.callisto.Z = cursor.getDouble(4);
			j.europa.X = cursor.getDouble(5);
			j.europa.Y = cursor.getDouble(6);
			j.europa.Z = cursor.getDouble(7);
			j.ganymede.X = cursor.getDouble(8);
			j.ganymede.Y = cursor.getDouble(9);
			j.ganymede.Z = cursor.getDouble(10);
			j.io.X = cursor.getDouble(11);
			j.io.Y = cursor.getDouble(12);
			j.io.Z = cursor.getDouble(13);
		}
		cursor.close();
		db.close();
		return j;
	}
	
	public HashMap<Long, JovianMoons> getRange(long time, long until)
	{
		HashMap<Long, JovianMoons> jm = new HashMap<Long, JovianMoons>();
		// Log.i("IO", "Getting data for body: " + body + " at jd: " + jd);
		
		SQLiteDatabase db = getReadableDatabase();
		String[] wherebits = {Long.toString(time), Long.toString(until)};
		Cursor cursor = db.query(TABLE_NAME, FROM, WHERE_RANGE, wherebits, null, null, ORDER_BY);
		
		while(cursor.moveToNext()) {
			JovianMoons j = new JovianMoons();
			long dbtime = cursor.getLong(1);
			j.jd = TimeUtil.mils2JD(dbtime);
			j.callisto.X = cursor.getDouble(2);
			j.callisto.Y = cursor.getDouble(3);
			j.callisto.Z = cursor.getDouble(4);
			j.europa.X = cursor.getDouble(5);
			j.europa.Y = cursor.getDouble(6);
			j.europa.Z = cursor.getDouble(7);
			j.ganymede.X = cursor.getDouble(8);
			j.ganymede.Y = cursor.getDouble(9);
			j.ganymede.Z = cursor.getDouble(10);
			j.io.X = cursor.getDouble(11);
			j.io.Y = cursor.getDouble(12);
			j.io.Z = cursor.getDouble(13);
			jm.put(new Long(dbtime), j);
		}
		cursor.close();
		db.close();
		return jm;
	}
	
	public void purgeRecords(long time) {
		SQLiteDatabase db = getWritableDatabase();
		String [] where = {Long.toString(time)};
		db.delete(TABLE_NAME, WHERE_DELETE, where);
		db.close();
	}
	
	public void deleteAll() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}
	
	public void addCoords(long time, JovianMoons jm) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TIME, time);
		values.put(IO_X, jm.io.X);
		values.put(IO_Y, jm.io.Y);
		values.put(IO_Z, jm.io.Z);
		values.put(CALLISTO_X, jm.callisto.X);
		values.put(CALLISTO_Y, jm.callisto.Y);
		values.put(CALLISTO_Z, jm.callisto.Z);
		values.put(EUROPA_X, jm.europa.X);
		values.put(EUROPA_Y, jm.europa.Y);
		values.put(EUROPA_Z, jm.europa.Z);
		values.put(GANYMEDE_X, jm.ganymede.X);
		values.put(GANYMEDE_Y, jm.ganymede.Y);
		values.put(GANYMEDE_Z, jm.ganymede.Z);
		
		try {
			db.insertOrThrow(TABLE_NAME, null, values);
		} catch (SQLException e) {
			// This is probably a key collision exception, just ignore it
		}
		db.close();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String create1 = "CREATE TABLE " + TABLE_NAME +
		"(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		TIME + " LONG NOT NULL, " +
		CALLISTO_X + " DOUBLE NOT NULL, " +
		CALLISTO_Y + " DOUBLE NOT NULL, " +
		CALLISTO_Z + " DOUBLE NOT NULL, " +
		EUROPA_X + " DOUBLE NOT NULL, " +
		EUROPA_Y + " DOUBLE NOT NULL, " +
		EUROPA_Z + " DOUBLE NOT NULL, " +
		GANYMEDE_X + " DOUBLE NOT NULL, " +
		GANYMEDE_Y + " DOUBLE NOT NULL, " +
		GANYMEDE_Z + " DOUBLE NOT NULL, " +
		IO_X + " DOUBLE NOT NULL, " +
		IO_Y + " DOUBLE NOT NULL, " +
		IO_Z + " DOUBLE NOT NULL);";
		
		String index1 = "create index " + TABLE_NAME + "_" + TIME + 
			" on " + TABLE_NAME + "(" + TIME + ")";

		db.execSQL(create1);
		db.execSQL(index1);
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
