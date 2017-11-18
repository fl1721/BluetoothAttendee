package com.csc_331_jagwares.bluetoothattendee.fragments;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.csc_331_jagwares.bluetoothattendee.R;
import com.csc_331_jagwares.bluetoothattendee.activities.ClassActivity;
import com.csc_331_jagwares.bluetoothattendee.adapters.AttendanceAdapter;
import com.csc_331_jagwares.bluetoothattendee.adapters.StudentEntryAdapter;
import com.csc_331_jagwares.bluetoothattendee.persistence.AttendeeDatasource;
import com.csc_331_jagwares.bluetoothattendee.persistence.Class;
import com.csc_331_jagwares.bluetoothattendee.persistence.Student;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TakeAttendanceFragment extends Fragment {

    private AttendeeDatasource datasource;

    private View view;

    private String className;
    private Class classEntry;
    private ArrayList<Student> students;

    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    AttendanceAdapter adapter;

    private ArrayList<BluetoothDevice> devices;


    public TakeAttendanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the main activity title.
        getActivity().setTitle("Take Attendance");
        // Inflate the layout for this fragment.
        View view = inflater.inflate(R.layout.fragment_take_attendance, container, false);

        // Get Datasource object from the ClassActivity.
        datasource = ((ClassActivity) getActivity()).getDatasource();

        // Get classEntry from ClassFragment.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            className = bundle.getString("className");
        }
        // Setup Class object.
        classEntry = datasource.getClassByName(className);

        // Get ArrayList of students from the class.
        students = classEntry.getStudents();

        // Get BluetoothAdapter from the ClassActivity.
        mBluetoothAdapter = ((ClassActivity) getActivity()).getBTAdapter();

        final CheckBox cbAttend = view.findViewById(R.id.cbAttend);
        cbAttend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(cbAttend.isChecked()){
                    Log.d("BT", "checked.");
                }else{
                    Log.d("BT", "unchecked.");
                }
            }
        });

        // Setup register devices button.
        final Button takeAttendanceBtn = view.findViewById(R.id.takeAttendanceBtn);
        takeAttendanceBtn.setOnClickListener(new View.OnClickListener()
        {
            boolean clicked = false;

            @Override
            public void onClick(View v)
            {

                // Send a request to enable Bluetooth.
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }

                if (clicked) {
                    takeAttendanceBtn.setText("Take Attendance");
                    clicked = false;
                    mBluetoothAdapter.cancelDiscovery();
                    Log.d("BT", "Cancelled task.");
                } else {
                    takeAttendanceBtn.setText("Stop Taking Attendance");
                    clicked = true;
                    devices = new ArrayList<>();
                    mBluetoothAdapter.startDiscovery();
                    Log.d("BT", "Started task.");
                }
            }
        });

        // Add the students from the ArrayList to the ListView.
        if (students != null) {
            populateListView(view, students);
        }

        // Listen for a ListView entry selection.
        registerClickCallback(view);

        // Register for broadcasts when a device is discovered.
        IntentFilter discoverDevicesFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, discoverDevicesFilter);

        return view;
    }

    private void populateListView(View view, ArrayList<Student> students) {
        // Create the adapter to convert the array to views
        adapter = new AttendanceAdapter(getContext(), students);

        // Attach the adapter to a ListView
        ListView lvStudentList = view.findViewById(R.id.lvStudentList);
        lvStudentList.setAdapter(adapter);
    }

    private void updateListView(Student student) {
        adapter.getData().set(adapter.getData().indexOf(student), student);
        adapter.notifyDataSetChanged();
    }

    private void registerClickCallback(View view) {
        ListView list = view.findViewById(R.id.lvStudentList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student studentEntry = students.get(position);

            }
        });
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            CheckBox cbAttendance = view.findViewById(R.id.cbAttend);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (!devices.contains(device)) {
                    Log.d("BT", "Added " + device.getName() + ":" + device.getAddress());
                    mBluetoothAdapter.cancelDiscovery();
                    for (Student student : students) {
                        if (student.getMacAddress().equals(device.getName())) {
                            view.setBackgroundResource(R.color.colorRegistered);
                            cbAttendance.setChecked(true);
                            updateListView(student);
                            Log.d("BT", student.getJagNumber() + " added with mac " + student.getMacAddress());
                        }
                    }
                    devices.add(device);
                    mBluetoothAdapter.startDiscovery();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the ACTION_FOUND receive
        // and cancel device discovery.
        mBluetoothAdapter.cancelDiscovery();
        getActivity().unregisterReceiver(mReceiver);
    }

}