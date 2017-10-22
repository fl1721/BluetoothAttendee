package com.csc_331_jagwares.bluetoothattendee.persistence.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.csc_331_jagwares.bluetoothattendee.persistence
        .AttendeeDatasource;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * Created by steven on 10/3/2017.
 */
public class Class extends Model {

    private String className;

    /**
     * className is read-only.
     *
     * @param datasource
     * @param className
     */
    public Class(AttendeeDatasource datasource, String className) {
        super(datasource);
        this.className = className;
    }

    public ContentValues toContentValues() {
        ContentValues row = new ContentValues();
        row.put("className", className);
        return row;
    }

    public String getClassName() {
        return className;
    }

    public ArrayList<Student> getStudents() {
        return datasource.getStudentsInClass(className);
    }

    public void addStudent(Student student) {
        datasource.enrollStudent(student, this);
    }
    /**
     * Save new objects, or save changes made by setter methods.
     * Not needed after calling Student.enroll(Class).
     */
    public void save() {
        datasource.insertClass(this);
    }
}
