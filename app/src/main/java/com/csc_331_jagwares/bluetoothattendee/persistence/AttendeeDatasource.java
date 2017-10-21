package com.csc_331_jagwares.bluetoothattendee.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.csc_331_jagwares.bluetoothattendee.persistence.model.Class;


import com.csc_331_jagwares.bluetoothattendee.persistence.model.Student;

import java.io.File;
import java.util.ArrayList;

public class AttendeeDatasource {
    private File dbPath;
    private SQLiteDatabase db;

    private String STUDENT_COLUMNS_STRING =
            "jagNumber, firstName, lastName, emailAddress, macAddress";

    private String CLASS_COLUMNS_STRING = "className";

    public AttendeeDatasource(String dbPath) {
        this.dbPath = new File(dbPath);
    }

    public void open() throws SQLException {
        this.db = SQLiteDatabase.openDatabase(
                dbPath.getPath(), null,
                SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    /**
     * Drop any existing tables and recreate them. Must be called when
     * opening the database for the first time.
     *
     * @throws SQLException
     */
    public void initializeDatabase() throws SQLException {
        //db.execSQL("DELETE FROM SQLITE_MASTER");
        db.execSQL("PRAGMA foreign_keys=ON");
        db.execSQL("CREATE TABLE tblClass (className TEXT PRIMARY KEY)");
        db.execSQL("CREATE TABLE tblStudent ( \n"
            + "jagNumber TEXT PRIMARY KEY, "
            + "firstName TEXT, \n"
            + "lastName TEXT, \n"
            + "emailAddress TEXT, \n"
            + "macAddress TEXT \n"
            + ");"
        );
        db.execSQL("CREATE TABLE tblEnrollment ( \n"
            + "jagNumber TEXT REFERENCES tblStudent(jagNumber), \n"
            + "className TEXT REFERENCES tblClass(className))"
        );
    }

    /**
     * Close the data source.
     */
    public void close() {
        db.close();
    }

    // Begin write methods.
    // ====================

    /**
     * Create or update a class with the given className.
     *
     * @param className
     * @return void
     */
    public void insertClass(String className) {
        ContentValues row = new ContentValues();
        row.put("className", className);
        db.beginTransaction();
        try {
            db.insertWithOnConflict("tblClass", null, row,
                    SQLiteDatabase.CONFLICT_REPLACE);
//            db.execSQL("INSERT INTO tblClass(className) VALUES (?)",
//                    new String[]{className}
//            );
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Create or update student with given jagNumber.
     *
     * @param jagNumber
     * @param firstName
     * @param lastName
     * @return void
     */
    public void insertStudent(String jagNumber, String firstName, String lastName,
                              String emailAddress, String macAddress) {
        // Todo: Models should be subclasses of ContentValues
        // so they can be passed directly to these methods.
        ContentValues row = new ContentValues();
        row.put("jagNumber", jagNumber);
        row.put("firstName", firstName);
        row.put("lastName", lastName);
        row.put("emailAddress", emailAddress);
        row.put("macAddress", macAddress);
        db.beginTransaction();
        try {
            db.insertWithOnConflict("tblStudent", null, row,
                    SQLiteDatabase.CONFLICT_REPLACE);
//            db.execSQL(
//                    "INSERT INTO tblStudent ( \n"
//                            + STUDENT_COLUMNS_STRING
//                            + ") \n"
//                            + "VALUES (?, ?, ?, ?, ?)",
//                    new String[]{jagNumber, firstName, lastName,
//                    emailAddress, macAddress}
//            );
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Add student with a given Jag Number to the class.
     *
     * @param jagNumber
     * @param className
     * @return
     */
    public void enrollStudent(String jagNumber, String className) {
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO tblEnrollment(jagNumber, className) "
                            + "VALUES (?, ?)",
                    new String[]{jagNumber, className}
            );
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // =================
    // End write methods

    // Begin read methods
    // ==================

    /**
     * Return class with the given name, else null;
     *
     * @param className
     * @return Class
     */
    public Class getClassByName(String className) {
        Cursor c = db.rawQuery("SELECT className FROM tblClass WHERE className = ?",
                new String[]{className}
        );
        if (c.moveToNext()) {
            return new Class(this, c.getString(0));
        } else {
            return null;
        }
    }

    public ArrayList<Class> getAllClasses() {
        Cursor c = db.rawQuery("SELECT * FROM tblClass;", null);
        ArrayList<Class> results = new ArrayList<Class>();
        while (c.moveToNext()) {
            results.add(new Class(this, c.getString(0)));
        }
        return results;
    }

    // Short for c.getString(c.getColumnIndex(colName));
    private String __(Cursor c, String fieldName) {
        return c.getString(c.getColumnIndex(fieldName));
    }

    /**
     * Return student with given Jag number, else null.
     *
     * @param jagNumber
     * @return
     */
    /// TODO: use explicit field selection.
    public Student getStudentByJagNumber(String jagNumber) {
        Cursor c = db.rawQuery("SELECT " + STUDENT_COLUMNS_STRING + " FROM tblStudent WHERE jagNumber = ?",
                new String[]{jagNumber});
        if (c.moveToNext()) {
            return new Student(this,
                    __(c, "jagNumber"),
                    __(c, "firstName"),
                    __(c, "lastName"),
                    __(c, "emailAddress"),
                    __(c, "macAddress")
            );
        }
        return null;
    }

    String STUDENTS_IN_CLASS_QUERY =
            "SELECT s.jagNumber, s.firstName, s.lastName, s.emailAddress, s.macAddress \n"
            + "FROM tblClass c \n"
            + "JOIN tblEnrollment e ON c.className = e.className \n"
            + "JOIN tblStudent s ON e.jagNumber = s.jagNumber \n"
            + "WHERE c.className = ? \n"
            ;

    public ArrayList<Student> getStudentsInClass(String className) {
        Cursor c = db.rawQuery(STUDENTS_IN_CLASS_QUERY,
                new String[]{className});
        ArrayList<Student> students = new ArrayList<>();
        while (c.moveToNext()) {
            students.add(new Student(
                    this,
                    __(c, "jagNumber"),
                    __(c, "firstName"),
                    __(c, "lastName"),
                    __(c, "emailAddress"),
                    __(c, "macAddress"))
            );
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
    public boolean studentInClass(String jagNumber, String className) {
        Cursor c = db.rawQuery("SELECT * FROM tblEnrollment WHERE "
                        + "jagNumber = ? and className = ?",
                new String[]{jagNumber, className}
        );
        return c.moveToFirst();
    }

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
