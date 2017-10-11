package com.csc_331_jagwares.bluetoothattendee.persistence.suite;

//import android.support.test.filters.SmallTest;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.csc_331_jagwares.bluetoothattendee.persistence
        .AttendeeDatasource;
import com.csc_331_jagwares.bluetoothattendee.persistence.model.Class;
import com.csc_331_jagwares.bluetoothattendee.persistence.model.Student;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
        datasource.addClass("Underwater Basket Weaving");
        datasource.addClass("Calculus");
        datasource.addClass("Pigonometry");
        datasource.addStudent("J99999999", "Jimmy", "James");
        datasource.addStudent("J88888888", "Willy", "Wonka");
        datasource.enrollStudent("J99999999", "Underwater Basket Weaving");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        datasource.close();
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
        assertFalse(datasource.studentInClass("J88888888", "Underwater Basket Weaving"));
    }
}