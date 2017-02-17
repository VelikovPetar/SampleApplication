package com.example.velik_000.sampleapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by velik_000 on 02/16/2017.
 */

public class RecordsContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.velik_000.sampleapplication.contentprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + RecordsTable.RECORDS_TABLE);
    private static final int ALL_RECORDS = 1;
    private static final int SINGLE_RECORD = 2;
    private static final UriMatcher uriMathcer;
    static {
        uriMathcer = new UriMatcher(UriMatcher.NO_MATCH);
        uriMathcer.addURI(AUTHORITY, RecordsTable.RECORDS_TABLE, ALL_RECORDS);
        uriMathcer.addURI(AUTHORITY, RecordsTable.RECORDS_TABLE + "/#", SINGLE_RECORD);
    }
    private RecordsDatabaseHelper database;

    @Override
    public boolean onCreate() {
        database = new RecordsDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = database.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(RecordsTable.RECORDS_TABLE);
        switch(uriMathcer.match(uri)) {
            case ALL_RECORDS:
                break;
            case SINGLE_RECORD:
                String id = uri.getLastPathSegment();
                builder.appendWhere(RecordsTable.COLUMN_ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("unsupported uri : " + uri);
        }
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(uriMathcer.match(uri)) {
            case SINGLE_RECORD:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + RecordsTable.RECORDS_TABLE;
            case ALL_RECORDS:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." +RecordsTable.RECORDS_TABLE;
            default:
                throw new IllegalArgumentException("Unsupported Uri : " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = database.getWritableDatabase();
        switch(uriMathcer.match(uri)) {
            case ALL_RECORDS:
                break;
            default:
                throw new IllegalArgumentException("Unsupported Uri : " + uri);
        }
        long id = db.insert(RecordsTable.RECORDS_TABLE, null, values);
        if(id != -1) {
            getContext().getContentResolver().notifyChange(uri, null);
            return Uri.parse(CONTENT_URI + "/" + id);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = database.getWritableDatabase();
        switch(uriMathcer.match(uri)) {
            case ALL_RECORDS:
                break;
            case SINGLE_RECORD:
                String id = uri.getLastPathSegment();
                String where = RecordsTable.COLUMN_ID + "=" + id;
                if(!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                selection = where;
                break;
            default:
                throw new IllegalArgumentException("Unsupported Uri : " + uri);
        }
        int count = db.delete(RecordsTable.RECORDS_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = database.getWritableDatabase();
        switch(uriMathcer.match(uri)) {
            case ALL_RECORDS:
                break;
            case SINGLE_RECORD:
                String id = uri.getLastPathSegment();
                String where = RecordsTable.COLUMN_ID + "=" + id;
                if(!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                selection = where;
                break;
            default:
                throw new IllegalArgumentException("Unsupported Uri : " + uri);
        }
        int count = db.update(RecordsTable.RECORDS_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
