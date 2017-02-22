package com.example.velik_000.sampleapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by velik_000 on 02/19/2017.
 */

public class RecordsCursorAdapter extends CursorAdapter {

    public RecordsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.records_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView rowID = (TextView) view.findViewById(R.id.row_id);
        TextView rowInfo = (TextView) view.findViewById(R.id.row_info);
        TextView rowSaved = (TextView) view.findViewById(R.id.row_saved);

        String idString = cursor.getString(cursor.getColumnIndex(RecordsTable.COLUMN_ID));
        String infoString = cursor.getString(cursor.getColumnIndex(RecordsTable.COLUMN_INFO));
        String savedString = cursor.getString(cursor.getColumnIndex(RecordsTable.COLUMN_SAVED));

        rowID.setText(idString);
        rowInfo.setText(infoString);
        rowSaved.setText(savedString);
    }
}
