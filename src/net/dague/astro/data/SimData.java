package net.dague.astro.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import static android.provider.BaseColumns._ID;
import static net.dague.astro.data.Constants.*;

public class SimData extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "astrosim.db";
	private static final int DATABASE_VERSION = 2;

	public SimData(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String create1 = "CREATE TABLE " + TABLE_NAME +
		"(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		TIME + " LONG NOT NULL, " +
		BODY + " CHAR(20) NOT NULL, " +
		X + " DOUBLE NOT NULL, " +
		Y + " DOUBLE NOT NULL, " +
		Z + " DOUBLE NOT NULL);";
		
		db.execSQL(create1);
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
