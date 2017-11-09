package com.csc_331_jagwares.bluetoothattendee.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.csc_331_jagwares.bluetoothattendee.R;
import com.csc_331_jagwares.bluetoothattendee.persistence.model.Student;

import java.util.ArrayList;

public class StudentEntryAdapter extends ArrayAdapter {

    // Holds the list of classes from the database to be added to the ListView.
    private ArrayList<Student> studentEntries;

    public StudentEntryAdapter(Context context, ArrayList<Student> studentEntries) {
        super(context, 0, studentEntries);
        this.studentEntries = studentEntries;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.student_entry, null);

        // Set object values to the ListView layout elements.
        Student studentEntry = (Student) getItem(position);
        TextView tvStudentName = view.findViewById(R.id.tvStudentName);
        TextView tvStudentId = view.findViewById(R.id.tvStudentId);
        TextView tvStudentDeviceRegistered = view.findViewById(R.id.tvStudentDeviceRegistered);

        if (studentEntry != null) {
            tvStudentName.setText(studentEntry.getFirstName() + " " + studentEntry.getLastName());
            tvStudentId.setText(studentEntry.getJagNumber());
            if (studentEntry.getMacAddress() != null) {
                view.setBackgroundResource(R.color.colorRegistered);
                tvStudentDeviceRegistered.setText("Device Registered: " + studentEntry.getMacAddress());
            } else {
                tvStudentDeviceRegistered.setText("Device not registered.");
            }
        }

        return view;
    }

    public ArrayList<Student> getData() {
        return studentEntries;
    }

}
