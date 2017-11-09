package com.csc_331_jagwares.bluetoothattendee.persistence.suite;

//import android.support.test.filters.SmallTest;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.csc_331_jagwares.bluetoothattendee.persistence
        .AttendeeDatasource;
import com.csc_331_jagwares.bluetoothattendee.persistence.model.Class;
import com.csc_331_jagwares.bluetoothattendee.persistence.model.Student;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by steven on 10/7/2017.
 */
@RunWith(AndroidJUnit4.class)
public class AttendeeDatasourceTest {
    static AttendeeDatasource datasource;
    static Context context = InstrumentationRegistry.getTargetContext();
    static File dbFile = new File(context.getFilesDir(), "foo.db");

    @BeforeClass
    public static void setUp() throws Exception {
        if (dbFile.exists()) {
            dbFile.delete();
        }
        dbFile.createNewFile();
        datasource = new AttendeeDatasource(dbFile.getPath());
        datasource.open();
        datasource.initializeDatabase();
        // Create classes.
        Class ubw = new Class(datasource, "Underwater Basket Weaving");
        ubw.save();
        Class calculus = new Class(datasource, "Calculus");
        calculus.save();
        Class pigonometry = new Class(datasource, "Pigonometry");
        pigonometry.save();
        // Calling save more than once is wasteful, but has no
        // visible effect.
        pigonometry.save();

        // Create students.
        Student jimmy = new Student(datasource,
                "J99999999", "Jimmy", "James",
                "jimmyjames@foo.bar", "00-14-22-01-23-45"
                );
        jimmy.save();
        Student willy = new Student(datasource,
                "J88888888", "Willy", "Wonka",
                "willywonka@foo.bar", "00-14-22-01-23-46");
        willy.save();
        // Hobbits don't go to school.
        Student frodo = new Student(datasource,
                "JOOGGGGGG", "Frodo", "Baggins",
                null, null);
        frodo.save();

        // Class.addStudent() and Student.enroll() are different
        // ways of doing the same thing.
        // You don't have to call save() after these.
        ubw.addStudent(jimmy);
        willy.enroll(ubw);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        datasource.close();
    }

    /*
    You cannot enroll a student in a class unless BOTH exist in the
    database. That means you have to have initially read them
    from the datasource or called save() after instantiating them.
     */
    @Test(expected = SQLiteConstraintException.class)
    public void enrollInUnsavedClass() throws Exception {
        Student jimmy = datasource.getStudentByJagNumber("J99999999");
        Class unsaved = new Class(datasource, "Oops.");
        jimmy.enroll(unsaved);
    }

    /*
    Begin Class model tests.
     */
    @Test
    public void testClassExists() throws Exception {
        assertTrue(datasource.classExists("Underwater Basket Weaving"));
    }

    @Test
    public void testGetClassByName() throws Exception {
        Class cls = datasource.getClassByName("Underwater Basket Weaving");
        assertTrue(cls.getClassName().equals("Underwater Basket Weaving"));
    }
    @Test
    public void testGetAllClasses() throws Exception {
        assertTrue(datasource.getAllClasses().size() == 3);
    }

    /*
    Begin Student model tests.
     */
    @Test
    public void testStudentExists() throws Exception {
        assertTrue(datasource.studentExists("J99999999"));
    }

    @Test
    public void testGetStudentByJagNumber() throws Exception {
        Student student = datasource.getStudentByJagNumber("J88888888");
        assertTrue(student.getFirstName().equals("Willy"));
    }

    @Test
    public void testStudentInClass() throws Exception {
        assertTrue(datasource.studentInClass("J99999999", "Underwater Basket Weaving"));
        assertTrue(datasource.studentInClass("J88888888", "Underwater Basket Weaving"));
        assertFalse(datasource.studentInClass("JOOGGGGGG", "Underwater Basket Weaving"));
    }

    @Test
    public void testGetStudentsInClass() {
        Class cls = datasource.getClassByName("Underwater Basket Weaving");
        ArrayList<Student> students = cls.getStudents();
        assertTrue(students.size() == 2);
        assertTrue(students.get(0).getFirstName().equals("Jimmy"));
    }

    @Test
    public void testSaveStudent() {
        Student jimmy = datasource.getStudentByJagNumber("J99999999");
        assertNotNull(jimmy);
        jimmy.setLastName("Jack");
        jimmy.save();
        jimmy = datasource.getStudentByJagNumber("J99999999");
        assertTrue(jimmy.getLastName().equals("Jack"));
    }
}