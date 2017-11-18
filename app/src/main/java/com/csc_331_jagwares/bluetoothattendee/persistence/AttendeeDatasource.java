package com.csc_331_jagwares.bluetoothattendee.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.util.Log;


import java.io.File;
import java.util.ArrayList;

public class AttendeeDatasource {
    private File dbPath;
    private SQLiteDatabase db;
    private static AttendeeDatasource datasourceInstance;

    public AttendeeDatasource(String dbPath) {
        this.dbPath = new File(dbPath);
    }

    private static final String DATABASE_NAME = "attendeeDatabase";

    private AttendeeDatasource(Context context) {
        this.dbPath = new File(context.getFilesDir(), "bluetoothAttendee.db");
    }

    public static synchronized AttendeeDatasource getInstance(Context context) {
        if (datasourceInstance == null) {
            datasourceInstance = new AttendeeDatasource(context.getApplicationContext());
        }
        return datasourceInstance;
    }

    public void open() throws SQLException {
        this.db = SQLiteDatabase.openDatabase(
                dbPath.getPath(), null,
                SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    /**
     * Create tables. Must be called when opening the database for
     * the first time.
     *
     * @throws SQLException
     */
    public void initializeDatabase() throws SQLException {
        //db.execSQL("DELETE FROM SQLITE_MASTER");
        db.execSQL("PRAGMA foreign_keys=ON");
        db.execSQL("CREATE TABLE tblClass ( \n"
            + "pk INTEGER PRIMARY KEY, \n"
            + "className TEXT, \n"
            + "CONSTRAINT classUnique UNIQUE(className)"
            + ")"
        );
        db.execSQL("CREATE TABLE tblStudent ( \n"
            + "pk INTEGER PRIMARY KEY, \n"
            + "jagNumber TEXT, \n"
            + "firstName TEXT, \n"
            + "lastName TEXT, \n"
            + "emailAddress TEXT, \n"
            + "macAddress TEXT, \n"
            + "CONSTRAINT studentUnique UNIQUE (jagNumber)"
            + ")"
        );
        db.execSQL("CREATE TABLE tblEnrollment ( \n"
            + "student INTEGER REFERENCES tblStudent(pk), \n"
            + "class INTEGER REFERENCES tblClass(pk), \n"
            + "CONSTRAINT enrollUnique UNIQUE(student, class) \n"
            + ")"
        );
//        db.execSQL("CREATE TABLE tblClassSession ( \n"
//            + "sessionID INTEGER PRIMARY KEY, \n"
//            + "className TEXT REFERENCES tblClass(className), \n"
//            + "date TEXT NOT NULL, \n"
//            + "CONSTRAINT sessUnique UNIQUE (className, date)"
//            + ")"
//        );
//        db.execSQL("CREATE TABLE tblAttendance (\n"
//            + "classSession INTEGER REFERENCES tblClassSession(sessionID), \n"
//            + "jagNumber TEXT REFERENCES tblStudent(jagNumber)"
//            + ")"
//        );
    }

    /**
     * Close the data source.
     */
    public void close() {
        db.close();
    }

    // ====================
    // Begin write methods.
    // ====================

    /**
     * Create or update a class with the given className.
     *
     * @param cls
     * @return void
     */
    public void insertClass(Class cls) {
        ContentValues row = cls.toContentValues();
        if (cls.getPk() == 0) {
            row.remove("pk");
        }
        db.beginTransaction();
        try {
            long pk = db.insertWithOnConflict("tblClass", null,
                    row,
                    SQLiteDatabase.CONFLICT_REPLACE);
            if (cls.getPk() == 0) {
                cls.setPk(pk);
            }
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }

    /**
     * Create or update student with given jagNumber.
     *
     * @param student
     * @return void
     */
    public void insertStudent(Student student) {
        ContentValues row = student.toContentValues();
        if (student.getPk() == 0) {
            row.remove("pk");
        }
        db.beginTransaction();
        try {
            long pk = db.insertWithOnConflict(
                    "tblStudent",
                    null,
                    row,
                    SQLiteDatabase.CONFLICT_REPLACE);
            if (student.getPk() == 0) {
                student.setPk(pk);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void insertClassSession(Class cls, SimpleDateFormat date) {

        //db.insertWithOnConflict()
    }

    /**
     * Add student with a given Jag Number to the class.
     *
     * @param student
     * @param cls
     */
    public void enrollStudent(Student student, Class cls) {
        ContentValues row = new ContentValues();
        row.put("student", student.getPk());
        row.put("class", cls.getPk());
        db.beginTransaction();
        try {
            db.insertWithOnConflict(
                    "tblEnrollment",
                    null,
                    row,
                    SQLiteDatabase.CONFLICT_REPLACE
            );
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // =================
    // End write methods
    // =================

    // ==================
    // Begin read methods
    // ==================

    /**
     * Return class with the given name, else null;
     *
     * @param className
     * @return Class
     */
    public Class getClassByName(String className) {
        Cursor c = db.rawQuery("SELECT * FROM tblClass WHERE className = ?",
                new String[]{className}
        );
        if (c.moveToNext()) {
            return Class.cursorToModel(this, c);
        } else {
            return null;
        }
    }

    public ArrayList<Class> getAllClasses() {
        Cursor c = db.rawQuery("SELECT * FROM tblClass;", null);
        ArrayList<Class> results = new ArrayList<Class>();
        while (c.moveToNext()) {
            results.add(Class.cursorToModel(this, c));
        }
        return results;
    }



    /**
     * Return student with given Jag number, else null.
     *
     * @param jagNumber
     * @return
     */
    public Student getStudentByJagNumber(String jagNumber) {
        Cursor c = db.rawQuery("SELECT * FROM tblStudent WHERE jagNumber = ?",
                new String[]{jagNumber});
        if (c.moveToNext()) {
            return Student.cursorToModel(this, c);
        }
        return null;
    }

    String STUDENTS_IN_CLASS_QUERY =
            "SELECT s.pk, s.jagNumber, s.firstName, s.lastName, s.emailAddress, s.macAddress \n"
            + "FROM tblClass c \n"
            + "JOIN tblEnrollment e ON c.pk = e.class \n"
            + "JOIN tblStudent s ON e.student = s.pk \n"
            + "WHERE c.className = ? \n"
            ;

    public ArrayList<Student> getStudentsInClass(String className) {
        Cursor c = db.rawQuery(STUDENTS_IN_CLASS_QUERY,
                new String[]{className});
        ArrayList<Student> students = new ArrayList<>();
        while (c.moveToNext()) {
            students.add(Student.cursorToModel(this, c));
        }
        return students;
    }

    /**
     * Return true if student is enrolled in class.
     *
     * @param jagNumber
     * @param className
     * @return boolean
     */
//    // TODO: Rethink this
//    public boolean studentInClass(String jagNumber, String className) {
//        Cursor c = db.rawQuery("SELECT * FROM tblEnrollment WHERE "
//                        + "jagNumber = ? and className = ?",
//                new String[]{jagNumber, className}
//        );
//        return c.moveToFirst();
//    }

    // Begin existence checks
    // ======================

    /**
     * Return true if a record of the student exists, else false.
     *
     * @param jagNumber
     * @return boolean
     */
    public boolean studentExists(String jagNumber) {
        Cursor c = db.rawQuery(
                "SELECT * FROM tblStudent WHERE jagNumber = ?",
                new String[]{jagNumber}
        );
        return c.moveToFirst(); //Returns false if cursor empty.
    }

    /**
     * Return true if class exists, else false
     *
     * @param className
     * @return
     */
    public boolean classExists(String className) {
        Cursor c = db.rawQuery(
                "SELECT * FROM tblClass WHERE className = ?",
                new String[]{className}
        );
        return c.moveToFirst();
    }
}
