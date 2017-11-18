package com.csc_331_jagwares.bluetoothattendee.persistence;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

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
        row.put("pk", pk);
        row.put("className", className);
        return row;
    }

    public static Class cursorToModel(AttendeeDatasource datasource, Cursor c) {
        Class cls = new Class(
                datasource,
                __(c, "className")
        );
        cls.setPk(Long.parseLong(__(c, "pk")));
        return cls;
    }

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
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
