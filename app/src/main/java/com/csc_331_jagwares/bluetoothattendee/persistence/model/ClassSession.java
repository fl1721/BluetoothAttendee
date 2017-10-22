package com.csc_331_jagwares.bluetoothattendee.persistence.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;

import com.csc_331_jagwares.bluetoothattendee.persistence
        .AttendeeDatasource;

/**
 * Created by steven on 10/21/2017.
 */

public class ClassSession extends Model {

    private String className;
    private SimpleDateFormat date;

    public ClassSession(AttendeeDatasource datasource, SimpleDateFormat date) {
        super(datasource);
        this.date = date;
    }

    public ContentValues toContentValues() {
        ContentValues row =  new ContentValues();
        row.put("className", className);
        row.put("date", date.toString());
        return row;
    }

    public void save() {}
}
