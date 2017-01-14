package org.onpanic.uninstallapps.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.onpanic.uninstallapps.database.AppsDataDb;


public class AppsProvider extends ContentProvider {
    private static final String AUTH = "org.onpanic.uninstallapps.providers.AppsProvider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTH + "/apps");

    //UriMatcher
    private static final int APPS = 1;
    private static final int APPS_ID = 2;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTH, "apps", APPS);
        uriMatcher.addURI(AUTH, "apps/#", APPS_ID);
    }

    private AppsDataDb appsDataDb;
    private Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        appsDataDb = new AppsDataDb(mContext);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String where = selection;
        if (uriMatcher.match(uri) == APPS_ID) {
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = appsDataDb.getReadableDatabase();

        return db.query(AppsDataDb.APPS_TABLE_NAME, projection, where,
                selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case APPS:
                return "vnd.android.cursor.dir/vnd.uninstallapps.apps";
            case APPS_ID:
                return "vnd.android.cursor.item/vnd.uninstallapps.app";
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long regId;

        SQLiteDatabase db = appsDataDb.getWritableDatabase();

        regId = db.insert(AppsDataDb.APPS_TABLE_NAME, null, values);

        mContext.getContentResolver().notifyChange(CONTENT_URI, null);

        return ContentUris.withAppendedId(CONTENT_URI, regId);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        String where = selection;
        if (uriMatcher.match(uri) == APPS_ID) {
            where = "_id=" + uri.getLastPathSegment();
        }

        SQLiteDatabase db = appsDataDb.getWritableDatabase();

        Integer rows = db.delete(AppsDataDb.APPS_TABLE_NAME, where, selectionArgs);

        mContext.getContentResolver().notifyChange(CONTENT_URI, null);

        return rows;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = appsDataDb.getWritableDatabase();
        Integer rows = db.update(AppsDataDb.APPS_TABLE_NAME, values, selection, selectionArgs);
        mContext.getContentResolver().notifyChange(CONTENT_URI, null);
        return rows;
    }

    public static final class App implements BaseColumns {

        public static final String PACKAGE_NAME = "packageName";
        public static final String ENABLED = "enabled";

        private App() {
        }
    }
}
