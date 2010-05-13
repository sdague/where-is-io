package net.dague.astro.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import net.dague.astro.data.SimData;
import static android.provider.BaseColumns._ID;
import static net.dague.astro.data.Constants.*;

public class SolarSim {
	private static String[] FROM = { _ID, BODY, TIME, X, Y, Z };
	private static String ORDER_BY = TIME + " DESC" ;
	private static String WHERE = BODY + "= ? and " + TIME + " = ?";

	
	native double[] returnJD(double jd);
	native double[] earthCoords(double jd);
	native double[] jupiterCoords(double jd);
	native double[] ioCoords(double jd);
	native double[] europaCoords(double jd);
	native double[] ganymedeCoords(double jd);
	native double[] callistoCoords(double jd);
	
	SimData coords;
	Context ctx;
	
	public SolarSim(Context context)
	{
		ctx = context;
		coords = new SimData(this.ctx);
	}
	
	private double moonPos(Vector3 moon, Vector3 earth, Vector3 jupiter)
	{
		// Vector3 earth = lookup("earth", jd);
		// Vector3 jupiter = lookup("jupiter", jd);
		
		// this is the moon vector from the sun
		Vector3 moon_abs = jupiter.add(moon);
		
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
	
	public JovianPoints getMoonPoints(long time, long timestep, int num)
	{
		JovianPoints jp = new JovianPoints();
		
		long now = time - (time % (15 * 60 * 1000));
		int i;
		long end = time + (timestep * num);
		for (i = 0; now < end; now += timestep, i++)
		{
			JovianMoons j = getMoons(now);
		    jp.add(j);
		}
		
		return jp;
	}
	
	public JovianMoons[] getMoons(long time, long timestep, long num)
	{
		JovianMoons[] jv = new JovianMoons[(int) num];
		long now;
		int i;
		long end = time + (timestep * num);
		for (i = 0, now = time; now < end; now += timestep, i++)
		{
			jv[i] = getMoons(now);			
		}
		return jv;
	}
	
	private Vector3 getCoords(String body, long time)
	{
		Vector3 v = new Vector3();
		// Log.i("IO", "Getting data for body: " + body + " at jd: " + jd);
		SQLiteDatabase db = coords.getReadableDatabase();
		String[] wherebits = {body, Long.toString(time)};
		Cursor cursor = db.query(TABLE_NAME, FROM, WHERE, wherebits, null, null, ORDER_BY);
		while(cursor.moveToNext()) {
			v.X = cursor.getDouble(3);
			v.Y = cursor.getDouble(4);
			v.Z = cursor.getDouble(5);
			break;
		}
		cursor.close();
		db.close();
		return v;
	}
	
	private void addCoords(String body, long time, Vector3 p) {
		SQLiteDatabase db = coords.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(BODY, body);
		values.put(TIME, time);
		values.put(X, p.X);
		values.put(Y, p.Y);
		values.put(Z, p.Z);
		db.insertOrThrow(TABLE_NAME, null, values);
		db.close();
	}
	
	public Vector3 lookup(String body, long time)
	{
		Vector3 v = getCoords(body, time);
		
		double jd = JD(time);
		if (!v.isZero()) {
			Log.i("DB", "Data was cached... using it");
			return v;
		}
		
		Log.i("DB", "Calculating new data for " + body + " at: " + jd);
		Vector3 newv;
		if (body.equals("earth")) {
			newv = new Vector3(earthCoords(jd));
		} else if(body.equals("jupiter")) {
			newv = new Vector3(jupiterCoords(jd));
		} else if(body.equals("io")) {
			newv = new Vector3(ioCoords(jd));
		} else if(body.equals("callisto")) {
			newv = new Vector3(callistoCoords(jd));
		} else if(body.equals("ganymede")) {
			newv = new Vector3(ganymedeCoords(jd));
		} else if(body.equals("europa")) {
			newv = new Vector3(europaCoords(jd));
		} else {
			newv = new Vector3();
		}
		
		// cache this for later
		addCoords(body, time, newv);
		
		return newv;		
	}
	
	public JovianMoons getMoons(long time)
	{
		
		JovianMoons jv = new JovianMoons();
		jv.jd = JD(time);

		Vector3 earth = lookup("earth", time);
		Vector3 jupiter = lookup("jupiter", time);
		
		Vector3 callisto = lookup("callisto", time);
		jv.callisto = moonPos(callisto, earth, jupiter);

		Vector3 io = lookup("io", time);
		jv.io = moonPos(io, earth, jupiter);

		Vector3 europa = lookup("europa", time);
		jv.europa = moonPos(europa, earth, jupiter);

		Vector3 ganymede = lookup("ganymede", time);
		jv.ganymede = moonPos(ganymede, earth, jupiter);
		
		return jv;
	}
	
	// Basic conversion to julian date
	public static double JD(long mils)
	{
		return ((mils / (86400.0 * 1000.0) ) + 2440587.5);
	}
	
	static {
		System.loadLibrary("solarsym");
	}
}
