package org.onpanic.uninstallapps.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppsDataDb extends SQLiteOpenHelper {

    public static final String APPS_TABLE_NAME = "apps";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "uninstall_apps";

    private static final String APPS_TABLE_CREATE =
            "CREATE TABLE " + APPS_TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "packageName TEXT, " +
                    "enabled INTEGER DEFAULT 1);";


    public AppsDataDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(APPS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

