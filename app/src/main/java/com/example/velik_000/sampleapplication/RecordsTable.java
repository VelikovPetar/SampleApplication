package com.example.velik_000.sampleapplication;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by velik_000 on 02/16/2017.
 */

public class RecordsTable {

    public static final String RECORDS_TABLE = "records";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INFO = "info";
    public static final String COLUMN_SAVED = "saved";

    private static final String CREATE_DATABASE = "create table " + RECORDS_TABLE +
            "(" + COLUMN_ID +" integer primary key autoincrement, " + COLUMN_INFO + " text not null, " +
            COLUMN_SAVED + " text not null check(" + COLUMN_SAVED + " IN (\"saved\", \"not saved\")));";

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO
    }
}
