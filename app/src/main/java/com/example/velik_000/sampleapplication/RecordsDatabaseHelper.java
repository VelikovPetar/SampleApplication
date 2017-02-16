package com.example.velik_000.sampleapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by velik_000 on 02/16/2017.
 */

public class RecordsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recordstable.db";
    private static final int DATABASE_VERSION = 1;

    public RecordsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        RecordsTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        RecordsTable.onUpgrade(db, oldVersion, newVersion);
    }
}
