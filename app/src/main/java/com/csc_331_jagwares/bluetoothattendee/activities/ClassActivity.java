package com.csc_331_jagwares.bluetoothattendee.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.csc_331_jagwares.bluetoothattendee.R;
import com.csc_331_jagwares.bluetoothattendee.fragments.ClassFragment;
import com.csc_331_jagwares.bluetoothattendee.persistence.AttendeeDatasource;
import com.csc_331_jagwares.bluetoothattendee.persistence.model.Class;

public class ClassActivity extends AppCompatActivity {

    private String className;
    private Class classEntry;

    private AttendeeDatasource datasource;

    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        // Setup datasource.
        datasource = AttendeeDatasource.getInstance(this);

        // Receive a class name.
        try {
            Intent intent = getIntent();
            className = intent.getStringExtra("className");
            Log.d("WHOA", className);
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Setup Class object.
        classEntry = datasource.getClassByName(className);
        // Setup Bluetooth adapter.
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        // Open ClassFragment initially.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainLayout, new ClassFragment());
        ft.commit();
    }

    public Class getClassEntry() {
        // This method is used to send a class to a fragment.
        return classEntry;
    }

    public BluetoothAdapter getBTAdapter() {
        // This method is used to send a BluetoothAdapter to a fragment.
        return mBluetoothAdapter;
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

    public AttendeeDatasource getDatasource() {
        // This method is used to send a datasource object to a fragment.
        return datasource;
    }

}
