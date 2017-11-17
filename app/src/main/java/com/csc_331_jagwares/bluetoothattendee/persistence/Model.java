package com.csc_331_jagwares.bluetoothattendee.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.csc_331_jagwares.bluetoothattendee.persistence
        .AttendeeDatasource;

/**
 * Created by steven on 10/6/2017.
 */

abstract public class Model {

    AttendeeDatasource datasource;

    protected long pk;

    long getPk() {
        return pk;
    }
    void setPk(long pk) {
        this.pk = pk;
    }
    // Get value of field from cursor.
    static String __(Cursor c, String fieldName) {
        return c.getString(c.getColumnIndex(fieldName));
    }

    public Model(AttendeeDatasource datasource) {
        this.datasource = datasource;
    }

    abstract public void save();

    abstract public ContentValues toContentValues();
}
