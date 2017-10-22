package com.csc_331_jagwares.bluetoothattendee.persistence.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.csc_331_jagwares.bluetoothattendee.persistence
        .AttendeeDatasource;

/**
 * Created by steven on 10/3/2017.
 */

public class Student extends Model {

    private String jagNumber;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String macAddress;

    /**
     * Create a new student. The new instance will not be
     * saved to the database until .save() is called.
     *
     * To modify the instance, call the relevant setter method(s),
     * then call save(). jagNumber is read-only.
     *
     * To enroll the student in a class, call enroll(). This
     * takes effect immediately; save() is unnecessary.
     * NOTE: enroll() will fail when called when called with a new
     * student or class that has not been saved.
     *
     * @param datasource
     * @param jagNumber
     * @param firstName
     * @param lastName
     * @param emailAddress
     * @param macAddress
     */
    public Student (AttendeeDatasource datasource, String jagNumber, String firstName, String lastName,
                    String emailAddress, String macAddress) {
        super(datasource);
        this.jagNumber = jagNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.macAddress = macAddress;
    }

    public ContentValues toContentValues() {
        ContentValues row = new ContentValues();
        row.put("jagNumber", jagNumber);
        row.put("firstName", firstName);
        row.put("lastName", lastName);
        row.put("emailAddress", emailAddress);
        row.put("macAddress", macAddress);
        return row;
    }

    /**
     * Enroll student in a given class.
     *
     * @param cls
     */
    public void enroll(Class cls) {
        datasource.enrollStudent(this, cls);
    }
    /**
     * Save new objects, or save changes made by setter methods.
     * Not needed after calling Student.enroll(Class).
     */
    public void save() {
        datasource.insertStudent(this);
    }

    public String getJagNumber() {
        return jagNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
