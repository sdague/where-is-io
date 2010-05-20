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

import net.dague.astro.util.JovianMoons;
import net.dague.astro.util.SolarSim;
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

public class SimData extends SQLiteOpenHelper {
	
	private static String[] FROM = { _ID, TIME, CALLISTO, EUROPA, GANYMEDE, IO };
	private static String ORDER_BY = TIME + " DESC" ;
	private static String WHERE_TIME = TIME + " = ?";
	private static String WHERE_RANGE = TIME + " >= ? and " + TIME + " <= ?";
	private static String WHERE_DELETE = TIME + " < ?";
	
	private static final String DATABASE_NAME = "astro.db";
	private static final int DATABASE_VERSION = 1;

	public SimData(Context context) {
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
			j.jd = SolarSim.JD(dbtime);
			j.callisto = cursor.getDouble(2);
			j.europa = cursor.getDouble(3);
			j.ganymede = cursor.getDouble(4);
			j.io = cursor.getDouble(5);
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
			j.jd = SolarSim.JD(dbtime);
			j.callisto = cursor.getDouble(2);
			j.europa = cursor.getDouble(3);
			j.ganymede = cursor.getDouble(4);
			j.io = cursor.getDouble(5);
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
		values.put(IO, jm.io);
		values.put(CALLISTO, jm.callisto);
		values.put(EUROPA, jm.europa);
		values.put(GANYMEDE, jm.ganymede);
		
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
		CALLISTO + " DOUBLE NOT NULL, " +
		EUROPA + " DOUBLE NOT NULL, " +
		GANYMEDE + " DOUBLE NOT NULL, " +
		IO + " DOUBLE NOT NULL);";
		
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
