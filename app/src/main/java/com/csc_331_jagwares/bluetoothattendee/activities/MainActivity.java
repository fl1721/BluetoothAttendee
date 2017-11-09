package com.csc_331_jagwares.bluetoothattendee.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.csc_331_jagwares.bluetoothattendee.R;
import com.csc_331_jagwares.bluetoothattendee.fragments.ClassesFragment;
import com.csc_331_jagwares.bluetoothattendee.fragments.HelpFragment;
import com.csc_331_jagwares.bluetoothattendee.fragments.ReportsFragment;
import com.csc_331_jagwares.bluetoothattendee.fragments.SettingsFragment;
import com.csc_331_jagwares.bluetoothattendee.persistence.AttendeeDatasource;
import com.csc_331_jagwares.bluetoothattendee.persistence.model.Class;
import com.csc_331_jagwares.bluetoothattendee.persistence.model.Student;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AttendeeDatasource datasource;
    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the layout of the activity.
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setup datasource.
        // TODO: Initialize database if it doesn't exist.
        datasource = AttendeeDatasource.getInstance(this);
        datasource.open();
        //datasource.initializeDatabase();

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

        // Request permissions required for the app.
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
        }

        // Update navigation drawer profile TextViews.
        View header = navigationView.getHeaderView(0);
        TextView tvHeaderName = header.findViewById(R.id.tvInstructorName);
        TextView tvHeaderEmail = header.findViewById(R.id.tvInstructorEmail);
        tvHeaderName.setText("Ryan Benton");
        tvHeaderEmail.setText("rbenton@southalabama.edu");

        // Checks first item in the navigation drawer initially.
        navigationView.setCheckedItem(R.id.nav_classes);

        // Open ClassesFragment initially.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainLayout, new ClassesFragment());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the action menu options.
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_classes) {
            fragment = new ClassesFragment();
        } else if (id == R.id.nav_reports){
            fragment = new ReportsFragment();
        } else if (id == R.id.nav_settings){
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_help){
            fragment = new HelpFragment();
        }

        // Switch to selected fragment.
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainLayout, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        //datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //datasource.close();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //datasource.close();
        super.onDestroy();
    }

    public AttendeeDatasource getDatasource() {
        // This method is used to send a datasource object to a fragment.
        return datasource;
    }

}
